package com.mobiversal.practica.life;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class ChangeGeoActivity extends AppCompatActivity {
    private DatabaseReference mUserDatabase;
    private FirebaseUser mCurentUser;
    private EditText currentLimit;
    private EditText newLimit;
    private Button changeLimit;
    private static final double METERS= 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_geo);

        mCurentUser= FirebaseAuth.getInstance().getCurrentUser();
        final String curent_id = mCurentUser.getUid();
        mUserDatabase= FirebaseDatabase.getInstance().getReference().child("users").child(curent_id);
        newLimit = (EditText) findViewById(R.id.Your_limit_btn);
        changeLimit = (Button) findViewById(R.id.Change_Geolimit_btn);
        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                currentLimit = (EditText) findViewById(R.id.Current_limit_btn);
                currentLimit.setText(String.valueOf(Double.valueOf(user.getGeoLimit()) / METERS) + " (KM)");
                currentLimit.setEnabled(false);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        changeLimit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String newValue = newLimit.getText().toString() +"000";
                Map updateHasMap= new HashMap();
                updateHasMap.put("GeoLimit",newValue);

                mUserDatabase.updateChildren(updateHasMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(ChangeGeoActivity.this,MainActivity.class);
                            startActivity(intent);
                        }

                    }
                });
            }
        });
    }
}
