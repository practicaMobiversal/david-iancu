package com.mobiversal.practica.life;

import android.content.Intent;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingActivity extends AppCompatActivity {

    private DatabaseReference mUserDatabase;
private FirebaseUser mCurentUser;
    private CircleImageView mImage;
    private TextView mName;
    private Button mImageBtn;
    private static final int GallPick=1;


    //storage

    private StorageReference mProfImg;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        mName=(TextView) findViewById(R.id.settings_name);
        mImage=(CircleImageView) findViewById(R.id.settings_image);
        mImageBtn=(Button) findViewById(R.id.settings_image_btn);

        mProfImg= FirebaseStorage.getInstance().getReference();

        mCurentUser= FirebaseAuth.getInstance().getCurrentUser();
        String curent_id=mCurentUser.getUid();

        mUserDatabase=FirebaseDatabase.getInstance().getReference().child("users").child(curent_id);

        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String name=dataSnapshot.child("name").getValue().toString();
                String image=dataSnapshot.child("image").getValue().toString();
                mName.setText(name);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

mImageBtn.setOnClickListener(new View.OnClickListener(){
    @Override
    public void onClick(View v) {

       Intent gall= new Intent();
        gall.setType("image/*");
        gall.setAction(getIntent().ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(gall, "Set Image"),GallPick);


    }});


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode== GallPick && resultCode==RESULT_OK){
        Uri imaguri= data.getData();

            CropImage.activity(imaguri)
                    .setAspectRatio(1,1)
                    .start(this);

    }


        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();

                StorageReference storagepath=mProfImg.child("profile_images").child(random()+ ".jpg");
                storagepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {


                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        if (task.isSuccessful()) {

                        }

                    }
                });


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }




    }

    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(10);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }

}
