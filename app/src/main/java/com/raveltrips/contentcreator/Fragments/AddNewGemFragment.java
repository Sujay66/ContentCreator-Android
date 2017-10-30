package com.raveltrips.contentcreator.Fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.raveltrips.contentcreator.AppContext;
import com.raveltrips.contentcreator.MainActivity;
import com.raveltrips.contentcreator.R;
import com.raveltrips.contentcreator.Utils.DataBaseHelper;
import com.raveltrips.contentcreator.Utils.PermUtility;
import com.raveltrips.contentcreator.async.AsyncComplete;
import com.raveltrips.contentcreator.async.DownloadJsonAsyncPostTask;
import com.raveltrips.contentcreator.async.DownloadJsonAsyncPutTask;
import com.raveltrips.contentcreator.async.MultipleImageUploadAsyncTask;
import com.raveltrips.contentcreator.models.CompleteTrip;
import com.raveltrips.contentcreator.models.GPSCoOrdinate;
import com.raveltrips.contentcreator.models.Pindrop;
import com.raveltrips.contentcreator.models.Profile;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddNewGemFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private Object mParam1;
    String filePath = null;
    String fileName = null;
    List filenames = new ArrayList();
    Place place = null;
    EditText gemname,tags,description,lat,lon;
    TextView save,cancel;
    Toolbar tool;
    ImageView gallery,cam;
    Button viewPhotos;
    List imageurls = new ArrayList();
    int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    DataBaseHelper dataBaseHelper;
    GPSCoOrdinate temp=new GPSCoOrdinate();
    List<Bitmap> images;
 /*   Integer[] imageIDs = {
            R.drawable.email, R.drawable.mobile, R.drawable.add_cart,
            R.drawable.email, R.drawable.mobile, R.drawable.add_cart,
            R.drawable.email, R.drawable.mobile, R.drawable.add_cart,
            R.drawable.email, R.drawable.mobile, R.drawable.add_cart,
            R.drawable.email, R.drawable.mobile, R.drawable.add_cart,
            R.drawable.email, R.drawable.mobile, R.drawable.add_cart,
    };*/

    public AddNewGemFragment() {
        // Required empty public constructor
    }


    public static AddNewGemFragment newInstance(Place place) {
        AddNewGemFragment fragment = new AddNewGemFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, (Parcelable) place);
        fragment.setArguments(args);
        return fragment;
    }

    public static AddNewGemFragment newInstance(Pindrop pindrop) {
        AddNewGemFragment fragment = new AddNewGemFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, (Serializable) pindrop);
        // args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getSerializable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("AddNewGemFragment","Inside AddNewGem");
        View rootView = inflater.inflate(R.layout.fragment_add_new_gem, container, false);
        Typeface museo = Typeface.createFromAsset(getContext().getApplicationContext().getAssets(),
                "fonts/Museo700-Regular.ttf");
        Typeface dinlight = Typeface.createFromAsset(getContext().getApplicationContext().getAssets(),
                "fonts/DIN2014Light.ttf");
        Typeface dindemi = Typeface.createFromAsset(getContext().getApplicationContext().getAssets(),
                "fonts/DIN 2014 Demi.ttf");
        Typeface montserrat = Typeface.createFromAsset(getContext().getApplicationContext().getAssets(),
                "fonts/Montserrat-SemiBold.ttf");
        tool= (Toolbar) getActivity().findViewById(R.id.toolbar_save);
        save = (TextView) tool.findViewById(R.id.toolbarsave);
        save.setTypeface(dinlight);
        cancel = (TextView) tool.findViewById(R.id.cancel);
        TextView titleheading = (TextView) tool.findViewById(R.id.titleheading);
        titleheading.setTypeface(dinlight);
        cancel.setTypeface(dinlight);
        lat = (EditText) rootView.findViewById(R.id.latitude) ;
        lat.setTypeface(dinlight);
        lon = (EditText) rootView.findViewById(R.id.longitude) ;
        lon.setTypeface(dinlight);
        gemname = (EditText)rootView.findViewById(R.id.gemname);
        gemname.setTypeface(dinlight);
        tags = (EditText)rootView.findViewById(R.id.tagstext);
        tags.setTypeface(dinlight);
        description = (EditText)rootView.findViewById(R.id.gemdescription);
        description.setTypeface(dinlight);
        gallery = (ImageView)rootView.findViewById(R.id.galleryaddicon);
        cam = (ImageView)rootView.findViewById(R.id.cameraaddicon);
        viewPhotos = (Button)rootView.findViewById(R.id.viewphotos);

        if ( mParam1 instanceof Pindrop){
            if (((Pindrop) mParam1).getGps()!=null && ((Pindrop) mParam1).getTags()!=null){

            lat.setText(((Pindrop) mParam1).getGps().getLattitude());
            lon.setText(((Pindrop) mParam1).getGps().getLongitude());
                for (String s : ((Pindrop) mParam1).getTags()) {
                    if (!s.equals("")) {
                        tags.append(s + ",");
                    }
                }
                }

            gemname.setText(((Pindrop) mParam1).getName());
            description.setText(((Pindrop) mParam1).getDescription());
        }else{
            viewPhotos.setVisibility(View.INVISIBLE);
            place  = getArguments().getParcelable(ARG_PARAM1);
            LatLng latlng = place.getLatLng();
            lat.setText(String.valueOf(latlng.latitude));
            lon.setText(String.valueOf(latlng.longitude));
        }

        viewPhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().
                        replace(R.id.hiddengemscontainer, ViewPhotosFragment.newInstance(mParam1))
                        .addToBackStack(null).commit();
            }
        });



        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{
                    if(PermUtility.checkCameraPermission(getContext())){
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, REQUEST_CAMERA);
                    }else{
                        ToastMessage(" Give Camera Permissions");
                    }
                }catch(Exception ex){
                    Log.d("AddTrip","Exception starting camera activity:"+ex);
                    ToastMessage(" Failed to start Camera");
                }

            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    if(PermUtility.checkReadPermission(getContext())){
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
                    }else{
                        ToastMessage(" Give Gallery Permissions");
                    }


                }catch(Exception ex){
                    Log.d("AddTrip","Exception starting gallery activity:"+ex);
                    ToastMessage(" Failed to Open Gallery");
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    if(getFragmentManager().getBackStackEntryCount()>1){
                        getFragmentManager().popBackStackImmediate();
                    }else {
                        getActivity().finish();
                    }

                }
                catch (Exception e)
                {
                    Log.d("Addnewgemfragment","Error on cancel"+e.getStackTrace());
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(PermUtility.checkReadPermission(getContext())) {
                    if (mParam1 instanceof Pindrop) {
                        int position = -1;
                        Pindrop temp1=null;
                        Bundle extras = getActivity().getIntent().getExtras();
                        if (extras!=null) {
                            position = (int) extras.get("pinposition");
                        }
                        if (position > -1)
                        {
                            temp1=AppContext.pindrops.remove(position);
                        }
                        ((Pindrop) mParam1).setName(gemname.getText().toString());
                        ((Pindrop) mParam1).setDescription(description.getText().toString());
                        temp.setLattitude(lat.getText().toString());
                        temp.setLongitude(lon.getText().toString());
                        ((Pindrop) mParam1).setGps(temp);
                        String[] tag = tags.getText().toString().split(",");
                        final List taglist = new ArrayList<>();
                        //((Pindrop) mParam1).setImageUrls(imageurls);
                        if(tag.length>0) {
                            for (int i = 0; i < tag.length; i++) {
                                tag[i] = tag[i].trim();
                                taglist.add(tag[i]);
                            }
                        }
                        if(taglist.size()>0){
                            ((Pindrop) mParam1).setTags(taglist);
                        }else{
                            ((Pindrop) mParam1).setTags(null);
                        }

                        checkUnfinished((Pindrop)mParam1);
                        AppContext.pindrops.add((Pindrop) mParam1);
                        AppContext.completeTrip.setPindrops(AppContext.pindrops);
                        if (filePath != null) {
                            uploadImagesToServer((Pindrop) mParam1);

                        }
                        else if(((Pindrop) mParam1).getImageUrls().size()>0)
                        {
                            if (AppContext.CALL)
                                CreateTripServerCall();
                            else
                                UpdateTripServerCall();
                        }
                        else
                        {
                            AppContext.pindrops.remove((Pindrop) mParam1);
                            if (temp1!=null)
                                AppContext.pindrops.add(temp1);
                            ToastMessage("Please Upload an Image");
                        }
                    } else {
                        Pindrop pindrop = new Pindrop();
                        pindrop.setName(gemname.getText().toString());
                        pindrop.setDescription(description.getText().toString());
                        temp.setLattitude(lat.getText().toString());
                        temp.setLongitude(lon.getText().toString());
                        pindrop.setGps(temp);
                        String[] tag = tags.getText().toString().split(",");
                        final List taglist = new ArrayList<>();

                        for (int i = 0; i < tag.length; i++) {
                            taglist.add(tag[i]);
                        }
                        pindrop.setTags(taglist);
                        checkUnfinished(pindrop);
                        AppContext.pindrops.add(pindrop);
                        pindrop.setImageUrls(imageurls);
                        AppContext.completeTrip.setPindrops(AppContext.pindrops);
                        if (filePath != null) {
                            uploadImagesToServer(pindrop);

                        }
                        else if(pindrop.getImageUrls().size()>0)
                        {
                            if (AppContext.CALL)
                                CreateTripServerCall();
                            else
                                UpdateTripServerCall();
                        }
                        else
                        {
                            AppContext.pindrops.remove(pindrop);
                            ToastMessage("Please Upload an Image");
                        }
                    }

                }else{
                    ToastMessage("Give Storage permissions");
                }
            }

        });


        return rootView;
    }



    private void checkUnfinished(Pindrop pindrops) {
        if (pindrops.getName() != null && pindrops.getGps() != null && pindrops.getDescription() != null
                && pindrops.getTags() != null && pindrops.getImageUrls() != null) {
            pindrops.setreadyForReview(true);
        }
    }
    private void resetFields() {
        getFragmentManager().popBackStackImmediate();
        getActivity().finish();
    }

//    private void uploadImagesToServer(final Pindrop pindrop) {
//
//        AsyncUploadImage imageUpload = new AsyncUploadImage(new AsyncComplete() {
//            @Override
//            public void OnJsonAsyncCompleted(List<String> json) {
//                try {
//                    if (json != null && json.size() > 0) {
//                        JSONObject responseModel = new JSONObject(json.get(0));
//                        String message = responseModel.getString("message");
//                        String status = responseModel.getString("status");
//                        if (status != null && status.equalsIgnoreCase("200")) {
//                            pindrop.setImageUrls(Arrays.asList(AppContext.BASE_IMAGE_URL+fileName));
//                            if (AppContext.CALL)
//                                CreateTripServerCall();
//                            else
//                                UpdateTripServerCall();
//                        }
//                        else {
//                            ToastMessage("Failed to upload image to server!! Try again later!");
//                            Log.d("AddNewGemFragment", "Message from server"+message);
//                        }
//                        Log.d("AddNewGemFragment", "image uploaded..filename"+fileName);
//                    }
//                } catch (Exception ex) {
//                    ToastMessage("Failed to upload image to server!! Try again later!");
//                    Log.d("AddNewGemFragment", "Exception converting search trips from json:" + ex);
//                }
//            }
//        });
//        imageUpload.setFilePath(filePath);
//        imageUpload.execute(AppContext.IMAGE_UPLOAD_URL);
//    }

    private void uploadImagesToServer(final Pindrop pindrop) {
                MultipleImageUploadAsyncTask imageUpload = new MultipleImageUploadAsyncTask(new AsyncComplete() {
            @Override
            public void OnJsonAsyncCompleted(List<String> json) {
                try {
                    if (json != null && json.size() > 0) {
                        JSONObject responseModel = new JSONObject(json.get(0));
                        String message = responseModel.getString("message");
                        String status = responseModel.getString("status");
                        if (status != null && status.equalsIgnoreCase("200")) {
                            List temp = new ArrayList<>();
                            if (pindrop.getImageUrls().size()>0)
                            {
                                temp = pindrop.getImageUrls();
                            }
                            for (int i=0;i<filenames.size();i++)
                            {
                                temp.add(AppContext.BASE_IMAGE_URL+filenames.get(i));
                            }
                            pindrop.setImageUrls(temp);
                           // pindrop.setImageUrls(Arrays.asList(AppContext.BASE_IMAGE_URL+fileName));
                            if (AppContext.CALL)
                                CreateTripServerCall();
                            else
                                UpdateTripServerCall();
                        }
                        else {
                            ToastMessage("Failed to upload image to server!! Try again later!");
                            Log.d("AddNewGemFragment", "Message from server"+message);
                        }
                        Log.d("AddNewGemFragment", "image uploaded..filename"+fileName);
                    }
                } catch (Exception ex) {
                    ToastMessage("Failed to upload image to server!! Try again later!");
                    Log.d("AddNewGemFragment", "Exception converting search trips from json:" + ex);
                }
            }
        });
        imageUpload.setFilePath(imageurls);
        imageUpload.execute(AppContext.IMAGE_UPLOAD_URL);
    }

    private void UpdateTripServerCall() {

        DownloadJsonAsyncPutTask task = new DownloadJsonAsyncPutTask();
         Gson gson = new Gson();
        task.setBodyContent(gson.toJson(AppContext.completeTrip).toString());
        Log.d("AddNewGemFragment", "profile json"+gson.toJson(AppContext.completeTrip).toString());
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
                            CompleteTrip ctrip = gson.fromJson(profObj.toString(), CompleteTrip.class);
                            if(ctrip!=null){
                                Log.d("AddNewGemFragment", "updated profile:"+gson.toJson(ctrip).toString());
                                ToastMessage("Trip updated successfully!");
                                AppContext.completeTrip.copyFromTrip(ctrip);
                                if(AppContext.CALL){
                                    AppContext.CALL = false;
                                }
                                UpdateProfileServerCall();
                            }
                        }else{
                            ToastMessage("Failed to update Profile! try again later..");
                            Log.d("AddNewGemFragment", "Failed to update");
                        }
                    }
                }
                catch (Exception ex) {
                    Log.d("AddNewGemFragment", "Exception converting profile from json:" + ex);
                }
            }
        });
        task.execute(AppContext.UPDATE_COMPLETE_TRIP_URL);
    }



    private void CreateTripServerCall() {

        DownloadJsonAsyncPostTask task = new DownloadJsonAsyncPostTask();
        Gson gson = new Gson();
        task.setBodyContent(gson.toJson(AppContext.completeTrip).toString());
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
                            CompleteTrip completeTrip = gson.fromJson(profObj.toString(), CompleteTrip.class);
                            if(completeTrip!=null){
                                Log.d("AddNewGemFragment", "updated profile:"+gson.toJson(completeTrip).toString());
                                ToastMessage("Trip created/updated successfully!");
                                AppContext.completeTrip.copyFromTrip(completeTrip);
                                boolean flag = MainActivity.dataBaseHelper.insertTripNameAndID("MYTRIPS",completeTrip.getCreatorId(),completeTrip.getId(),completeTrip.getName());
                                if(flag){
                                    Log.d("AddNewGem","Inserted into local database");
                                }
                                else{
                                    Log.d("AddNewGem","Could not insert into local database");
                                }
                                List<String> temp = null;
                                if (AppContext.profile.getTripsCompletedIds() != null)
                                    temp = AppContext.profile.getTripsCompletedIds();
                                else
                                    temp = new ArrayList<>();
                                temp.add(completeTrip.getId());
                                AppContext.profile.setTripsCompletedIds(temp);
                                UpdateProfileServerCall();


                                if(AppContext.CALL){
                                    AppContext.CALL = false;
                                }

                            }
                        }else{
                            ToastMessage("Failed to update Trip! try again later..");
                            Log.d("AddNewGemFragment", "Failed to update");
                        }
                    }
                }
                catch (Exception ex) {
                    Log.d("AddNewGemFragment", "Exception converting trip from json:" + ex);
                }
            }
        });
        task.execute(AppContext.UPDATE_COMPLETE_TRIP_URL);
    }
    private void ToastMessage(String s) {
        Toast.makeText(getContext(),s, Toast.LENGTH_SHORT).show();
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
        super.onActivityResult(requestCode, resultCode, data);
      /*  adapter = new ViewPhotosAdapter(getContext(),images);
        gridView.setAdapter(adapter);*/
        }

    private void onCaptureImageResult(Intent data) {

        try{
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            createFileFromBitmap(thumbnail);
        }catch(Exception ex){
            Log.d("AddNewGemFragment", "error capturing from camera"+ex);
        }
    }

    private void onSelectFromGalleryResult(Intent data) {
        try {
            Bitmap bm = null;
            if (data != null) {
                try {
                    bm = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), data.getData());
                    createFileFromBitmap(bm);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception ex) {
            Log.d("AddNewGemFragment", "error capturing from gallery" + ex);
        }
    }

    private void createFileFromBitmap(Bitmap bm) {
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
            Log.d("AddNewGemFragment", "onCaptureImageResult, writing image to disk..");

            File destination = new File(Environment.getExternalStorageDirectory(),
                    System.currentTimeMillis() + ".jpg");

            FileOutputStream fo;
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
            filePath = destination.getPath();
            //fileName = destination.getName();
            filenames.add(destination.getName());
            Log.d("AddNewGemFragment", "Image file created at" + filePath);
            ToastMessage("Photos Added Successfully");
            imageurls.add(filePath);

        } catch (Exception ex) {
            Log.d("AddNewGemFragment", "runtime error caught:" + ex);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PermUtility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Toast.makeText(this, "Read storage permission granted", Toast.LENGTH_LONG).show();
                PermUtility.checkCameraPermission(getContext());
            } else {
                Toast.makeText(getContext(), "Read storage denied", Toast.LENGTH_LONG).show();
            }
        }

        if (requestCode == PermUtility.MY_PERMISSIONS_REQUEST_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                PermUtility.checkWritePermission(getContext());
                //Toast.makeText(this, "Permissions granted!! Try again now.", Toast.LENGTH_LONG).show();
                //handle image picker.

            } else {
                Toast.makeText(getContext(), "Camera denied", Toast.LENGTH_LONG).show();
            }
        }

        if (requestCode == PermUtility.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "Write storage permission granted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getContext(), "Write storage denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            OnFragmentInteractionListener mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    private void UpdateProfileServerCall() {
        DownloadJsonAsyncPutTask task = new DownloadJsonAsyncPutTask();
        Gson gson = new Gson();
        task.setBodyContent(gson.toJson(AppContext.profile).toString());
        Log.d("ProfileActivity", "profile json"+gson.toJson(AppContext.profile).toString());
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
                                Log.d("AddNewGem", "updated profile:"+gson.toJson(sProfile).toString());
                                Log.d("AddNewGem","Profile updated successfully!");
                                AppContext.profile.copyFromProfile(sProfile);
                               // getFragmentManager().popBackStackImmediate();
                                    getActivity().finish();
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}



