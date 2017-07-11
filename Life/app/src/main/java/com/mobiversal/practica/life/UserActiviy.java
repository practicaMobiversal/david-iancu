package com.mobiversal.practica.life;


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

        mUsersDatabase= FirebaseDatabase.getInstance().getReference().child("Users");

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
    }



}
