package com.example.whatsapp.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.whatsapp.Adapter.UserAdapter;
import com.example.whatsapp.Model.Chatlist;
import com.example.whatsapp.Model.Users;
import com.example.whatsapp.R;
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

    FirebaseUser fuser;
    DatabaseReference reference;

    private List<Chatlist> usersList;

    RecyclerView recyclerView;

    //채팅이 없을 때
    ImageView chats_empty;

    public ChatsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chats, container, false);

        recyclerView = view.findViewById(R.id.recycler_view2);
        chats_empty = view.findViewById(R.id.chats_empty);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fuser = FirebaseAuth.getInstance().getCurrentUser();

        usersList = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("ChatList").child(fuser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                usersList.clear();

                //모든 유저 불러오기
                for( DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chatlist chatlist = snapshot.getValue(Chatlist.class);
                    usersList.add(chatlist);
                }
                chatList();

                //채팅이 없으면 chat_empty.jpg, 있으면 채팅리스트
                if( usersList.isEmpty() ){
                    recyclerView.setVisibility(View.GONE);
                    chats_empty.setVisibility(View.VISIBLE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    chats_empty.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }

    private void chatList(){

        // 최근 채팅 모두 가져오기
        mUsers = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("MyUsers");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();

                for( DataSnapshot snapshot : dataSnapshot.getChildren()){

                    Users user = snapshot.getValue(Users.class);
                    for(Chatlist chatlist : usersList){

                        //채팅리스트의 id와 유저 id가 같으면
                        if(user.getId().equals(chatlist.getId())){
                            mUsers.add(user);
                        }
                    }
                }

                userAdapter = new UserAdapter(getContext(), mUsers, true);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }



}