package com.kyrostechnologies.thirunavukkarasu.pixels.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kyrostechnologies.thirunavukkarasu.pixels.R;
import com.kyrostechnologies.thirunavukkarasu.pixels.modelclass.User;
import com.kyrostechnologies.thirunavukkarasu.pixels.storage.SessionManager;
import com.kyrostechnologies.thirunavukkarasu.pixels.storage.Storage;

public class Login_Activity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private GoogleApiClient mGoogleApiClient;
    private static final String TAG=Login_Activity.class.getSimpleName();
    private int RC_SIGN_IN=2;
    private Storage storage;
    private SessionManager sessionManager;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private String userid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_login_);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .requestScopes(new Scope(Scopes.PLUS_LOGIN))
                .requestScopes(new Scope(Scopes.PLUS_ME))
                .requestId()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        storage=Storage.getInstance(getApplicationContext());
        sessionManager=new SessionManager(getApplicationContext());
        SignInButton signInButton = (SignInButton) findViewById(R.id.google_sign_in);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setScopes(gso.getScopeArray());
        mFirebaseInstance=FirebaseDatabase.getInstance();
        mFirebaseDatabase=mFirebaseInstance.getReference("users");
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();

            }
        });
    }


    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        Log.d(TAG, "handle code:" + result.getStatus().toString());
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            String displayName=acct.getDisplayName();
            Uri profilePicture=acct.getPhotoUrl();
            if(profilePicture!=null){
                Log.d("UserProfilePicture",profilePicture.toString());

            }
            String email=acct.getEmail();
            String uniqueId=acct.getId();
            if(displayName!=null){
                storage.putDisplayName(displayName);
            }if(email!=null){
                storage.putEmailId(email);
                sessionManager.createLoginSession(email,uniqueId);
            }if(uniqueId!=null){
                storage.putUserId(uniqueId);
            }if(profilePicture!=null){
                String pic=profilePicture.toString();
                storage.putUserPicture(pic);
            }
            String picture="pic";
            if(profilePicture!=null){
                picture=profilePicture.toString();

            }
            User user=new User(email,displayName,uniqueId,picture);
             userid=mFirebaseDatabase.push().getKey();
            mFirebaseDatabase.child(userid).setValue(user);
            addUserChangeListener();
            Intent i=new Intent(Login_Activity.this,MainActivity.class);
            startActivity(i);
            Login_Activity.this.finish();
        } else {
            Toast.makeText(getApplicationContext(),"Something went wrong!",Toast.LENGTH_SHORT).show();
        }
    }

    private void addUserChangeListener() {
        mFirebaseDatabase.child(userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user=dataSnapshot.getValue(User.class);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Failed to read user", databaseError.toException());

            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
