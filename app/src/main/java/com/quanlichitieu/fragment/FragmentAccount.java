package com.quanlichitieu.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.quanlichitieu.ChangePassword;
import com.quanlichitieu.LoginActivity;
import com.quanlichitieu.R;
import com.quanlichitieu.UpdateProfile;

import de.hdodenhof.circleimageview.CircleImageView;

public class FragmentAccount extends Fragment {
    private CircleImageView imageView;
    private TextView tvname;
    private TextView tvemail;
    private Button btLogout;
    private Button btUpdateInfo;
    private Button btChange;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageView=view.findViewById(R.id.img_avatar);
        tvname=view.findViewById(R.id.tvname);
        tvemail=view.findViewById(R.id.tvemail);
        showInformation();
        btLogout=view.findViewById(R.id.btLogout);
        btUpdateInfo=view.findViewById(R.id.btUpdate);
        btChange=view.findViewById(R.id.btChange);
        btLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent=new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();

            }
        });
        btUpdateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), UpdateProfile.class);
                startActivity(intent);

            }
        });
        btChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), ChangePassword.class);
                startActivity(intent);

            }
        });




    }
    private void showInformation(){
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        if(user==null){
            return;
        }
        String name = user.getDisplayName();
        String email = user.getEmail();
        Uri photoUrl = user.getPhotoUrl();

        if(name==null){
            tvname.setVisibility(View.GONE);
        }
        else{
            tvname.setVisibility(View.VISIBLE);
            tvname.setText(name);
        }
        tvemail.setText(email);
        Glide.with(this).load(photoUrl).error(R.drawable.profile_image).into(imageView);




    }

    @Override
    public void onResume() {
        super.onResume();
        showInformation();
    }
}