package com.demo.mobilproje;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.demo.mobilproje.Adapter.MessageAdapter;
import com.demo.mobilproje.Model.Chat;
import com.demo.mobilproje.Model.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessageActivity extends AppCompatActivity {

    TextView username;
    ImageView imageView;

    RecyclerView recyclerView;
    EditText msg_ET;
    ImageButton sendBtn;

    MessageAdapter messageAdapter;
    List<Chat> chats;

    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    Intent intent;
    String userid;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        username = findViewById(R.id.username);
        imageView = findViewById(R.id.imageViewProfile);
        sendBtn = findViewById(R.id.btn_send);
        msg_ET = findViewById(R.id.text_send);

        //Recyclerview

        recyclerView = findViewById(R.id.recycler_View);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        //Toolbar toolbar =findViewById(R.id.toolbar2);
        /*setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/
      /*  toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/


    intent = getIntent();
    userid = intent.getStringExtra("userid");

    firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
    databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

    databaseReference.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users users = snapshot.getValue(Users.class);
                username.setText(users.getUsername());

                if (users.getImageURL().equals("default")){
                    imageView.setImageResource(R.mipmap.ic_launcher);
                }
                else{
                    Glide.with(MessageActivity.this)
                            .load(users.getImageURL())
                            .into(imageView);
                }
                readMessage(firebaseUser.getUid(),userid,users.getImageURL());
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    });

    sendBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String msg =msg_ET.getText().toString();
            if (!msg.equals("")){
                sendMessage(firebaseUser.getUid(),userid,msg);

            }
            else {
                Toast.makeText(MessageActivity.this, "Can not send empty Message.", Toast.LENGTH_SHORT).show();
            }
            msg_ET.setText("");
        }
    });


    }

    private void sendMessage(String sender,String receiver,String message){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("message",message);

        databaseReference.child("Chats").push().setValue(hashMap);

        //Latest chat with contacts

        final DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("ChatList")
                .child(firebaseUser.getUid())
                .child(userid);

        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()){
                    chatRef.child("id").setValue(userid);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    private void readMessage(String myId,String userId,String imageurl){
            chats = new ArrayList<>();

            databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    chats.clear();
                    for (DataSnapshot snapshot1 : snapshot.getChildren()){
                        Chat chat = snapshot1.getValue(Chat.class);
                        if ((chat.getReceiver().equals(myId) && chat.getSender().equals(userId)) || (chat.getReceiver().equals(userId) && chat.getSender().equals(myId))){
                            chats.add(chat);
                        }
                        messageAdapter = new MessageAdapter(MessageActivity.this,chats,imageurl);
                        recyclerView.setAdapter(messageAdapter);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
    }

}