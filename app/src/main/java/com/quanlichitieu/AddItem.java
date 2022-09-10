package com.quanlichitieu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.quanlichitieu.model.Item;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddItem extends AppCompatActivity implements View.OnClickListener{
    public Spinner sp;
    private EditText eTitle,ePrice,eDate;
    private Button btUpdate,btCancel;
    private String uid,key;
    private FirebaseDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        initView();
        btCancel.setOnClickListener(this);
        btUpdate.setOnClickListener(this);
        eDate.setOnClickListener(this);
        database=FirebaseDatabase.getInstance();
        uid= FirebaseAuth.getInstance().getUid();



    }

    private void initView() {
        sp=findViewById(R.id.spCatogory);
        eTitle=findViewById(R.id.tvTitle);
        ePrice=findViewById(R.id.tvPrice);
        eDate=findViewById(R.id.tvDate);
        btUpdate=findViewById(R.id.btUpdate);
        btCancel=findViewById(R.id.btCancel);
        sp.setAdapter(new ArrayAdapter<String>(this,R.layout.item_spinner,getResources().getStringArray(R.array.Category)));


    }

    @Override
    public void onClick(View view) {
        if(view==eDate){
            final Calendar c=Calendar.getInstance();
            int year=c.get(Calendar.YEAR);
            int month=c.get(Calendar.MONTH);
            int day=c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dialog=new DatePickerDialog(AddItem.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                    String date="";
                    if(m>8){
                        date=d+"/"+(m+1)+"/"+y;

                    }
                    else{
                        date=d+"/0"+(m+1)+"/"+y;
                    }
                    eDate.setText(date);

                }
            },year,month,day);
            dialog.show();
        }
        if(view==btCancel){
            finish();
        }
        if (view==btUpdate){
            String t=eTitle.getText().toString();
            String p=ePrice.getText().toString();
            String c=sp.getSelectedItem().toString();
            String d=eDate.getText().toString();
            //  if(!t.isEmpty() && p.matches("\\d")){
            Item i=new Item(t,c,p,d);
            //Update lên DB
          //  SQLiteHelper db=new SQLiteHelper(this);
         //   db.addItem(i);
            DatabaseReference reference=database.getReference(uid+"/items");
            key=reference.push().getKey();
            i.setKey(key);
            reference.child(key).setValue(i, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                    Toast.makeText(getApplicationContext(),"Add Successfull",Toast.LENGTH_LONG).show();
                    finish();
                }
            });


            //}
        }

    }
}