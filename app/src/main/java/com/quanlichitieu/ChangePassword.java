package com.quanlichitieu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePassword extends AppCompatActivity {
    private EditText npass;
    private Button btchange;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        npass=findViewById(R.id.newPassword);
        btchange=findViewById(R.id.btChangePassword);
        progressDialog=new ProgressDialog(this);
        btchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newstrP=npass.getText().toString().trim();
                progressDialog.show();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                user.updatePassword(newstrP)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(),"Password change successfull",Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();

                                }
                            }
                        });


            }
        });
    }
}