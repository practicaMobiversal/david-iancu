package com.mobiversal.practica.life;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {
    private ImageView mProfileImage;
    private TextView mProfileName, mProfileFriendsCount;
    private Button mProfileSendReqBtn, mDeclineBtn;

    private DatabaseReference mFriendsReqDatabase;
    private DatabaseReference mFriendDatabase;
    private DatabaseReference mUserDatabase;

    private DatabaseReference mNotificationDatabase;
    private ProgressDialog mProgressDialog;

    private FirebaseUser mCurrent_user;
    private String mCurrent_state;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        final String user_id =getIntent().getStringExtra("user_id");

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(user_id);
        mFriendsReqDatabase =FirebaseDatabase.getInstance().getReference().child("Friends_req");
        mFriendDatabase=FirebaseDatabase.getInstance().getReference().child("Friends");
        mCurrent_user =FirebaseAuth.getInstance().getCurrentUser();
        mNotificationDatabase =FirebaseDatabase.getInstance().getReference().child("notifications");


        mProfileImage = (ImageView) findViewById(R.id.profile_image);
        mProfileName = (TextView) findViewById(R.id.profile_displayName);
        mProfileFriendsCount=(TextView) findViewById(R.id.profile_totalFriends);
        mProfileSendReqBtn = (Button) findViewById(R.id.profile_send_req_btn);
        mDeclineBtn = (Button) findViewById(R.id.profile_decline_btn);

        mCurrent_state ="not_friends";


        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Loading");
        mProgressDialog.setMessage("Please wait");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();

        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

String name=dataSnapshot.child("name").getValue().toString();
                String image=dataSnapshot.child("tumbimg").getValue().toString();

                mProfileName.setText(name);
                Picasso.with(ProfileActivity.this).load(image).placeholder(R.drawable.default_user).into(mProfileImage);

                //-------friend list

                mFriendsReqDatabase.child((mCurrent_user.getUid())).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.hasChild(user_id)){

                        String req_type=dataSnapshot.child(user_id).child("request_type").getValue().toString();
                        if(req_type.equals("recived")){


                            mCurrent_state="req_recived";
                            mProfileSendReqBtn.setText("Accept Friend Request");
                            mDeclineBtn.setVisibility(View.VISIBLE);
                            mDeclineBtn.setEnabled(true);

                        } else if(req_type.equals("sent")){
                            mCurrent_state="req_sent";
                            mProfileSendReqBtn.setText("Cancel Friend Request");

                            mDeclineBtn.setVisibility(View.INVISIBLE);
                            mDeclineBtn.setEnabled(false);

                        }
                        mProgressDialog.dismiss();

                    }
                    else {


                        mFriendDatabase.child((mCurrent_user.getUid())).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.hasChild(user_id)){

                                    mCurrent_state="friends";
                                    mProfileSendReqBtn.setText("Unfriend");
                                    mDeclineBtn.setVisibility(View.INVISIBLE);
                                    mDeclineBtn.setEnabled(false);

                                }
                                mProgressDialog.dismiss();

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                mProgressDialog.dismiss();
                            }

                        });
                    }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });





            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mProfileSendReqBtn.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                mProfileSendReqBtn.setEnabled(false);
                if(mCurrent_state.equals("not_friends")){

                    mFriendsReqDatabase.child(mCurrent_user.getUid()).child(user_id).child("request_type").setValue("send").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                mFriendsReqDatabase.child(user_id).child(mCurrent_user.getUid()).child("request_type").setValue("recived").addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                    mProfileSendReqBtn.setEnabled(true);

                                        HashMap<String,String>notificationData =new HashMap<>();
                                        notificationData.put("from",mCurrent_user.getUid());
                                        notificationData.put("type","request");

                                        mNotificationDatabase.child(user_id).push().setValue(notificationData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                mCurrent_state="req_sent";
                                                mProfileSendReqBtn.setText("Cancel Friend Request");
                                                mDeclineBtn.setVisibility(View.INVISIBLE);
                                                mDeclineBtn.setEnabled(false);

                                            }
                                        });




                                    }
                                });



                            }
                            else{
                                Toast.makeText(ProfileActivity.this,"Failed Sending",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }

                // -----------how to cancel------------------

                if( mCurrent_state.equals("req_sent")){
                    mFriendsReqDatabase.child(mCurrent_user.getUid()).child(user_id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            mFriendsReqDatabase.child(user_id).child(mCurrent_user.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    mProfileSendReqBtn.setEnabled(true);
                                    mCurrent_state="not_friends";
                                    mProfileSendReqBtn.setText("Sent Request");

                                    mDeclineBtn.setVisibility(View.INVISIBLE);
                                    mDeclineBtn.setEnabled(false);


                                }
                            });

                        }
                    });


                }


                if(mCurrent_state.equals(("req_recived"))) {

                    final String curentDate=DateFormat.getDateTimeInstance().format(new Date());

                    mFriendDatabase.child(mCurrent_user.getUid()).child(user_id).setValue(curentDate).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                        mFriendDatabase.child(user_id).child(mCurrent_user.getUid()).setValue(curentDate).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                mFriendsReqDatabase.child(mCurrent_user.getUid()).child(user_id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        mFriendsReqDatabase.child(user_id).child(mCurrent_user.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                                mProfileSendReqBtn.setEnabled(true);
                                                mCurrent_state="friends";
                                                mProfileSendReqBtn.setText("Unfriend");

                                                mDeclineBtn.setVisibility(View.INVISIBLE);
                                                mDeclineBtn.setEnabled(false);


                                            }
                                        });

                                    }
                                });




                            }
                        });


                        }
                    });

                }

            }
        });
    }


}

