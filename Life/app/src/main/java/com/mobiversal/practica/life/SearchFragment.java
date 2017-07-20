package com.mobiversal.practica.life;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

import de.hdodenhof.circleimageview.CircleImageView;


public class SearchFragment extends Fragment {
    private RecyclerView mUsersList;
    private View mMainView;
    private DatabaseReference mUsersDatabase;

    private double metersLimit;
    private static double latitude;
    private static double longitude;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mMainView = inflater.inflate(R.layout.fragment_search, container, false);
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        mUsersList = (RecyclerView) mMainView.findViewById(R.id.search_fragment);
        mUsersList.setHasFixedSize(true);
        mUsersList.setLayoutManager(new LinearLayoutManager(SearchFragment.this.getContext()));

        mUsersDatabase.addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    if (LoginActivity.userEmail.equals(user.getEmail())) {
                        latitude = Double.valueOf(user.getLatitude());
                        longitude = Double.valueOf(user.getLongitude());
                        metersLimit = Double.valueOf(user.getGeoLimit());
                    }
                }

                //   List<>
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return mMainView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

//        mToolbar=(Toolbar) findViewById(R.id.users_appbar);
//        setSupportActionBar(mToolbar);
//        getSupportActionBar().setTitle("All Users");
//        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

//       mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("users");
//        mUsersDatabase.addListenerForSingleValueEvent(new ValueEventListener()




//
@Override
public void onStart() {
    super.onStart();

    final FirebaseRecyclerAdapter<User, UsersViewHolder> firebaseRecyclerAdapter =
            new FirebaseRecyclerAdapter<User, UsersViewHolder>(User.class, R.layout.users_single, UsersViewHolder.class, mUsersDatabase) {
                @Override
                public UsersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    UsersViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);

                    viewHolder.setOnClickListener(new UsersViewHolder.ClickListener() {
                        @Override
                        public void onItemClick(View v, int position) {
                            Intent profileIntent = new Intent(getContext(), ProfileActivity.class);
                            final String user_id = getRef(position).getKey();
                            profileIntent.putExtra("user_id", user_id);
                            startActivity(profileIntent);
                        }
                    });
                    return viewHolder;
                }

                @Override
                protected void populateViewHolder(final UsersViewHolder viewHolder, User users, int position) {

                    if (LoginActivity.userEmail.equals(users.getEmail()))
                        viewHolder.Layout_hide();
                    if (!LoginActivity.userEmail.equals(users.getEmail())) {
                        Location locationA = new Location("A");
                        locationA.setLatitude(latitude);
                        locationA.setLongitude(longitude);
                        Location locationB = new Location("B");
                        locationB.setLatitude(Double.valueOf(users.getLatitude()));
                        locationB.setLongitude(Double.valueOf(users.getLongitude()));
                        float distance = locationA.distanceTo(locationB);
                        if (distance > metersLimit)
                            viewHolder.Layout_hide();
                        else {
                            viewHolder.nameView.setText(users.getName());
                            Picasso.with(getContext())
                                    .load(users.gettumbimg())
                                    .placeholder(R.drawable.default_user)
                                    .into(viewHolder.circImageView);
                        }
//                        if(LoginActivity.userEmail.equals(users.getEmail()))
//                            viewHolder.Layout_hide();
//                        else
//                            viewHolder.Person_Email(users.getEmail());
                    }
                }
            };
    mUsersList.setAdapter(firebaseRecyclerAdapter);

    new Handler().postDelayed(new Runnable() {
        @Override
        public void run() {
            firebaseRecyclerAdapter.notifyDataSetChanged();
        }
    }, 500L);
}package com.mobiversal.practica.life;

import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class SearchFragment extends Fragment {
    private RecyclerView mUsersList;
    private View mMainView;
    private DatabaseReference mUsersDatabase;

    private double metersLimit;
    private static double latitude;
    private static double longitude;

public class SearchFragment extends Fragment {
    private RecyclerView mUsersList;
    private View mMainView;
    private DatabaseReference mUsersDatabase;

    private double metersLimit;
    private static double latitude;
    private static double longitude;    @Override
  public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

 @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mMainView = inflater.inflate(R.layout.fragment_search, container, false);
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        mUsersList = (RecyclerView) mMainView.findViewById(R.id.search_fragment);
        mUsersList.setHasFixedSize(true);
        mUsersList.setLayoutManager(new LinearLayoutManager(SearchFragment.this.getContext()));

        mUsersDatabase.addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    if (LoginActivity.userEmail.equals(user.getEmail())) {
                        latitude = Double.valueOf(user.getLatitude());
                        longitude = Double.valueOf(user.getLongitude());
                        metersLimit = Double.valueOf(user.getGeoLimit());
                    }
                }

                //   List<>
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return mMainView;public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mMainView = inflater.inflate(R.layout.fragment_search, container, false);
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        mUsersList = (RecyclerView) mMainView.findViewById(R.id.search_fragment);
        mUsersList.setHasFixedSize(true);
        mUsersList.setLayoutManager(new LinearLayoutManager(SearchFragment.this.getContext()));

        mUsersDatabase.addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    if (LoginActivity.userEmail.equals(user.getEmail())) {
                        latitude = Double.valueOf(user.getLatitude());
                        longitude = Double.valueOf(user.getLongitude());
                        metersLimit = Double.valueOf(user.getGeoLimit());
                    }
                }

                //   List<>
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return mMainView;    }

//        mToolbar=(Toolbar) findViewById(R.id.users_appbar);
//        setSupportActionBar(mToolbar);
//        getSupportActionBar().setTitle("All Users");
//        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

//       mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("users");
//        mUsersDatabase.addListenerForSingleValueEvent(new ValueEventListener()




//
@Override
public void onStart() {
    super.onStart();

    final FirebaseRecyclerAdapter<User, UsersViewHolder> firebaseRecyclerAdapter =
            new FirebaseRecyclerAdapter<User, UsersViewHolder>(User.class, R.layout.users_single, UsersViewHolder.class, mUsersDatabase) {
                @Override
                public UsersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    UsersViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);

                    viewHolder.setOnClickListener(new UsersViewHolder.ClickListener() {
                        @Override
                        public void onItemClick(View v, int position) {
                            Intent profileIntent = new Intent(getContext(), ProfileActivity.class);
                            final String user_id = getRef(position).getKey();
                            profileIntent.putExtra("user_id", user_id);
                            startActivity(profileIntent);
                        }
                    });
                    return viewHolder;
                }

                @Override
                protected void populateViewHolder(final UsersViewHolder viewHolder, User users, int position) {

                    if (LoginActivity.userEmail.equals(users.getEmail()))
                        viewHolder.Layout_hide();
                    if (!LoginActivity.userEmail.equals(users.getEmail())) {
                        Location locationA = new Location("A");
                        locationA.setLatitude(latitude);
                        locationA.setLongitude(longitude);
                        Location locationB = new Location("B");
                        locationB.setLatitude(Double.valueOf(users.getLatitude()));
                        locationB.setLongitude(Double.valueOf(users.getLongitude()));
                        float distance = locationA.distanceTo(locationB);
                        if (distance > metersLimit)
                            viewHolder.Layout_hide();
                        else {
                            viewHolder.nameView.setText(users.getName());
                            Picasso.with(getContext())
                                    .load(users.gettumbimg())
                                    .placeholder(R.drawable.default_user)
                                    .into(viewHolder.circImageView);
                        }
//                        if(LoginActivity.userEmail.equals(users.getEmail()))
//                            viewHolder.Layout_hide();
//                        else
//                            viewHolder.Person_Email(users.getEmail());
                    }
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }}
