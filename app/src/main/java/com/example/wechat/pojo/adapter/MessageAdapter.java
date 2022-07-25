package com.example.wechat.pojo.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wechat.R;
import com.example.wechat.model.messages;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.viewHolder> {
    private final int msg_sent_id = 0;
    private final int msg_received_id = 1;
    private List<messages> arrayList;
    Context context;
    FirebaseUser firebaseUser;
    public MessageAdapter (Context context, List<messages> arrayList){
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == msg_sent_id) {
            return new viewHolder(LayoutInflater.from(context).inflate(R.layout.msg_sent_item, parent, false));
        } else {
            return new viewHolder(LayoutInflater.from(context).inflate(R.layout.msg_received_item, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        messages messages = arrayList.get(position);
        holder.message.setText(messages.getMessage());
        if(position == arrayList.size()-1) {

            if (messages.isMessageStatus()) {
               holder.messageState.setText("Seen");
            } else {
                holder.messageState.setText("Delivered");
            }
        }
            else  {
                holder.messageState.setVisibility(View.GONE);
            }
        }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {
        TextView message,messageState;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.message);
            messageState =(TextView) itemView.findViewById(R.id.messageState);

        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Log.d("user name" , firebaseUser.getUid());
        if(arrayList.get(position).getSender().equals(firebaseUser.getUid())){
           return msg_sent_id;
        }
        else{
            return msg_received_id;
        }
    }
}
