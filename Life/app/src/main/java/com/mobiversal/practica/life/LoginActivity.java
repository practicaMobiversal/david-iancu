package com.mobiversal.practica.life;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText mLoginEmail;
    private EditText mLoginPassword;
    private FirebaseAuth mAuth;
    public static String userEmail;
    public static int Device_Width;
    private Button mLogin_btn;
    private ProgressDialog mLoginProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        mLoginProgress = new ProgressDialog(this);

        mLoginEmail = (EditText) findViewById(R.id.login_email);
        mLoginPassword = (EditText) findViewById(R.id.login_password);
        mLogin_btn = (Button) findViewById(R.id.login_btn);

        mLogin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email =mLoginEmail.getText().toString();
                String password = mLoginPassword.getText().toString();
                userEmail = email;

                if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){

                    mLoginProgress.setTitle("Loging In");
                    mLoginProgress.setMessage("Please wait");
                    mLoginProgress.setCanceledOnTouchOutside(false);
                    mLoginProgress.show();
                    loginUser(email,password);
                }
            }
        });
        DisplayMetrics metrics = getApplicationContext().getResources().getDisplayMetrics();
        Device_Width = metrics.widthPixels;

    }

    private void loginUser(final String email, String password) {
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    userEmail = email;
                    RegisterActivity.loginStatus = true;
                    mLoginProgress.dismiss();
                    Intent mainIntent = new Intent(LoginActivity.this,MainActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(mainIntent);
                    finish();
                }
                else{
                    mLoginProgress.hide();
                    Log.e("RegisterActivity", "Register Error", task.getException());
                    Toast.makeText(LoginActivity.this," Error",Toast.LENGTH_LONG).show();
                }
            }
        });



    }
}
