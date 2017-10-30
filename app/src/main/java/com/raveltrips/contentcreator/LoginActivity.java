package com.raveltrips.contentcreator;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;


public class LoginActivity extends AppCompatActivity {


    EditText userName,Email;
    EditText passwordET;
    TextView nametext,mail,pass;
    TextView login;
    Button signUp;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    private static String TAG = "MAIN";
    private static final int RC_SIGN_IN = 123;
    private static final String USER_CREATION_SUCCESS =  "Successfully created user";
    private static final String USER_CREATION_ERROR =  "User creation error";
    private static final String EMAIL_INVALID =  "email is invalid :";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user!=null){
                    Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);  //Replace MainActivity.class with your launcher class from previous assignments
                    LoginActivity.this.startActivity(myIntent);
                }else{

                }

            }
        };

        Typeface museo = Typeface.createFromAsset(getApplicationContext().getApplicationContext().getAssets(),
                "fonts/Museo700-Regular.ttf");
        Typeface dinlight = Typeface.createFromAsset(getApplicationContext().getApplicationContext().getAssets(),
                "fonts/DIN2014Light.ttf");
        Typeface dindemi = Typeface.createFromAsset(getApplicationContext().getApplicationContext().getAssets(),
                "fonts/DIN 2014 Demi.ttf");
        Typeface montserrat = Typeface.createFromAsset(getApplicationContext().getApplicationContext().getAssets(),
                "fonts/Montserrat-SemiBold.ttf");

        nametext = (TextView) findViewById(R.id.Nametext);
        mail = (TextView) findViewById(R.id.emailtext);
        pass = (TextView) findViewById(R.id.passwordtext);


        nametext.setTypeface(dinlight);
        mail.setTypeface(dinlight);
        pass.setTypeface(dinlight);


        userName = (EditText)findViewById(R.id.username);
        Email = (EditText)findViewById(R.id.email);
        passwordET = (EditText)findViewById(R.id.password);
        signUp = (Button)findViewById(R.id.signup);

        userName.setTypeface(dinlight);
        Email.setTypeface(dinlight);
        passwordET.setTypeface(dinlight);

        login = (TextView) findViewById(R.id.login);
        login.setTypeface(dinlight);
        login.setClickable(true);
        login.setMovementMethod(LinkMovementMethod.getInstance());
        signUp.setTypeface(dinlight);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setIsSmartLockEnabled(false)
                                .setProviders(Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                        new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()
                                ))
                                .build(),
                        RC_SIGN_IN);
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              createUser();
            }
        });


        try{
            if(AppContext.profile!=null){
                AppContext.profile = null;
                AppContext.completedTrips.clear();
                AppContext.wishlistTrips.clear();
            }
        }catch (Exception ex){ex.printStackTrace();}

    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("LoginActivity",String.valueOf(resultCode));
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            // Successfully signed in
            if (resultCode == ResultCodes.OK) {
                Intent myIntent = new Intent(LoginActivity.this, MainActivity.class); //Replace MainActivity.class with your launcher class from previous assignments
                LoginActivity.this.startActivity(myIntent);

                return;
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    showSnackbar("Sign in cancelled");
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                    showSnackbar("No network connnection");
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    showSnackbar("Error occured while signing in");
                    return;
                }
            }

            showSnackbar("Error occured while signing in");
        }
    }

    public void showSnackbar(String s){
        Snackbar snackbar = Snackbar.make(Email,s,Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onBackPressed() {
        //Exit the application
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }

    // Validate email address for new accounts.
    private boolean isEmailValid(String email) {
        boolean isGoodEmail = (email != null && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches());
        if (!isGoodEmail) {
            Email.setError(EMAIL_INVALID + email);
            return false;
        }
        return true;
    }

    // create a new user in Firebase
    public void createUser() {
        if(userName.getText() == null ||  !isEmailValid(Email.getText().toString())) {
            showSnackbar("Email not valid!!");
            return;
        }
        if(passwordET.getText()==null || passwordET.getText().toString().isEmpty()){
            showSnackbar("Password not valid!!");
            return;
        }

        mAuth.createUserWithEmailAndPassword(Email.getText().toString(),passwordET.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Snackbar snackbar = Snackbar.make(Email, USER_CREATION_SUCCESS, Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }else{
                    Snackbar snackbar = Snackbar.make(Email, USER_CREATION_ERROR, Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
            }
        });
    }
}
