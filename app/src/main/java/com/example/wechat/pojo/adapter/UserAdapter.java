package com.example.wechat.pojo.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.wechat.R;
import com.example.wechat.model.Users;
import com.example.wechat.model.messages;
import com.example.wechat.ui.MessageActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.viewHolder> {

    private ArrayList<Users> arrayList;
    Context context;
    boolean isOnline;
    String lastMessage;

    public UserAdapter (Context context, ArrayList<Users> arrayList,boolean isOnline){
        this.context = context;
        this.arrayList = arrayList;
        this.isOnline = isOnline;

    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new viewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.user_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Users users = arrayList.get(position);
        holder.username.setText(users.getUsername());
        if (users.getImageURL().equals("def")) {
            holder.imageView.setImageResource(R.drawable.image);
        }
        else {
            Glide.with(context).load(users.getImageURL()).into( holder.imageView);
        }

        if(isOnline) {
            getLastMessage(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid(), users.getId(), holder.lastMessage);
            if (users.getStatus().equals("online")) {
                holder.status.setVisibility(View.VISIBLE);
            }
            else {
                holder.status.setVisibility(View.GONE);
            }
        }
        else {
            holder.status.setVisibility(View.GONE);
            holder.lastMessage.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MessageActivity.class);
                intent.putExtra("userID",users.getId());
                Log.d("user id" , users.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }



    public static class viewHolder extends RecyclerView.ViewHolder {
        TextView username,lastMessage;
        CircleImageView imageView, status;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.userName);
            lastMessage = itemView.findViewById(R.id.lastMessage);
            imageView = itemView.findViewById(R.id.circularImage);
            status = itemView.findViewById(R.id.statusImage);
        }
    }
    void getLastMessage(String sender , String receiver , final TextView last_msg  ){
        lastMessage = "empty";
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ChatData");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    messages messages = dataSnapshot.getValue(messages.class);
                    assert messages != null;
                    if ((messages.getReceiver().equals(sender) && messages.getSender().equals(receiver)) ||
                            (messages.getSender().equals(sender) && messages.getReceiver().equals(receiver))) {
                        lastMessage= messages.getMessage();
                    }
                }
                if (lastMessage.equals("empty")){
                    last_msg.setText(""); ;
                }
                else{
                    last_msg.setText(lastMessage);
                }
                lastMessage = "empty";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
