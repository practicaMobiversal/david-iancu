package com.mobiversal.practica.life;


import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
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

        final FirebaseRecyclerAdapter<User,UsersViewHolder> firebaseRecyclerAdapter=
                new FirebaseRecyclerAdapter<User, UsersViewHolder>(User.class, R.layout.users_single, UsersViewHolder.class,mUsersDatabase ) {
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

                        return viewHolder;
                    }

                    @Override
                    protected void populateViewHolder(UsersViewHolder viewHolder, User users, int position) {
                        if (LoginActivity.userEmail.equals(users.getEmail()))
                            viewHolder.Layout_hide();
                        viewHolder.nameView.setText(users.getName());
                        Picasso.with(getApplicationContext())
                                .load(users.gettumbimg())
                                .placeholder(R.drawable.default_user)
                                .into(viewHolder.circImageView);

                    }
                };
        mUsersList.setAdapter(firebaseRecyclerAdapter);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                firebaseRecyclerAdapter.notifyDataSetChanged();
            }
        }, 500L);


    }

    @Override
    protected void onStart() {
        super.onStart();

    }



    public static class UsersViewHolder extends RecyclerView.ViewHolder{
        TextView nameView;
        CircleImageView circImageView;
        private final RelativeLayout layout;
        final RelativeLayout.LayoutParams params;
        private UsersViewHolder.ClickListener myListener;

        public interface ClickListener {
            public void onItemClick(View view, int position);
        }

        public UsersViewHolder(View itemView) {
            super(itemView);
            nameView = (TextView) itemView.findViewById(R.id.user_single_name);
            circImageView = (CircleImageView) itemView.findViewById(R.id.single_user_img);
            layout = (RelativeLayout) itemView.findViewById(R.id.user_single_item);
            params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
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
        private void Layout_hide() {
            params.height=0;
            layout.setLayoutParams(params);
        }
    }



}
