package com.mobiversal.practica.life;

import android.app.ProgressDialog;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

public class ProfileActivity extends AppCompatActivity {
    private ImageView mProfileImage;
    private TextView mProfileName, mProfileFriendsCount;
    private Button mProfileSendReqBtn;

    private  DatabaseReference mFriendsReqDatabase;
    private DatabaseReference mUserDatabase;
    private ProgressDialog mProgressDialog;

    private FirebaseUser mCurrent_user;
    private String mCurrent_state;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        final String user_id =getIntent().getStringExtra("user_id");

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Userss").child(user_id);
        mFriendsReqDatabase =FirebaseDatabase.getInstance().getReference().child("Friends_req");
        mCurrent_user =FirebaseAuth.getInstance().getCurrentUser();


        mProfileImage = (ImageView) findViewById(R.id.profile_image);
        mProfileName = (TextView) findViewById(R.id.profile_displayName);
        mProfileFriendsCount=(TextView) findViewById(R.id.profile_totalFriends);
        mProfileSendReqBtn = (Button) findViewById(R.id.profile_send_req_btn);

        mCurrent_state ="not friends";

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Loading");
        mProgressDialog.setMessage("Please wait");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();

        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String display_name = dataSnapshot.child("name").getValue().toString();
                String image =dataSnapshot.child("image").getValue().toString();

                mProfileName.setText(display_name);
                Picasso.with(ProfileActivity.this).load(image).placeholder(R.drawable.default_user).into(mProfileImage);

                mProgressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mProfileSendReqBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCurrent_state.equals("not friends")){

                    mFriendsReqDatabase.child(mCurrent_user.getUid()).child(user_id).child("request_type").setValue("send").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                mFriendsReqDatabase.child(user_id).child(mCurrent_user.getUid()).child("request_type").setValue("received").addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(ProfileActivity.this,"Request Send Successfull",Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                            else{
                                Toast.makeText(ProfileActivity.this,"Faild Sending",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }
}
