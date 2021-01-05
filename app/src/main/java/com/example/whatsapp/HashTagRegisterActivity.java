package com.example.whatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class HashTagRegisterActivity extends AppCompatActivity {

    String[] hasharr = {"연애", "집콕", "음악감상","영화","맛집탐방",
                        "드라마", "MBTI", "실외취미", "실내취미",
                        "운동", "E-Sports","요리"};

    ArrayList<CheckBox> hashes;
    ArrayList<String> items;
    int result;

    //Firebase
    FirebaseAuth auth;
    DatabaseReference myRef;

    //등록버튼
    Button button_hastag_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hash_tag_register);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        auth = FirebaseAuth.getInstance();

        hashes = new ArrayList<>();

        CheckBox check1 = findViewById(R.id.love_hash);
        hashes.add(check1);

        CheckBox check2 = findViewById(R.id.zip_hash);
        hashes.add(check2);

        CheckBox check3 = findViewById(R.id.music_hash);
        hashes.add(check3);

        CheckBox check4 = findViewById(R.id.movie_hash);
        hashes.add(check4);

        CheckBox check5 = findViewById(R.id.food_hash);
        hashes.add(check5);

        CheckBox check6 = findViewById(R.id.drama_hash);
        hashes.add(check6);

        CheckBox check7 = findViewById(R.id.mbti_hash);
        hashes.add(check7);

        CheckBox check8 = findViewById(R.id.outdoor_hash);
        hashes.add(check8);

        CheckBox check9 = findViewById(R.id.indoor_hash);
        hashes.add(check9);

        CheckBox check10= findViewById(R.id.sport_hash);
        hashes.add(check10);

        CheckBox check11= findViewById(R.id.esports_hash);
        hashes.add(check11);

        CheckBox check12= findViewById(R.id.cook_hash);
        hashes.add(check12);

        //체크박스 리스너
        CheckBoxListener listener = new CheckBoxListener();
        for(CheckBox checkBox:hashes){
            checkBox.setOnCheckedChangeListener(listener);
        }

        //시작하기 버튼 클릭
        button_hastag_register = findViewById(R.id.button_hastag_register);
        button_hastag_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = getCount();
                if(count != 3){
                    Toast.makeText(getApplicationContext(), "해시태그를 3가지 선택해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    getItems();
                    getCount();
                    getHashValue();
                    HashTagRegister();
                }
            }
        });
    }

    //선택태그값 합산
    int getHashValue(){
        result=0;
        for(int i=0;i<hashes.size();i++){
            CheckBox checkBox = hashes.get(i);
            if(checkBox.isChecked()){
                int value = (int)Math.pow(2.0, (double)i);
                result += value;
            }
        }
        return result;
    }

    //해시태그 선택개수
    int getCount(){
        int count=0;
        for(int i=0;i<hashes.size();i++){
            CheckBox checkBox = hashes.get(i);
            if(checkBox.isChecked()){
                count+=1;
            }
        }
        return count;
    }

    //선택된 해시태그들 가져오기
    ArrayList<String> getItems(){
        items = new ArrayList<>();
        for(int i=0;i<hashes.size();i++){
            CheckBox checkBox = hashes.get(i);
            if(checkBox.isChecked()){
                String item = hasharr[i];
                items.add(item);
            }
        }
        return items;
    }

    //3개 이상 선택시 선택불가
    class CheckBoxListener implements CompoundButton.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            int count = getCount();
            if(count>3){
                buttonView.setChecked(false);
            }
        }
    }

    //fb 데이터 추가
    private void HashTagRegister(){

        FirebaseUser firebaseUser = auth.getCurrentUser();
        String userid = firebaseUser.getUid();
        myRef = FirebaseDatabase.getInstance()
                .getReference("MyUsers").child(userid);

        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        String gender = intent.getStringExtra("gender");
        String imageURL = intent.getStringExtra("imageURL");
        String status = intent.getStringExtra("status");
        String pro_q1 = intent.getStringExtra("pro_q1");
        String pro_q2 = intent.getStringExtra("pro_q2");
        String pro_q3 = intent.getStringExtra("pro_q3");

        //Hashmap
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("id", userid);
        hashMap.put("username", username);
        hashMap.put("gender", gender);
        hashMap.put("imageURL", imageURL);
        hashMap.put("status", status);
        hashMap.put("pro_q1", pro_q1);
        hashMap.put("pro_q2", pro_q2);
        hashMap.put("pro_q3", pro_q3);
        hashMap.put("hash1", items.get(0));
        hashMap.put("hash2", items.get(1));
        hashMap.put("hash3", items.get(2));
        hashMap.put("hashtotal",result+"");

        myRef.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(HashTagRegisterActivity.this, MainActivity.class);
                    intent.putExtra("id", userid);
                    intent.putExtra("username", username);
                    intent.putExtra("gender", gender);
                    intent.putExtra("imageURL", "default");
                    intent.putExtra("status", "offline");
                    intent.putExtra("pro_q1", pro_q1);
                    intent.putExtra("pro_q2", pro_q2);
                    intent.putExtra("pro_q3", pro_q3);
                    intent.putExtra("hash1", items.get(0));
                    intent.putExtra("hash2", items.get(1));
                    intent.putExtra("hash3", items.get(2));
                    intent.putExtra("hashtotal",result+"");
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}