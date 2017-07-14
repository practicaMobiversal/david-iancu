package com.mobiversal.practica.life;


import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseUser;
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

        FirebaseRecyclerAdapter<Show_Chat_Activity_Data_Items,UsersViewHolder> firebaseRecyclerAdapter=
                new FirebaseRecyclerAdapter<Show_Chat_Activity_Data_Items, UsersViewHolder>(Show_Chat_Activity_Data_Items.class, R.layout.users_single, UsersViewHolder.class,mUsersDatabase ) {
                    @Override
                    public UsersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                        UsersViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);

                        viewHolder.setOnClickListener(new UsersViewHolder.ClickListener() {
                            @Override
                            public void onItemClick(View v, int position) {
                                Intent profileIntent = new Intent (UserActiviy.this,ProfileActivity.class);
                                final String user_id = getRef(position).getKey();
                                profileIntent.putExtra("user_id", user_id);
                                startActivity(profileIntent);
                            }
                        });

            protected void populateViewHolder(UsersViewHolder viewHolder, Users users, int position) { @Override
            protected void populateViewHolder(UsersViewHolder viewHolder, Show_Chat_Activity_Data_Items users, int position) {

 viewHolder.setName(users.getName());
                viewHolder.setImage(users.gettumbimg(),getApplicationContext());

                final String user_id=getRef(position).getKey();
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                      @Override
                    protected void populateViewHolder(UsersViewHolder viewHolder, Users users, int position) {

 viewHolder.nameView.setText(users.getName());
                        Picasso.with(getApplicationContext())
                                .load(users.gettumbimg())
                                .placeholder(R.drawable.default_user)
                                .into(viewHolder.circImageView);                    }
                };
        mUsersList.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();



    }



    public static class UsersViewHolder extends RecyclerView.ViewHolder{
        TextView nameView;
        CircleImageView circImageView;

        private UsersViewHolder.ClickListener myListener;

        public interface ClickListener {
            public void onItemClick(View view, int position);
        }

        public UsersViewHolder(View itemView) {
            super(itemView);
            nameView = (TextView) itemView.findViewById(R.id.user_single_name);
            circImageView = (CircleImageView) itemView.findViewById(R.id.single_user_img);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myListener.onItemClick(v, getAdapterPosition());
                }
            });
       }

       public void setOnClickListener(ClickListener clickListener) {
           myListener = clickListener;
       }
    }



}
