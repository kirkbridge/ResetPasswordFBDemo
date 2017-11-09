package com.kirkbridge.bit.passwordreset;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class NewAccountActivity extends AppCompatActivity {

    private Button btnCreateAccount;
    private EditText etEmail, etPassword;
    private TextView tvBackToLogin;
    private ProgressDialog progressDialog;
    private FirebaseAuth fbAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);

        progressDialog = new ProgressDialog(this);

        btnCreateAccount = (Button) findViewById(R.id.btnCreateAccount);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        tvBackToLogin = (TextView) findViewById(R.id.tvBackToLogin);

        fbAuth = FirebaseAuth.getInstance();

        if(fbAuth.getCurrentUser() != null)
        {
            startActivity( new Intent(NewAccountActivity.this, ProfileActivity.class));
        }

        tvBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    private void registerUser()
    {
        Boolean isValidRegistration = true;
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || email.equals("")) {
            isValidRegistration = false;
            Toast.makeText(this, "Please enter a valid email.", Toast.LENGTH_SHORT).show();
        }
        else if (password.isEmpty() || password.equals("") || password.length() < 6) {
            isValidRegistration = false;
            Toast.makeText(this, "Please enter a valid password.", Toast.LENGTH_SHORT).show();
        }

        if (isValidRegistration) {
            progressDialog.setMessage("Registering User...");
            progressDialog.show();

            fbAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(NewAccountActivity.this, "User Registered..", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                            else {
                                Toast.makeText(NewAccountActivity.this, "Registration failed..", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }

                        }
                    });
        }

    }
}
