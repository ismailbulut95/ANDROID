package com.demo.mobilproje.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.demo.mobilproje.Adapter.UserAdapter;
import com.demo.mobilproje.Model.ChatList;
import com.demo.mobilproje.Model.Users;
import com.demo.mobilproje.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ChatsFragment extends Fragment {


    private UserAdapter userAdapter;
    private List<Users> mUsers;

    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;

    private List<ChatList> userList;

    RecyclerView recyclerView;

    public ChatsFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_chats, container, false);

        recyclerView = view.findViewById(R.id.recycler_View2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        userList = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("ChatList")
                .child(firebaseUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();

                //Loop for all users
                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                    ChatList chatList = snapshot1.getValue(ChatList.class);
                    userList.add(chatList);
                }
                chatList();
            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }
    private void chatList() {
        //all chat information

        mUsers = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mUsers.clear();

                for (DataSnapshot snapshot1 : snapshot.getChildren()){

                    Users users = snapshot1.getValue(Users.class);
                    for (ChatList chatList : userList){
                        if (users.getId().equals(chatList.getId())){
                            mUsers.add(users);
                        }
                    }
                }
                userAdapter = new UserAdapter(getContext(),mUsers);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}