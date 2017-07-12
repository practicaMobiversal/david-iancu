package com.mobiversal.practica.life;


import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserActiviy extends AppCompatActivity {
        private Toolbar mToolbar;
    private RecyclerView mUsersList;
    private DatabaseReference mUsersDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_activiy);


        mToolbar=(Toolbar) findViewById(R.id.users_appbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("All Users");
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        mUsersDatabase= FirebaseDatabase.getInstance().getReference().child("users");

        mUsersList=(RecyclerView) findViewById(R.id.users_list);
        mUsersList.setHasFixedSize(true);
        mUsersList.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Users,UsersViewHolder> firebaseRecyclerAdapter=
                new FirebaseRecyclerAdapter<Users, UsersViewHolder>(Users.class, R.layout.users_single, UsersViewHolder.class,mUsersDatabase ) {


            @Override
            protected void populateViewHolder(UsersViewHolder viewHolder, Users users, int position) {

                viewHolder.setName(users.getName());
                viewHolder.setImage(users.getTumbImage(),getApplicationContext());

                final String user_id=getRef(position).getKey();
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent profileIntent = new Intent (UserActiviy.this,ProfileActivity.class);
                        profileIntent.putExtra("user_id",user_id);
                        startActivity(profileIntent);



                    }
                });

            }
        };
        mUsersList.setAdapter(firebaseRecyclerAdapter);

    }



    public static class UsersViewHolder extends RecyclerView.ViewHolder{
        View mView;

        public UsersViewHolder(View itemView) {
            super(itemView);

            mView=itemView;

        }

        public void setName(String name){

            TextView mUserName= (TextView) mView.findViewById(R.id.user_single_name);
            mUserName.setText(name);
        }

         public void setImage(String tumb_image, Context ct){

             CircleImageView userImageView=(CircleImageView) mView.findViewById(R.id.circleImageView) ;
             Picasso.with(ct).load(tumb_image).placeholder(R.drawable.default_user).into(userImageView);

       }
    }



}
