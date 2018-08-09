package com.example.gpsk1.triper.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.gpsk1.triper.MainActivity;
import com.example.gpsk1.triper.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class AccountFragment extends Fragment{
    private Button logout;
    private ImageView profile;
    private FirebaseUser user;
    private Uri photoUrl;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account,container,false);

        profile = (ImageView)view.findViewById(R.id.fragment_account_imageView);
        logout = (Button)view.findViewById(R.id.fragment_account_button_logout);

        user = FirebaseAuth.getInstance().getCurrentUser();
        photoUrl = user.getPhotoUrl();
        




        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(getActivity(), "로그아웃 완료", Toast.LENGTH_LONG).show();
                AccountFragment.this.getActivity().finish();
                startActivity(new Intent(AccountFragment.this.getActivity(), MainActivity.class));
            }
        });

        return view;
    }
}
