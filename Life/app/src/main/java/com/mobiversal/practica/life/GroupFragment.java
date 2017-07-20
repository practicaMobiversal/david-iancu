package com.mobiversal.practica.life;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class GroupFragment extends Fragment {
    private View mMainView;
    private DatabaseReference mUserDatabase;
    public FirebaseRecyclerAdapter<Show_Chat_Activity_Data_Items, ChatActivity.Show_Chat_ViewHolder> mFirebaseAdapter;
    private ListView mGrupChat;
    private ListView mLVChat;
    private FirebaseAuth mAuth;
    private String mCurent_user_id;
    private ImageView mSendBtn,mAttachBtn;
    private EditText mImput;
    private FirebaseListAdapter myAdapter;



    public GroupFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {

        mMainView = inflater.inflate(R.layout.fragment_group, container, false);
        mGrupChat=(ListView) mMainView.findViewById(R.id.group_chat);
        mAuth = FirebaseAuth.getInstance();
        mUserDatabase= FirebaseDatabase.getInstance().getReference().child("users");
        mSendBtn=(ImageView) mMainView.findViewById(R.id.sendButton1);
        mAttachBtn=(ImageView) mMainView.findViewById((R.id.attachButton1));
        mImput=(EditText) mMainView.findViewById(R.id.messageArea1);

        return mMainView;
    }

    @Override
    public void onStart() {
        super.onStart();

        mSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseDatabase.getInstance().getReference().child("Group").push()
                        .setValue(new ChatMessage(mImput.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getEmail()));



                mLVChat.smoothScrollToPosition(myAdapter.getCount() - 1);
            }
        });

        displayChatMessage();
    }
    private void displayChatMessage() {

        mLVChat = (ListView) mMainView.findViewById(R.id.group_chat);
        myAdapter = new FirebaseListAdapter<ChatMessage>((Activity) getContext(), ChatMessage.class, R.layout.list_item, FirebaseDatabase.getInstance().getReference()) {
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
        mLVChat.setAdapter((ListAdapter) myAdapter);










    }
}
