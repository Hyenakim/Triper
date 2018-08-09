package com.example.gpsk1.triper.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.gpsk1.triper.R;
import com.example.gpsk1.triper.model.ChatModel;
import com.example.gpsk1.triper.model.GuideModel;
import com.example.gpsk1.triper.model.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ChatFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat,container,false);

        RecyclerView recyclerView =(RecyclerView)view.findViewById(R.id.chatfragment_recyclerview);
        recyclerView.setAdapter(new ChatRecyclerViewAdapter());
        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));

        return view;
    }

    class ChatRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        private List<ChatModel> chatModels = new ArrayList<>();
        private String uid;
        public ChatRecyclerViewAdapter() {
            uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

            FirebaseDatabase.getInstance().getReference().child("chatrooms").orderByChild("users/"+uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    chatModels.clear();
                    for(DataSnapshot item : dataSnapshot.getChildren()){
                        chatModels.add(item.getValue(ChatModel.class));
                    }
                    notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat,parent,false);

            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            final CustomViewHolder customViewHolder = (CustomViewHolder)holder;
            String destinationUid = null;

            // 일일이 챗방에 있는 유저를 체크
            for(String user : chatModels.get(position).users.keySet()){
                if(!user.equals(uid)){ // 내가 아닌 사람
                    destinationUid = user;

                }
            }
            // destination이 누군지
            FirebaseDatabase.getInstance().getReference().child("Guide").child(destinationUid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                   UserModel userModel = dataSnapshot.getValue(UserModel.class);
                    /*Glide.with(customViewHolder.itemView.getContext())
                            .load(userModel.profieImageUrl)
                            .apply(new RequestOptions().circleCrop())
                            .into(customViewHolder.imageView); */

                    customViewHolder.textView_title.setText(userModel.userName); // 상대방 이름 = 채팅방 이름
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            // 채팅방 마지막 메시지 : 메시지를 내림 차순으로 정렬 후 마지막 메시지의 키값을 가져옴
            Map<String, ChatModel.Comment> commentMap = new TreeMap<>(Collections.<String>reverseOrder());
            commentMap.putAll(chatModels.get(position).comments);
            String lastMessageKey = (String) commentMap.keySet().toArray()[0];
            customViewHolder.textView_last_message.setText(chatModels.get(position).comments.get(lastMessageKey).message);
        }

        @Override
        public int getItemCount() {
            return chatModels.size();
        }

        private class CustomViewHolder extends RecyclerView.ViewHolder {
            public ImageView imageView;
            public TextView textView_title;
            public TextView textView_last_message;

            public CustomViewHolder(View view) {
                super(view);

                imageView = (ImageView) view.findViewById(R.id.chatitem_imageview);
                textView_title = (TextView) view.findViewById(R.id.chatitem_textview_title);
                textView_last_message = (TextView)view.findViewById(R.id.chatitem_textview_lastMessage);
            }
        }
    }
}
