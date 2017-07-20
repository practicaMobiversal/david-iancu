package com.mobiversal.practica.life;


import android.location.Location;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Geocodes extends AppCompatActivity {
//    private Toolbar mToolbar;
    private RecyclerView mUsersList;
    private DatabaseReference mUsersDatabase;
    private static final double METERS_LIMIT=5000;
    private static double latitude;
    private static double longitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_activiy);


//        mToolbar=(Toolbar) findViewById(R.id.users_appbar);
//        setSupportActionBar(mToolbar);
//        getSupportActionBar().setTitle("All Users");
//        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("users");

        mUsersDatabase.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    if(LoginActivity.userEmail.equals(user.getEmail())) {
                        latitude = Double.valueOf(user.getLatitude());
                        longitude = Double.valueOf(user.getLongitude());
                    }
                }
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    if(!LoginActivity.userEmail.equals(user.getEmail())) {
                        Location locationA = new Location("A");
                        locationA.setLatitude(latitude);
                        locationA.setLongitude(longitude);
                        Location locationB = new Location("B");
                        locationB.setLatitude(Double.valueOf(user.getLatitude()));
                        locationB.setLongitude(Double.valueOf(user.getLongitude()));
                        float distance = locationA.distanceTo(locationB);

                        if(distance == METERS_LIMIT) {
                           // snapshot.getRef().removeValue();
                        }
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mUsersList=(RecyclerView) findViewById(R.id.users_list);
        mUsersList.setHasFixedSize(true);
        mUsersList.setLayoutManager(new LinearLayoutManager(this));

        final FirebaseRecyclerAdapter<User,UsersViewHolder> firebaseRecyclerAdapter=
                new FirebaseRecyclerAdapter<User, UsersViewHolder>(User.class, R.layout.users_single, UsersViewHolder.class, mUsersDatabase ) {
//                    @Override
//                    public UsersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//                        UsersViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
//
//                        viewHolder.setOnClickListener(new UsersViewHolder.ClickListener() {
//                            @Override
//                            public void onItemClick(View v, int position) {
//                                Intent profileIntent = new Intent (Geocodes.this,ProfileActivity.class);
//                                final String user_id = getRef(position).getKey();
//                                profileIntent.putExtra("user_id", user_id);
//                                startActivity(profileIntent);
//                            }
//                        });
//
//                        return viewHolder;
//                    }

                    @Override
                    protected void populateViewHolder(UsersViewHolder viewHolder, User users, int position) {


                        viewHolder.nameView.setText(users.getName());
                        Picasso.with(getApplicationContext())
                                .load(users.gettumbimg())
                                .placeholder(R.drawable.default_user)
                                .into(viewHolder.circImageView);
//                        if(LoginActivity.userEmail.equals(users.getEmail()))
//                            viewHolder.Layout_hide();
//                        else
//                            viewHolder.Person_Email(users.getEmail());
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

//    @Override
//    protected void onStart() {
//        super.onStart();
//
//    }



    public static class UsersViewHolder extends RecyclerView.ViewHolder{
        TextView nameView, person_email;
        CircleImageView circImageView;
        //final LinearLayout.LayoutParams params;

        private final LinearLayout layout;

        private UsersViewHolder.ClickListener myListener;

        public interface ClickListener {
            public void onItemClick(View view, int position);
        }

        public UsersViewHolder(View itemView) {
            super(itemView);
            nameView = (TextView) itemView.findViewById(R.id.user_single_name);
           // person_email = (TextView) itemView.findViewById(R.id.chat_persion_email);
            circImageView = (CircleImageView) itemView.findViewById(R.id.single_user_img);
            layout = (LinearLayout)itemView.findViewById(R.id.show_chat_single_item_layout);
          //  params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myListener.onItemClick(v, getAdapterPosition());
                }
            });
        }

//        private void Layout_hide() {
//            params.height = 0;
//            //itemView.setLayoutParams(params);
//            layout.setLayoutParams(params);
//
//        }

        private void Person_Email(String title) {
            person_email.setText(title);
        }

//        public void setOnClickListener(ClickListener clickListener) {
//            myListener = clickListener;
//
//
//        }
    }



}
