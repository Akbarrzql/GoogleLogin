package com.example.ptsapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

public class ProfilActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private ImageView profilImage;
    private TextView profilName;
    private TextView profilEmail;
    private TextView id;
    private Button signOutBTN;

    private GoogleApiClient googleSignInClient;
    private GoogleSignInOptions gso;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        profilImage = findViewById(R.id.profileImage);
        profilName = findViewById(R.id.name);
        profilEmail = findViewById(R.id.email);
        id = findViewById(R.id.userId);
        signOutBTN = findViewById(R.id.logoutBtn);

//        //facebook logout
//        AccessToken accessToken = AccessToken.getCurrentAccessToken();
//
//        GraphRequest request = GraphRequest.newMeRequest(
//                accessToken,
//                new GraphRequest.GraphJSONObjectCallback() {
//                    @Override
//                    public void onCompleted(
//                            JSONObject object,
//                            GraphResponse response) {
//                        try {
//                           String fullName =  object.getString("name");
//                           String URL = object.getJSONObject("picture").getJSONObject("data").getString("url");
//                           profilName.setText(fullName);
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//        Bundle parameters = new Bundle();
//        parameters.putString("fields", "id,name,link,picture.type(large)");
//        request.setParameters(parameters);
//        request.executeAsync();

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleSignInClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
        signOutBTN.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Auth.GoogleSignInApi.signOut(googleSignInClient).setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        if(status.isSuccess())
                            gotoMainActivity();
                            else
                            Toast.makeText(ProfilActivity.this, "Logout Gagal", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }

    private void gotoMainActivity() {
        startActivity(new Intent(ProfilActivity.this, LoginActivity.class));
        finish();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void handleSignInResult(GoogleSignInResult result) {
        if(result.isSuccess()){
            GoogleSignInAccount account = result.getSignInAccount();
            profilName.setText(account.getDisplayName());
            profilEmail.setText(account.getEmail());
            id.setText(account.getId());
            String personPhotoUrl = account.getPhotoUrl().toString();
            Glide.with(this).load(personPhotoUrl).into(profilImage);
        } else {
            gotoMainActivity();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(googleSignInClient);

        if(opr.isDone()){
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        }else {
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    handleSignInResult(googleSignInResult);
                }
            });
    }
}
}