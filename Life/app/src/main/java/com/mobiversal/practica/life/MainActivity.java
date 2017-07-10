package com.mobiversal.practica.life;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private FirebaseAuth mAuth;
    private Toolbar mToolbar;
    private FirebaseListAdapter<ChatMessage> myAdapter;
    RelativeLayout activity_main;

    private ListView mLVChat;

    private EditText input;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        mToolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Life");



        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message").child("1");

        //myRef.setValue("Hello, World!");

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

         input = (EditText) findViewById(R.id.input);

        activity_main =(RelativeLayout) findViewById(R.id.activity_main);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                // Read the input field and push a new instance
                // of ChatMessage to the Firebase database
                FirebaseDatabase.getInstance().getReference().push()
                        .setValue(new ChatMessage(input.getText().toString(),FirebaseAuth.getInstance().getCurrentUser().getEmail()));
                input.setText("");
                // Clear the input


                mLVChat.smoothScrollToPosition(myAdapter.getCount() -1);
            }
        });

        displayChatMessage();

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//
//            }
//        });
        //   Intent intent = new Intent(this,SecondActivity.class);
        //      startActivity(intent );

    }

    private void displayChatMessage() {

        mLVChat = (ListView) findViewById(R.id.list_of_message);
        myAdapter = new FirebaseListAdapter<ChatMessage>(this, ChatMessage.class, R.layout.list_item, FirebaseDatabase.getInstance().getReference()) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {

                //Get references to the views of list_item.xml
                TextView messageText, messageUser, messageTime;
                messageText = (TextView) v.findViewById(R.id.message_text);
                messageUser = (TextView) v.findViewById(R.id.message_user);
                messageTime = (TextView) v.findViewById(R.id.message_time);

                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());
                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", model.getMessageTime()));


            }
        };
        mLVChat.setAdapter(myAdapter);

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

        return true;
    }
}
