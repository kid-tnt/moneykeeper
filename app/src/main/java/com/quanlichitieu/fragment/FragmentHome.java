package com.quanlichitieu.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.quanlichitieu.R;
import com.quanlichitieu.UpdateDeleteItem;
import com.quanlichitieu.adapter.RecycleViewAdapter;
import com.quanlichitieu.model.Item;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class FragmentHome extends Fragment implements RecycleViewAdapter.ItemListener {
    private RecyclerView recyclerView;
    private RecycleViewAdapter adapter;
    private TextView tvTong;
    private List<Item> itemList;
    int tong;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView=view.findViewById(R.id.recycleView);
        tvTong=view.findViewById(R.id.tvTong);
        tong=0;
        itemList=new ArrayList<>();
        adapter=new RecycleViewAdapter(itemList);

        adapter.setList(itemList);
        LinearLayoutManager manager=new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        adapter.setItemListener(this);
        getItemFromRB();
        tvTong.setText("Tong: "+tong);

    }


    private void getItemFromRB() {

        FirebaseDatabase database=FirebaseDatabase.getInstance();
        String uid= FirebaseAuth.getInstance().getUid();
        DatabaseReference reference=database.getReference(uid+"/items");

        reference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tong=0;
                Date d=new Date();
                SimpleDateFormat f =new SimpleDateFormat("dd/MM/yyyy");
                if(itemList!=null){
                    itemList.clear();
                }
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    Item item= dataSnapshot.getValue(Item.class);
                    if(item.getDate().equals(f.format(d))){
                        itemList.add(item);
                        tong+=Integer.parseInt(item.getPrice());
                    }



                }
                tvTong.setText("Tong: "+tong);

                adapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //Toast.makeText(getContext(),"Error when fetch data",Toast.LENGTH_LONG).show();

            }
        });

    }


    @Override
    public void onItemClick(View view, int position) {

        Item item= adapter.getItem(position);
        Intent intent=new Intent(getActivity(), UpdateDeleteItem.class);
        intent.putExtra("item",item);
        startActivity(intent);

    }

    @Override
    public void onResume() {
        super.onResume();
        getItemFromRB();
       // tvTong.setText("Tong: "+tong);
        adapter.setList(itemList);

    }
}