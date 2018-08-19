package com.example.gpsk1.triper.fragment;

import android.app.ActivityOptions;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.gpsk1.triper.FilterActivity;
import com.example.gpsk1.triper.Main2Activity;
import com.example.gpsk1.triper.R;
import com.example.gpsk1.triper.chat.MessageActivity;
import com.example.gpsk1.triper.model.GuideModel;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class PeopleFragment extends Fragment {
    private int mode;
    private FloatingActionButton floatButton;
    private String place;
    private String language;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        /* Main2Activity에서 값 받아오기 */
        mode = ((Main2Activity)getActivity()).getMode();

        View view = inflater.inflate(R.layout.fragment_people,container,false);
        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.peoplefragment_recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        recyclerView.setAdapter(new PeopleFragmentRecyclerViewAdapter());

        floatButton = (FloatingActionButton)view.findViewById(R.id.peoplefragment_floatingButton);
        return view;
    }

    class PeopleFragmentRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        List<GuideModel> userModels;

        public PeopleFragmentRecyclerViewAdapter(){
            userModels = new ArrayList<>();
            ValueEventListener guide = FirebaseDatabase.getInstance().getReference().child("Guide").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    userModels.clear(); //누적된 데이터 클리어
                    String tmpUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        /* 관광객 모드일 때는 검색 조건에 맞게 가이드 목록 띄우기*/
                        if(mode == 0){
                            userModels.add(snapshot.getValue(GuideModel.class)); //리스트에 추가
                        } // 관광객 모드 if 끝

                        /* 가이드 모드일 때는 자신을 제외한 가이드 목록만 뛰우기*/
                        else {
                            userModels.add(snapshot.getValue(GuideModel.class)); //리스트에 추가
                            GuideModel tmp = userModels.get(userModels.size() - 1); // 가장 최근에 추가된 리스트 값
                            if (tmp.uid.toString().equals(tmpUid)) {
                                userModels.remove(userModels.size()-1); }
                        } // if 끝

                    } // for문 끝

                    notifyDataSetChanged(); //새로고침
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend,parent,false);

            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

            FirebaseStorage.getInstance().getReference().child("userImages").child(userModels.get(position).uid).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(holder.itemView.getContext()).load(uri).apply(new RequestOptions().circleCrop()).into(((CustomViewHolder)holder).imageView);
                }
            });
            //이미지 올리기

//            Glide.with(holder.itemView.getContext())
//                    .load(userModels.get(position).profilImageUrl)
//                    .apply(new RequestOptions().circleCrop())
//                    .into(((CustomViewHolder)holder).imageView);

            //텍스트 올리기
            ((CustomViewHolder)holder).textView.setText(userModels.get(position).guideName);
            ((CustomViewHolder)holder).placeTV.setText(userModels.get(position).place);
            ((CustomViewHolder)holder).lan1TV.setText(userModels.get(position).language1);
            ((CustomViewHolder)holder).lan2TV.setText(userModels.get(position).language2);

            if(mode == 0){ // 관광객 모드일 때만 클릭 기능 부여
                /* 클릭 시 채팅방 이동 */
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), MessageActivity.class);
                    intent.putExtra("destinationUid", userModels.get(position).uid);
                    ActivityOptions activityOptions = ActivityOptions.makeCustomAnimation(v.getContext(), R.anim.fromright, R.anim.toleft);
                    startActivity(intent, activityOptions.toBundle());
                }
            });

                /* 필터 버튼 클릭 */
                floatButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), FilterActivity.class);
                        startActivityForResult(intent, 3000);

                    }
                });


            } // if 끝

        }



        @Override
        public int getItemCount() {
            return userModels.size();
        }

        private class CustomViewHolder extends RecyclerView.ViewHolder {
            public ImageView imageView;
            public TextView textView;
            public TextView placeTV;
            public TextView lan1TV;
            public TextView lan2TV;

            public CustomViewHolder(View view) {
                super(view);
                imageView = (ImageView)view.findViewById(R.id.frienditem_imageview);
                textView = (TextView)view.findViewById(R.id.frienditem_textview);
                placeTV = (TextView)view.findViewById(R.id.frienditem_place_textview);
                lan1TV = (TextView)view.findViewById(R.id.frienditem_lan1_textview);
                lan2TV = (TextView)view.findViewById(R.id.frienditem_lan2_textview);

            }
        }
    } //클래스 끝

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
       super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == 3000){
            place = data.getStringExtra("place");
            language = data.getStringExtra("language");

            Bundle bundle = new Bundle();
            bundle.putString("placeTo", place);
            bundle.putString("lanTo", language);

            Fragment fragment = new FilteredFragment();
            fragment.setArguments(bundle);

           getFragmentManager().beginTransaction().replace(R.id.main2activity_framelayout, fragment).addToBackStack(null).commit();

        }
    }
}
