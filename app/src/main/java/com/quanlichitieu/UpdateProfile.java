package com.quanlichitieu;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Magnifier;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateProfile extends AppCompatActivity {
    private CircleImageView imgavatar;
    private EditText ename;
    private EditText eemail;
    private Button btUpdateInformation;
    private Button btUpdateEmail;
    private ProgressDialog progressDialog;
    public static final int REQUEST_CODE=10;
    private Uri muri;
 private ActivityResultLauncher<Intent> mactivityResultLauncher=registerForActivityResult(
         new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
             @Override
             public void onActivityResult(ActivityResult result) {
                 if(result.getResultCode()==RESULT_OK){
                     Intent intent=result.getData();
                     if(intent==null){
                         return;
                     }
                     muri=intent.getData();
                     try {
                         Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),muri);
                         imgavatar.setImageBitmap(bitmap);

                     } catch (IOException e) {
                         e.printStackTrace();
                     }
                 }

             }
         }
 );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        ename=findViewById(R.id.eFullname);
        eemail=findViewById(R.id.eEmail);
        imgavatar=findViewById(R.id.imgavatar);
        btUpdateInformation=findViewById(R.id.btUpdateProfile);
        btUpdateEmail=findViewById(R.id.btUpdateEmail);

        showInformation();
        initListener();

    }



    private void showInformation(){
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        if(user==null){
            return;
        }
        String name = user.getDisplayName();
        String email = user.getEmail();
        Uri photoUrl = user.getPhotoUrl();

            ename.setText(name);
           eemail.setText(email);
        Glide.with(this).load(photoUrl).error(R.drawable.profile_image).into(imgavatar);




    }
    private void initListener() {
        imgavatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickRequestPermission();
            }
        });
        btUpdateInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateInfo();
            }
        });
        btUpdateEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upDateEmail();
            }
        });

    }

    private void upDateEmail() {
        String strEmail=eemail.getText().toString().trim();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        progressDialog.setMessage("Registration in progress");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        user.updateEmail(strEmail)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),"Email update successfull!",Toast.LENGTH_SHORT).show();
                            showInformation();
                            progressDialog.dismiss();

                        }
                    }
                });
    }


    private void onClickRequestPermission() {
        //check verion
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.M){
            openGallery();
            return;

        }
        if(this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
            openGallery();
        }
        else {
            String []permisions={Manifest.permission.READ_EXTERNAL_STORAGE};
            this.requestPermissions(permisions,REQUEST_CODE);

        }

    }

    private void openGallery() {
        Intent intent=new Intent();
        intent.setType("image/");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        mactivityResultLauncher.launch(Intent.createChooser(intent,"Select Picture"));



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==REQUEST_CODE){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                openGallery();
            }
            else {
                return;
            }
        }
    }
    private void updateInfo() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user ==null){
            return;
        }
        String uname=ename.getText().toString().trim();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(uname)
                .setPhotoUri(muri)
                .build();
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Update in progress");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),"Update profile successfull",Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();

                        }
                    }
                });
    }

}