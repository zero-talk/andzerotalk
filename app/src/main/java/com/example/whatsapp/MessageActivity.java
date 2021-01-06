package com.example.whatsapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.whatsapp.Adapter.MessageAdapter;
import com.example.whatsapp.Model.Chat;
import com.example.whatsapp.Model.Users;
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

    FirebaseUser fuser;
    DatabaseReference reference;
    Intent intent;

    RecyclerView recyclerView;
    EditText msg_editText;
    ImageButton sendBTN;

    MessageAdapter messageAdapter;
    List<Chat> mchat;
    String userid;

    ValueEventListener seenListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //유저 프로필, 유저이름
        imageView = findViewById(R.id.imageview_profile);
        username = findViewById(R.id.usernamey);

        //보내기 버튼
        sendBTN = findViewById(R.id.btn_send);
        msg_editText = findViewById(R.id.text_send);

        //리사이클러뷰
        recyclerView = findViewById(R.id.recycler_view);  //activity_message.xml
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        intent = getIntent();
        userid = intent.getStringExtra("userid");
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("MyUsers").child(userid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users user = snapshot.getValue(Users.class);
                username.setText(user.getUsername());

                //프로필 이미지가 디폴트면
                if(user.getImageURL().equals("default")){
                    imageView.setImageResource(R.mipmap.user_default_profile);
                } else {
                    Glide.with(MessageActivity.this).load(user.getImageURL()).into(imageView);
                }

                readMessages(fuser.getUid(), userid, user.getImageURL());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//
//        View.OnClickListener listener = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // 다이얼로그
//                AlertDialog.Builder builder = new AlertDialog.Builder(MessageActivity.this);
//                builder.setTitle("상대방 프로필 보기");
////                builder.setIcon(R.mipmap.ic_launcher);
//                LayoutInflater inflater = getLayoutInflater();
//                builder.setView(R.layout.fragment_profile);
//
////                builder.setNeutralButton("취소", listener);
//                builder.show();
//
//            }
//        };
//        imageView.setOnClickListener(listener);
//        username.setOnClickListener(listener);




        //메시지 보내기 버튼 클릭
        sendBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = msg_editText.getText().toString();
                if( !msg.equals("")){
                    sendMessage(fuser.getUid(), userid, msg);
                } else {
                    Toast.makeText(MessageActivity.this, "메시지를 작성해 주세요.", Toast.LENGTH_SHORT).show();
                }
                msg_editText.setText("");
            }
        });

        SeenMessage(userid);

    }

    //메시지 확인여부 체크
    private void SeenMessage(String userid) {

        reference = FirebaseDatabase.getInstance().getReference("Chats");

        seenListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                   Chat chat = snapshot.getValue(Chat.class);

                   if(chat.getReceiver().equals(fuser.getUid()) && chat.getSender().equals(userid)) {

                       HashMap<String, Object> hashMap = new HashMap<>();
                       hashMap.put("isseen", true);
                       snapshot.getRef().updateChildren(hashMap);
                   }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //메시지 보내기
    private void sendMessage(String sender, String receiver, String message){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);
        hashMap.put("isseen", false);

        reference.child("Chats").push().setValue(hashMap);

        //chat fragment 추가 (최근 채팅)
        final DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("ChatList")
                                          .child(fuser.getUid()).child(userid);

        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()){
                    chatRef.child("id").setValue(userid);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    //메시지 받기
    private void readMessages(String myid, String userid, String imageurl){

        mchat = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mchat.clear();
                for( DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if(chat.getReceiver().equals(myid) && chat.getSender().equals(userid)  ||
                       chat.getReceiver().equals(userid) && chat.getSender().equals(myid)) {
                        mchat.add(chat);
                    }

                    messageAdapter = new MessageAdapter(MessageActivity.this, mchat, imageurl);
                    recyclerView.setAdapter(messageAdapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    //접속 상태 체크 (온라인, 오프라인)
    private void CheckStatus(String status){

        reference = FirebaseDatabase.getInstance().getReference("MyUsers").child(fuser.getUid());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);

        reference.updateChildren(hashMap);
    }

    @Override
    protected void onResume(){
        super.onResume();
        CheckStatus("online");
    }

    @Override
    protected void onPause(){
        super.onPause();
        reference.removeEventListener(seenListener);
        CheckStatus("offline");
    }

}