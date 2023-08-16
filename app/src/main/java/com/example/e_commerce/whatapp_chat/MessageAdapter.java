package com.example.e_commerce.whatapp_chat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_commerce.R;
import com.example.e_commerce.whatapp_chat.ChatAppMessageModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MessageAdapter extends RecyclerView.Adapter {
    ArrayList<ChatAppMessageModel> arrayList;
    Context context;
    String recId;
    int SENDER_VIEW_TYPE =1;
    int RECEIVER_VIEW_TYPE =2;

    public MessageAdapter(ArrayList<ChatAppMessageModel> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    public MessageAdapter(ArrayList<ChatAppMessageModel> arrayList, Context context, String recId) {
        this.arrayList = arrayList;
        this.context = context;
        this.recId = recId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       if(viewType== SENDER_VIEW_TYPE){
           View view = LayoutInflater.from(context).inflate(R.layout.sample_sender,parent,false);
           return new SenderViewHolder(view);
       }else{
           View view = LayoutInflater.from(context).inflate(R.layout.sample_reciever,parent,false);
           return new RecieverViewHolder(view);
       }
    }

    @Override
    public int getItemViewType(int position) {
       if(arrayList.get(position).getuId().equals(FirebaseAuth.getInstance().getUid())){
           return SENDER_VIEW_TYPE;
       }else{
           return RECEIVER_VIEW_TYPE;
       }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            ChatAppMessageModel model = arrayList.get(position);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(context).setTitle("Delete").setMessage("Are you sure want to delete this message?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    String senderRoom = FirebaseAuth.getInstance().getUid()+recId;
                                    database.getReference().child("chats").child(senderRoom)
                                            .child(model.getMessageId()).setValue(null);
                                }
                            }).setNegativeButton("no", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
                }
            });

            if(holder.getClass()== SenderViewHolder.class){
                Date date = new Date(model.getTimestamp());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm:ss");
                String strDate = simpleDateFormat.format(date);
                ((SenderViewHolder) holder).senderTime.setText(strDate);

                ((SenderViewHolder) holder).senderMessage.setText(model.getMessage());
            }else{
                Date date = new Date(model.getTimestamp());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm:ss");
                String strDate = simpleDateFormat.format(date);

                ((RecieverViewHolder) holder).recieverTime.setText(strDate);
                ((RecieverViewHolder) holder).recieverMessage.setText(model.getMessage());
            }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class RecieverViewHolder extends RecyclerView.ViewHolder{
        TextView recieverMessage,recieverTime;

        public RecieverViewHolder(@NonNull View itemView) {
            super(itemView);
            recieverMessage = itemView.findViewById(R.id.recieverText);
            recieverTime = itemView.findViewById(R.id.recieverTime);
        }
    }

    public class SenderViewHolder extends RecyclerView.ViewHolder{
        TextView senderMessage,senderTime;
        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            senderMessage = itemView.findViewById(R.id.senderText);
            senderTime = itemView.findViewById(R.id.senderTime);
        }
    }
}
