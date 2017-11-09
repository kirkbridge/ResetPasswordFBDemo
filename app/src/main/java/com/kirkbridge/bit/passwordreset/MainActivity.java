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

public class MainActivity extends AppCompatActivity {

    private Button btnLogin, btnNewAccount;
    private EditText etEmail, etPassword;
    private TextView tvForgotPassword;
    private ProgressDialog progressDialog;
    private FirebaseAuth fbAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDialog = new ProgressDialog(this);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnNewAccount = (Button) findViewById(R.id.btnNewAccount);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        tvForgotPassword = (TextView) findViewById(R.id.tvForgotPassword);

        fbAuth = FirebaseAuth.getInstance();

        // check to see if user is logged in
        if(fbAuth.getCurrentUser()!=null)
        {
            startActivity(new Intent(MainActivity.this, ProfileActivity.class));
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        btnNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, NewAccountActivity.class));
            }
        });

        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ResetPasswordActivity.class));
            }
        });
    }

    private void login()
    {
        Boolean isValidLogin = true;
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        progressDialog.setMessage("Logging In User...");
        progressDialog.show();

        if (email.isEmpty() || email.equals("")) {
            isValidLogin = false;
            Toast.makeText(this, "Please enter a valid email.", Toast.LENGTH_SHORT).show();
        }
        else if (password.isEmpty() || password.equals("") || password.length() < 6) {
            isValidLogin = false;
            Toast.makeText(this, "Please enter a valid password.", Toast.LENGTH_SHORT).show();
        }

        if (isValidLogin) {
            fbAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())
                            {
                                // login successful
                                Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                startActivity(new Intent(MainActivity.this, ProfileActivity.class));

                            }
                            else
                            {
                                Toast.makeText(MainActivity.this, "Error: Invalid Login.", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        }
                    });
        }
    }
}

