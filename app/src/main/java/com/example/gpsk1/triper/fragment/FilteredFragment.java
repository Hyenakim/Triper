package com.example.gpsk1.triper.fragment;

import android.app.ActivityOptions;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gpsk1.triper.FilterActivity;
import com.example.gpsk1.triper.R;
import com.example.gpsk1.triper.chat.MessageActivity;
import com.example.gpsk1.triper.model.GuideModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class FilteredFragment extends Fragment {
    private String place;
    private String language;
    private FloatingActionButton fab;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_people, container, false);
        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.peoplefragment_recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        recyclerView.setAdapter(new FilteredFragment.FilteredFragmentRecyclerViewAdapter());

        Bundle bundle = getArguments();
        if(bundle != null){
            place = bundle.getString("placeTo");
            language = bundle.getString("lanTo");


        }

        fab = view.findViewById(R.id.peoplefragment_floatingButton);
        return view;
    }

    class FilteredFragmentRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        List<GuideModel> filterModel;

        public FilteredFragmentRecyclerViewAdapter(){
            filterModel = new ArrayList<>();
            ValueEventListener guide = FirebaseDatabase.getInstance().getReference().child("Guide").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    filterModel.clear();

                    for( DataSnapshot snapshot : dataSnapshot.getChildren() ){

                            filterModel.add(snapshot.getValue(GuideModel.class)); //리스트에 추가

                            if(place == null  && language != null ){ // 언어 조건만 존재 할 때

                                GuideModel tmp = filterModel.get(filterModel.size() - 1); // 가장 최근에 추가된 리스트 값
                                if( ! tmp.language1.toString().equals(language) ){
                                    if(tmp.language2 == null){
                                        filterModel.remove(filterModel.size() - 1); // 언어가 다르면 삭제
                                    }
                                    else{
                                        if( !tmp.language2.toString().equals(language) ){
                                            filterModel.remove(filterModel.size() - 1); // 언어가 다르면 삭제
                                        }
                                    }
                                }
                            }
                            else if( place != null && language == null){ // 장소 조건만 존재 할 때
                                GuideModel tmp = filterModel.get(filterModel.size() - 1); // 가장 최근에 추가된 리스트 값
                                if( !tmp.place.toString().equals(place) ) {
                                    filterModel.remove(filterModel.size() - 1); // 장소가 다르면 삭제
                                }
                            }
                            else if( place != null && language != null){ // 장소 언어 조건 모두 존재할 때
                                GuideModel tmp = filterModel.get(filterModel.size() - 1); // 가장 최근에 추가된 리스트 값
                                if( !tmp.place.toString().equals(place) ) {
                                    if( ! tmp.language1.toString().equals(language) ){
                                        if(tmp.language2 == null){
                                            filterModel.remove(filterModel.size() - 1); // 언어가 다르면 삭제
                                        }
                                        else{
                                            if( !tmp.language2.toString().equals(language) ){
                                                filterModel.remove(filterModel.size() - 1); // 언어가 다르면 삭제
                                            }
                                        }
                                    }
                                }
                            }
                    } // for 끝
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
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
//이미지 올리기

//            Glide.with(holder.itemView.getContext())
//                    .load(userModels.get(position).profilImageUrl)
//                    .apply(new RequestOptions().circleCrop())
//                    .into(((CustomViewHolder)holder).imageView);

            //텍스트 올리기
            ((CustomViewHolder)holder).textView.setText(filterModel.get(position).guideName);
            ((CustomViewHolder)holder).placeTV.setText(filterModel.get(position).place);
            ((CustomViewHolder)holder).lan1TV.setText(filterModel.get(position).language1);
            ((CustomViewHolder)holder).lan2TV.setText(filterModel.get(position).language2);

                /* 클릭 시 채팅방 이동 */
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), MessageActivity.class);
                        intent.putExtra("destinationUid", filterModel.get(position).uid);
                        ActivityOptions activityOptions = ActivityOptions.makeCustomAnimation(v.getContext(), R.anim.fromright, R.anim.toleft);
                        startActivity(intent, activityOptions.toBundle());
                    }
                });

                /* 필터 버튼 클릭 */
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), FilterActivity.class);
                        startActivityForResult(intent, 3000);
                    }
                });
        }

        @Override
        public int getItemCount() {
            return filterModel.size();
        }
        private class CustomViewHolder extends RecyclerView.ViewHolder {
            public ImageView imageView;
            public TextView textView;
            public TextView placeTV;
            public TextView lan1TV;
            public TextView lan2TV;

            public CustomViewHolder(View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.frienditem_imageview);
                textView = itemView.findViewById(R.id.frienditem_textview);
                placeTV = itemView.findViewById(R.id.frienditem_place_textview);
                lan1TV = itemView.findViewById(R.id.frienditem_lan1_textview);
                lan2TV = itemView.findViewById(R.id.frienditem_lan2_textview);

            }
        }
    }

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
