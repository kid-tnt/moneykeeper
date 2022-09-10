package com.quanlichitieu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import java.util.Calendar;

public class UpdateDeleteItem extends AppCompatActivity implements View.OnClickListener {
    public Spinner sp;
    private EditText eTitle,ePrice,eDate;
    private Button btUpdate,btBack,btDelete;
    private Item item;
    private String uid;
    private FirebaseDatabase database;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_delete_item);
        initView();
        btDelete.setOnClickListener(this);
        btUpdate.setOnClickListener(this);
        btBack.setOnClickListener(this);
        eDate.setOnClickListener(this);
        Intent intent=getIntent();
        item=(Item) intent.getSerializableExtra("item");
        eTitle.setText(item.getTitle());
        ePrice.setText(item.getPrice());
        eDate.setText(item.getDate());
        uid=FirebaseAuth.getInstance().getUid();
        database=FirebaseDatabase.getInstance();
        reference= database.getReference(uid+"/items/"+item.getKey());

        int p=0;
        for(int i=0;i<sp.getCount();i++){
            if(sp.getItemAtPosition(i).toString().equalsIgnoreCase(item.getCategory())){
                p=i;break;
            }
        }
        sp.setSelection(p);

    }

    private void initView() {
        sp=findViewById(R.id.spCatogory);
        eTitle=findViewById(R.id.tvTitle);
        ePrice=findViewById(R.id.tvPrice);
        eDate=findViewById(R.id.tvDate);
        btUpdate=findViewById(R.id.btUpdate);
        btBack=findViewById(R.id.btBack);
        btDelete=findViewById(R.id.btDelete);

        sp.setAdapter(new ArrayAdapter<String>(this,R.layout.item_spinner,getResources().getStringArray(R.array.Category)));
    }

    @Override
    public void onClick(View view) {

        if(view==eDate){
            final Calendar c=Calendar.getInstance();
            int year=c.get(Calendar.YEAR);
            int month=c.get(Calendar.MONTH);
            int day=c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dialog=new DatePickerDialog(UpdateDeleteItem.this, new DatePickerDialog.OnDateSetListener() {
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
        if(view ==btBack){
            finish();
        }
        if(view==btUpdate) {
            String t = eTitle.getText().toString();
            String p = ePrice.getText().toString();
            String c = sp.getSelectedItem().toString();
            String d = eDate.getText().toString();
            String key = item.getKey();
            Item i = new Item(t, c, p, d, key);
            reference.setValue(i, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                    Toast.makeText(getApplicationContext(),"Update successfull",Toast.LENGTH_LONG).show();
                    finish();
                }
            });


        }
        if(view==btDelete){
            AlertDialog.Builder build=new AlertDialog.Builder(view.getContext());
            build.setTitle("Confirm delete");
            build.setMessage("Are you sure delete this item?");
            build.setIcon(R.drawable.ic_launcher_background);

            build.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    reference.removeValue(new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                            Toast.makeText(getApplicationContext(),"Delete successfull",Toast.LENGTH_LONG).show();
                            finish();
                        }
                    });
                    finish();
                }
            });
            build.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            AlertDialog dialog=build.create();
            dialog.show();



        }

    }
}