package com.mobiversal.practica.life;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.util.DisplayMetrics;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.internal.kx;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {
    private EditText mDisplayName;
    private EditText mEmail;
    private EditText mPassword;
    private Button mCreateBtn;
    private FirebaseAuth mAuth;
    private ProgressDialog mRegProgress;
    public static boolean loginStatus = false;
    private FusedLocationProviderClient mFusedLocationClient;
    private static final String DEFAULT_GEO_LIMIT = "10000";
    double latitude;
    double longitude;

private DatabaseReference mRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                });

        mRegProgress =new ProgressDialog(this);



        mAuth = FirebaseAuth.getInstance();

        mDisplayName = (EditText) findViewById(R.id.reg_display_name);
        mEmail = (EditText) findViewById(R.id.reg_email);
        mPassword =(EditText) findViewById(R.id.reg_password);
        mCreateBtn = (Button) findViewById(R.id.reg_create_btn);

        mCreateBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String display_name =mDisplayName.getText().toString();
                String email =mEmail.getText().toString();
                String password =mPassword.getText().toString();

                if(!TextUtils.isEmpty(display_name) || !TextUtils.isEmpty(email) || !TextUtils.isEmpty(password)) {

                    mRegProgress.setTitle("Registering User");
                    mRegProgress.setMessage("Please wait");
                    mRegProgress.setCanceledOnTouchOutside(false);
                    mRegProgress.show();
                    register_user(display_name, email, password);
                }

            }
        });
        DisplayMetrics metrics = getApplicationContext().getResources().getDisplayMetrics();
        LoginActivity.Device_Width = metrics.widthPixels;
    }

    private void register_user(final String display_name, final String email, String password) {
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    LoginActivity.userEmail = email;
                    loginStatus = true;
//                    FirebaseUser user = mAuth.getCurrentUser();
//                    String UserID=user.getEmail().replace("@","").replace(".","");
//                    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
//
//                    DatabaseReference ref1= mRootRef.child("users").child(UserID);
//
//                    ref1.child("Name").setValue(display_name.trim());
//                    ref1.child("Image_Url").setValue("Null");
//                    ref1.child("Email").setValue(user.getEmail());
//                    loginStatus = true;
//                    mRegProgress.dismiss();
//                    Intent mainIntent = new Intent(RegisterActivity.this,MainActivity.class);
//                    startActivity(mainIntent);
//                    finish();

                    FirebaseUser curent_user=FirebaseAuth.getInstance().getCurrentUser();
                    String uid=curent_user.getUid();
                    mRef= FirebaseDatabase.getInstance().getReference().child("users").child(uid);
                    HashMap<String,String> uMap=new HashMap<String, String>();
                    uMap.put("name",display_name);
                    uMap.put("image","default");
                    uMap.put("Email",email);
                    uMap.put("Latitude", String.valueOf(latitude));
                    uMap.put("Longitude", String.valueOf(longitude));
                    uMap.put("GeoLimit", DEFAULT_GEO_LIMIT);

                    mRef.setValue(uMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                           if(task.isSuccessful()){

                               mRegProgress.dismiss();
                               Intent mainIntent = new Intent(RegisterActivity.this,MainActivity.class);
                               startActivity(mainIntent);
                               finish();


                           }
                        }
                    });




                }else{
                    mRegProgress.hide();
                    Log.e("RegisterActivity", "Register Error", task.getException());
                    Toast.makeText(RegisterActivity.this," Error",Toast.LENGTH_LONG).show();
                }




            }
        });
    }


}
