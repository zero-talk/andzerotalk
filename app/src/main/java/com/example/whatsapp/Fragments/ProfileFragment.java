package com.example.whatsapp.Fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.whatsapp.MainActivity;
import com.example.whatsapp.Model.Users;
import com.example.whatsapp.QuestionRegisterActivity;
import com.example.whatsapp.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {

    TextView username, usergender, user_profile_hash1, user_profile_hash2, user_profile_hash3;
    ImageView imageView;
    EditText proqn1, proqn2, proqn3;

    DatabaseReference reference;
    FirebaseUser fuser;

    //프로필 이미지
    StorageReference storageReference;

    //프로필 질문수정
    Button profile_modify;

    private static final int IMAGE_REQUEST = 1;
    private Uri imageUri;
    private StorageTask uploadTask;
    private Context context;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //fragment_profile.xml
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        imageView = view.findViewById(R.id.profile_image2);
        username = view.findViewById(R.id.usernamer);
        usergender = view.findViewById(R.id.usergender);
        proqn1 = view.findViewById(R.id.proqn1);
        proqn2 = view.findViewById(R.id.proqn2);
        proqn3 = view.findViewById(R.id.proqn3);

        user_profile_hash1 = view.findViewById(R.id.user_profile_hash1);
        user_profile_hash2 = view.findViewById(R.id.user_profile_hash2);
        user_profile_hash3 = view.findViewById(R.id.user_profile_hash3);

        //프로필 이미지
        storageReference = FirebaseStorage.getInstance().getReference("uploads");

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("MyUsers").child(fuser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Users user = dataSnapshot.getValue(Users.class);
                username.setText(user.getUsername());
                usergender.setText(user.getGender());
                proqn1.setText(user.getPro_q1());
                proqn2.setText(user.getPro_q2());
                proqn3.setText(user.getPro_q3());

                user_profile_hash1.setText("#"+user.getHash1());
                user_profile_hash2.setText("#"+user.getHash2());
                user_profile_hash3.setText("#"+user.getHash3());

                if(user.getImageURL().equals("default")){
                  imageView.setImageResource(R.mipmap.user_default_profile);
                } else {
                    Glide.with(getContext()).load(user.getImageURL()).into(imageView);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });


        //프로필 질문수정.
        context = container.getContext();
        profile_modify = view.findViewById(R.id.profile_modify);
        profile_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("").setMessage("프로필을 수정하시겠습니까?")
                        .setIcon(R.mipmap.empty)

                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String pro_q1 = proqn1.getText().toString();
                                String pro_q2 = proqn2.getText().toString();
                                String pro_q3 = proqn3.getText().toString();

                                if(pro_q1.equals(null)) { pro_q1 = ""; }
                                if(pro_q2.equals(null)) { pro_q2 = ""; }
                                if(pro_q3.equals(null)) { pro_q3 = ""; }

                                changeProfileQuestion(pro_q1, pro_q2, pro_q3);
                                Toast.makeText(context,"프로필이 변경되었습니다.", Toast.LENGTH_SHORT).show();
                            }
                        })

                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(context,"변경이 취소되었습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }).setCancelable(false).show();
            }
        });
        return view;
    }



    //질문답변 수정하기
    private void changeProfileQuestion( String pro_q1, String pro_q2, String pro_q3){

        reference = FirebaseDatabase.getInstance()
                .getReference("MyUsers").child(fuser.getUid());

        reference.child("pro_q1").setValue(pro_q1);
        reference.child("pro_q2").setValue(pro_q2);
        reference.child("pro_q3").setValue(pro_q3);
    }

    private String getFileExtension(Uri uri){

        ContentResolver contentResolver = getContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }


    //프로필 이미지 클릭시 프로필 이미지 업로드
    private void SelectImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);
    }


    //이미지 업로드
    private void UpLoadMyImage(){
       final ProgressDialog progressDialog = new ProgressDialog(getContext());
       progressDialog.setMessage("이미지 변경중");
       progressDialog.show();

       if(imageUri != null){
           final StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));

           uploadTask = fileReference.putFile(imageUri);
           uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
               @Override
               public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                   if (!task.isSuccessful()){
                       throw task.getException();
                   }
                   return fileReference.getDownloadUrl();
               }
           }).addOnCompleteListener(new OnCompleteListener<Uri>() {
               @Override
               public void onComplete(@NonNull Task<Uri> task) {

                   if(task.isSuccessful()){
                       Uri downLoadUri = task.getResult();
                       String mUri = downLoadUri.toString();

                       reference = FirebaseDatabase.getInstance().getReference("MyUsers").child(fuser.getUid());

                       HashMap<String, Object> map = new HashMap<>();

                       map.put("imageURL", mUri);
                       reference.updateChildren(map);

                       progressDialog.dismiss();

                   } else {
                       Toast.makeText(getContext(), "이미지 등록 실패", Toast.LENGTH_SHORT).show();
                   }

               }
           }).addOnFailureListener(new OnFailureListener() {
               @Override
               public void onFailure(@NonNull Exception e) {
                   Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                   progressDialog.dismiss();
               }
           });

       } else {
           Toast.makeText(getContext(), "이미지를 선택해 주세요.", Toast.LENGTH_SHORT).show();
       }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == IMAGE_REQUEST && resultCode == RESULT_OK
           && data != null && data.getData() != null) {

             imageUri = data.getData();

             if(uploadTask != null && uploadTask.isInProgress()){
                 Toast.makeText(getContext(), "업로드중입니다.", Toast.LENGTH_SHORT).show();
             } else {
                 UpLoadMyImage();
             }

        }

    }


}