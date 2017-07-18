package com.mobiversal.practica.life;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity implements GroupFragment.OnFragmentInteractionListener,SearchFragment.OnFragmentInteractionListener{


    private static final String TAG = "MainActivity";
    private FirebaseAuth mAuth;
    private Toolbar mToolbar;
    private FirebaseListAdapter<ChatMessage> myAdapter;
    RelativeLayout activity_main;

    private ListView mLVChat;
    private EditText input;
    private ViewPager mViewPager;
    private SectionPagerAdapter mSectionPagerAdapter;
    private TabLayout mTabLayout;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_main);
        if(!RegisterActivity.loginStatus)
            sendToStart();
        mAuth = FirebaseAuth.getInstance();


       mToolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Life");



       // Setup ViewPager.
        mViewPager = (ViewPager) findViewById(R.id.main_tab_pager);
        mSectionPagerAdapter = new SectionPagerAdapter(getSupportFragmentManager()); // <-- This is the key
        mViewPager.setAdapter(mSectionPagerAdapter);

        mViewPager.setOffscreenPageLimit(4);

        mTabLayout=(TabLayout) findViewById(R.id.main_tabs);
        mTabLayout.setupWithViewPager(mViewPager);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message").child("1");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });





    }






    @Override
    protected void onStart() {
        super.onStart();
        Log.d( TAG,"onStart");
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null)
        {
            sendToStart();
        }
    }

    private void sendToStart() {
        Intent startIntent =new Intent(MainActivity.this,StartActivity.class);
        startActivity(startIntent);
        finish();

    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d( TAG,"onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d( TAG,"onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d( TAG,"onStop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d( TAG,"onRestart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d( TAG,"onDestroy");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super .onOptionsItemSelected(item);
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
        if(item.getItemId()== R.id.main_logout_btn){
            FirebaseAuth.getInstance().signOut();
            sendToStart();
        }



        if(item.getItemId()== R.id.main_all_btn){
            Intent allusers= new Intent( MainActivity.this,UserActiviy.class);
            startActivity(allusers);
        }

        if(item.getItemId()== R.id.settings_btn){
            Intent settings= new Intent( MainActivity.this,SettingActivity.class);
            startActivity(settings);
        }


        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public boolean checkLogInStatus() {
        boolean status = false;



        return status;
    }
}
