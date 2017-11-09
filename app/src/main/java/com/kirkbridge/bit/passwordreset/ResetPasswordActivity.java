package com.kirkbridge.bit.passwordreset;

import android.app.ProgressDialog;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import static java.lang.Thread.sleep;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText etEmail;
    private Button btnResetPassword;
    private FirebaseAuth auth;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        auth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);

        etEmail = (EditText) findViewById(R.id.etEmail);
        btnResetPassword = (Button) findViewById(R.id.btnResetPassword);
        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean isValidEmail = true;
                String emailAddress = etEmail.getText().toString().trim();

                if(emailAddress.isEmpty() || emailAddress.equals(""))
                {
                    isValidEmail = false;
                    Toast.makeText(ResetPasswordActivity.this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
                }
                if(isValidEmail)
                {
                    progressDialog.setMessage("Sending reset Password email");
                    progressDialog.show();

                    auth.sendPasswordResetEmail(emailAddress)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("RESET_EMAIL", "Email sent.");
                                        Toast.makeText(ResetPasswordActivity.this, "Email Sent", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();

                                        // if successful return back to other activty after 3 seconds
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                finish();
                                            }
                                        }, 3000);


                                    }
                                    else
                                    {
                                        Toast.makeText(ResetPasswordActivity.this, "Email address could not be found", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }
                                }
                            });
                }
            }
        });

    }
}