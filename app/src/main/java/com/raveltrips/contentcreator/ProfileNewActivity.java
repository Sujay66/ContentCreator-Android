package com.raveltrips.contentcreator;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.raveltrips.contentcreator.async.AsyncComplete;
import com.raveltrips.contentcreator.async.AsyncUploadImage;
import com.raveltrips.contentcreator.async.DownloadImageAsyncTask;
import com.raveltrips.contentcreator.async.DownloadJsonAsyncPutTask;
import com.raveltrips.contentcreator.models.Profile;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileNewActivity extends AppCompatActivity {

    TextView picture,profileheader;
    EditText name, youtubeURL, quickBio;
    CircleImageView imageView;
    int imagePickerType = 1;
     Profile modifiedProfile;
     int REQUEST_CAMERA = 0, SELECT_FILE = 1;
     Button cancelBtn;
     Button saveBtn;
     String filePath = null;
     String fileName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_new);

        Typeface museo = Typeface.createFromAsset(getApplicationContext().getApplicationContext().getAssets(),
                "fonts/Museo700-Regular.ttf");
        Typeface dinlight = Typeface.createFromAsset(getApplicationContext().getApplicationContext().getAssets(),
                "fonts/DIN2014Light.ttf");
        Typeface dindemi = Typeface.createFromAsset(getApplicationContext().getApplicationContext().getAssets(),
                "fonts/DIN 2014 Demi.ttf");
        Typeface montserrat = Typeface.createFromAsset(getApplicationContext().getApplicationContext().getAssets(),
                "fonts/Montserrat-SemiBold.ttf");
        picture = (TextView) findViewById(R.id.uploadapic);
        profileheader = (TextView) findViewById(R.id.profileheader);
        name = (EditText) findViewById(R.id.nametext);
        youtubeURL = (EditText) findViewById(R.id.youtubetext);
        quickBio = (EditText) findViewById(R.id.quickbioText);
        imageView = (CircleImageView) findViewById(R.id.profile_image);
        saveBtn = (Button)findViewById(R.id.save_profile);
        cancelBtn = (Button)findViewById(R.id.cancel_profile);
        setProfileData();

        SpannableString content = new SpannableString("Upload Profile Picture");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        picture.setText(content);
        picture.setTypeface(dinlight);
        SpannableString contentone = new SpannableString(" My Content Creator Profile");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        profileheader.setText(contentone);
        profileheader.setTypeface(dindemi);


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!AppContext.OfflineMode){
                Log.d("ProfileActivity","Saving the profile update changes!!..");
                String updatedName = name.getText().toString();
                String updatedyoutubeURL = youtubeURL.getText().toString();
                String updatedquickBio = quickBio.getText().toString();
                modifiedProfile.setName(updatedName);
                modifiedProfile.setMobNo(updatedyoutubeURL);
                modifiedProfile.setAddress(updatedquickBio);
                if(filePath==null){
                    // oly datd update, no image
                    UpdateProfileServerCall();
                } else {
                    //upload image first, then profile
                    uploadImageToServer();
                }}
                else{
                    ToastMessage("No connection! Try to update later");
                }
              //  setProfileData(); Add later depending on how save is working
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    if(AppContext.profile!=null){
                        finish();
                    }
                }catch(Exception ex) {}
            }
        });

        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
                Snackbar.make(view, "Please select an Image", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void setProfileData() {

        if(AppContext.profile!=null){
            try{
                modifiedProfile = new Profile();
                modifiedProfile.copyFromProfile(AppContext.profile);
                setViewData(AppContext.profile);
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
    }

    private void UpdateProfileServerCall() {
        DownloadJsonAsyncPutTask task = new DownloadJsonAsyncPutTask();
        Gson gson = new Gson();
        task.setBodyContent(gson.toJson(modifiedProfile).toString());
        Log.d("ProfileActivity", "profile json"+gson.toJson(modifiedProfile).toString());
        task.setAsyncComplete(new AsyncComplete() {
            @Override
            public void OnJsonAsyncCompleted(List<String> json) {
                try {
                    Gson gson = new Gson();
                    if (json != null && json.size() > 0) {
                        JSONObject responseModel = new JSONObject(json.get(0));
                        JSONArray jsonArray = responseModel.getJSONArray("payLoad");
                        String status = responseModel.getString("status");
                        if (status != null && status.equalsIgnoreCase("200")) {
                            JSONObject profObj = jsonArray.getJSONObject(0);
                            Profile sProfile = gson.fromJson(profObj.toString(), Profile.class);
                            if(sProfile!=null){
                                Log.d("ProfileActivity", "updated profile:"+gson.toJson(sProfile).toString());
                                ToastMessage("Profile updated successfully!");
                                AppContext.profile.copyFromProfile(sProfile);
                                setViewData(AppContext.profile);
                            }
                        }else{
                            ToastMessage("Failed to update Profile! try again later..");
                        }
                    }
                }
                catch (Exception ex) {
                    Log.d("ProfileActivity", "Exception converting profile from json:" + ex);
                }
            }
        });
        task.execute(AppContext.UPDATE_PROFILE_URL);
    }

    void  uploadImageToServer(){
        AsyncUploadImage imageUpload = new AsyncUploadImage(new AsyncComplete() {
            @Override
            public void OnJsonAsyncCompleted(List<String> json) {
                try {
                    if (json != null && json.size() > 0) {
                        JSONObject responseModel = new JSONObject(json.get(0));
                        String message = responseModel.getString("message");
                        String status = responseModel.getString("status");
                        if (status != null && status.equalsIgnoreCase("200")) {
                            //proceed to next stage...
                            modifiedProfile.setImageUrl(AppContext.BASE_IMAGE_URL+fileName);
                            UpdateProfileServerCall();
                        }
                        else {
                            ToastMessage("Failed to upload image to server!! Try again later!");
                            Log.d("ProfileActivity", "Message from server"+message);
                        }

                        Log.d("ProfileActivity", "image uploaded..filename"+fileName);
                    }
                } catch (Exception ex) {
                    ToastMessage("Failed to upload image to server!! Try again later!");
                    Log.d("ProfileActivity", "Exception converting search trips from json:" + ex);
                }
            }
        });
        imageUpload.setFilePath(filePath);
        imageUpload.execute(AppContext.IMAGE_UPLOAD_URL);

    }
    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Gallery",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileNewActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                //get type, for first time, asks for permission
                if (items[item].equals("Take Photo")) {
                    imagePickerType = 0;
                } else if (items[item].equals("Choose from Gallery")) {
                    imagePickerType = 1;
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                    return;
                }
                if(com.raveltrips.contentcreator.Utils.PermUtility.checkReadPermission(ProfileNewActivity.this)){
                    if (items[item].equals("Take Photo")) {
                        imagePickerType = 0;
                        handleImagepick(0);
                    } else if (items[item].equals("Choose from Gallery")) {
                        imagePickerType = 1;
                        handleImagepick(1);
                    } else if (items[item].equals("Cancel")) {
                        dialog.dismiss();
                    }
                }
            }
        });
        builder.show();
    }

    void handleImagepick(int selection){
        switch(selection){
            case 0:
                cameraIntent();
                break;
            case 1:
                galleryIntent();
                break;
        }
    }

    private void cameraIntent()
    {
        try{
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, REQUEST_CAMERA);
        }catch(Exception ex){
            Log.d("AddTrip","Exception starting camera activity:"+ex);
            ToastMessage(" Failed to start Camera");
        }

    }

    private void galleryIntent()
    {
        try{
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);

        }catch(Exception ex){
            Log.d("AddTrip","Exception starting gallery activity:"+ex);
            ToastMessage(" Failed to Open Gallery");
        }
    }

    public void ToastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }

    public void setViewData(Profile profiledata){

        name.setText(profiledata.getName());
        youtubeURL.setText(profiledata.getMobNo());
        quickBio.setText(profiledata.getAddress());

        if(profiledata.getImageUrl()!=null && !profiledata.getImageUrl().isEmpty()){
            try {
                if (AppContext.USE_PICASSO)
                    Picasso.with(this).load(profiledata.getImageUrl()).into(imageView);
                else {
                    Bitmap bitmap = AppContext.getBitmapFromMemCache(profiledata.getImageUrl());
                    if (bitmap != null) {
                        imageView.setImageBitmap(bitmap);
                    } else {
                        DownloadImageAsyncTask task = new DownloadImageAsyncTask(imageView);
                        task.execute(profiledata.getImageUrl());
                    }
                }
            }catch (Exception ex){ex.printStackTrace();}
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        try{

            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            createFileFromBitmap(thumbnail);
            imageView.setImageBitmap(thumbnail);
        }catch(Exception ex){
            Log.d("AddTrip", "error capturing from camera"+ex);
        }
    }

    public void createFileFromBitmap(Bitmap bitmap) {
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
            Log.d("Utility", "onCaptureImageResult, writing image to disk..");

            File destination = new File(Environment.getExternalStorageDirectory(),
                    System.currentTimeMillis() + ".jpg");

            FileOutputStream fo;
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
            filePath = destination.getPath();
            fileName = destination.getName();
            Log.d("AddTrip", "Image file created at" + filePath);

        } catch (Exception ex) {
            Log.d("AddTrip", "runtime error caught:" + ex);
        }
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        try {
            Bitmap bm = null;
            if (data != null) {
                try {
                    bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                    createFileFromBitmap(bm);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            imageView.setImageBitmap(bm);

        } catch (Exception ex) {
            Log.d("AddTrip", "error capturing from gallery" + ex);
        }
    }

}
