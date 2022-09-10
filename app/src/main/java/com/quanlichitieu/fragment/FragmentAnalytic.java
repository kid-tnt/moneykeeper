package com.quanlichitieu.fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.quanlichitieu.R;
import com.quanlichitieu.model.Item;

import java.util.ArrayList;
import java.util.List;


public class FragmentAnalytic extends Fragment {
    private PieChart pieChart;
    private int an,mua,di,nha,dien,nuoc,tong;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_analytic, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pieChart=view.findViewById(R.id.pieChart);

        getData();
        configurePiechart();
        loadPieChar();

    }

    private void configurePiechart() {
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelTextSize(15);
        pieChart.setCenterText(tong+"");
        pieChart.setCenterTextColor(Color.BLACK);
        pieChart.setCenterTextSize(20);
        pieChart.getDescription().setEnabled(false);

        Legend l=pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setEnabled(true);
        l.setDrawInside(false);


    }

    private void loadPieChar() {
        ArrayList<PieEntry> entries=new ArrayList<>();
        if(an!=0){
            entries.add(new PieEntry(an,"Ăn uống"));
        }
        if(mua!=0){
            entries.add(new PieEntry(mua,"Mua sắm"));
        }
        if(di!=0){
            entries.add(new PieEntry(di,"Đi lại"));
        }
        if(nha !=0){
            entries.add(new PieEntry(nha,"Tiền nhà"));
        }
        if(dien!=0){
            entries.add(new PieEntry(dien,"Tiền điện"));
        }
        if(nuoc!=0){
            entries.add(new PieEntry(nuoc,"Tiền nước"));
        }






        PieDataSet dataSet=new PieDataSet(entries,"Category");
        ArrayList<Integer> colors=new ArrayList<>();
        for(int color: ColorTemplate.MATERIAL_COLORS){
            colors.add(color);
        }
        for(int color: ColorTemplate.COLORFUL_COLORS){
            colors.add(color);
        }
        dataSet.setColors(colors);
        PieData data=new PieData(dataSet);
        data.setDrawValues(true);
        data.setValueTextSize(15);
        data.setValueTextColor(Color.WHITE);
        data.setValueFormatter(new PercentFormatter(pieChart));
        pieChart.setData(data);
        pieChart.invalidate();



    }

    private void getData() {
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        String uid= FirebaseAuth.getInstance().getUid();
        DatabaseReference reference=database.getReference(uid+"/items");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                an=0;
               mua=0;
               di=0;
               nha=0;
               dien=0;
               nuoc=0;
               tong=0;

                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    Item item= dataSnapshot.getValue(Item.class);
                    tong+=Integer.parseInt(item.getPrice());
                    if(item.getCategory().equals("Ăn uống")){
                        an+=Integer.parseInt(item.getPrice());

                    }
                    if(item.getCategory().equals("Đi lại")){
                        di+=Integer.parseInt(item.getPrice());

                    }
                    if(item.getCategory().equals("Mua sắm")){
                        mua+=Integer.parseInt(item.getPrice());

                    }
                    if(item.getCategory().equals("Tiền nhà")){
                        nha+=Integer.parseInt(item.getPrice());

                    }if(item.getCategory().equals("Tiền nước")){
                        nuoc+=Integer.parseInt(item.getPrice());

                    }
                    if(item.getCategory().equals("Tiền điện")){
                        dien+=Integer.parseInt(item.getPrice());

                    }


                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
        configurePiechart();
        loadPieChar();

    }
}