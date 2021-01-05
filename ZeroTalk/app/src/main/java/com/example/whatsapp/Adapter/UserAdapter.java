package com.example.whatsapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.whatsapp.MessageActivity;
import com.example.whatsapp.Model.Users;
import com.example.whatsapp.R;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context context;
    private List<Users> mUsers;
    private boolean isChat;

    public UserAdapter(Context context, List<Users> mUsers, boolean isChat) {
        this.context = context;
        this.mUsers = mUsers;
        this.isChat = isChat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_item, parent, false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Users users = mUsers.get(position);
        holder.username.setText(users.getUsername());

        if (users.getImageURL().equals("default")){
            holder.userProfile.setImageResource(R.mipmap.user_default_profile);
        } else {
            Glide.with(context).load(users.getImageURL()).into(holder.userProfile);
        }


        //접속상태 체크. 온라인 or 오프라인 버튼 보이기
        if (isChat) {
            if(users.getStatus().equals("online")){
                holder.imageViewON.setVisibility(View.VISIBLE);
                holder.imageViewOFF.setVisibility(View.GONE);
            } else {
                holder.imageViewON.setVisibility(View.GONE);
                holder.imageViewOFF.setVisibility(View.VISIBLE);
            }

        } else {
            holder.imageViewON.setVisibility(View.GONE);
            holder.imageViewOFF.setVisibility(View.GONE);
        }


        //유저 프로필 클릭시 채팅 시작
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MessageActivity.class);
                intent.putExtra("userid", users.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView username;
        public ImageView userProfile;

        //온, 오프라인 상태태
        public ImageView imageViewON;
        public ImageView imageViewOFF;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.usernamer);
            userProfile = itemView.findViewById(R.id.userProfile);
            imageViewON = itemView.findViewById(R.id.statusimageON);
            imageViewOFF = itemView.findViewById(R.id.statusimageOFF);
        }
    }
}
