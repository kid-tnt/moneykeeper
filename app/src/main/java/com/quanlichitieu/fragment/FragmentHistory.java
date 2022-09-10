package com.quanlichitieu.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.telephony.ims.RcsUceAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.quanlichitieu.R;
import com.quanlichitieu.UpdateDeleteItem;
import com.quanlichitieu.adapter.RecycleViewAdapter;
import com.quanlichitieu.model.Item;

import java.util.ArrayList;
import java.util.List;


public class FragmentHistory extends Fragment implements RecycleViewAdapter.ItemListener {

    private RecyclerView recyclerView;
    private RecycleViewAdapter adapter;
    private List<Item> mlistItems;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView=view.findViewById(R.id.recycleView);
        adapter=new RecycleViewAdapter(mlistItems);
        mlistItems=new ArrayList<>();
        getListItems();

        adapter.setList(mlistItems);
        LinearLayoutManager manager=new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        adapter.setItemListener(this);

    }

    private void getListItems() {
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        String uid= FirebaseAuth.getInstance().getUid();
        DatabaseReference reference=database.getReference(uid+"/items");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(mlistItems!=null){
                    mlistItems.clear();
                }
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    Item item= dataSnapshot.getValue(Item.class);
                    mlistItems.add(item);

                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
        getListItems();
        adapter.setList(mlistItems);

    }
}