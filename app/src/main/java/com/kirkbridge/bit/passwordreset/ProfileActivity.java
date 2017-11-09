package com.kirkbridge.bit.passwordreset;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import org.w3c.dom.Text;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvUserEmail, tvUserName, tvImageUrl;
    private EditText etDisplayName, etPhotoUrl, etPassword;
    private Button btnLogOut, btnUpdate;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        tvUserEmail = (TextView) findViewById(R.id.tvUserEmail);
        tvUserName = (TextView) findViewById(R.id.tvUserName);
        tvImageUrl = (TextView) findViewById(R.id.tvImageUrl);

        etDisplayName = (EditText) findViewById(R.id.etDisplayName);
        etPhotoUrl = (EditText) findViewById(R.id.etPhotoUrl);
        //etPassword = (EditText) findViewById(R.id.etPassword);

        btnLogOut = (Button) findViewById(R.id.btnLogOut);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        if(user != null)
        {
            tvUserEmail.setText("Logged in as: " + user.getEmail());
        }

        if(user.getDisplayName()!=null)
        {
            tvUserName.setText("User Name is: " + user.getDisplayName());
        }
        else
        {
            tvUserName.setText("No User Name set.");
        }

        if(user.getPhotoUrl() != null)
        {
            tvImageUrl.setText("Profile Image URL is: " + user.getPhotoUrl());
        }
        else
        {
            tvImageUrl.setText("No profile picture chosen");
        }

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile();
            }
        });

    }

    private void updateProfile()
    {
        String displayName = etDisplayName.getText().toString().trim();
        String photoUrl = etPhotoUrl.getText().toString().trim() ;
        //String password = etPassword.getText().toString().trim();

        // Lets update the user's profile
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(displayName)
                .setPhotoUri(Uri.parse(photoUrl))
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("UPDATE_PROFILE", "User profile updated.");
                            Toast.makeText(ProfileActivity.this, "Profile Updated, Please login again", Toast.LENGTH_SHORT).show();

                            // if successful return back to other activty after 3 seconds
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    btnLogOut.performClick();
                                }
                            }, 3000);
                        }
                    }
                });

    }
}
