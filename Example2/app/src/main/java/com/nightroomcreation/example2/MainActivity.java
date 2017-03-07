package com.nightroomcreation.example2;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.nightroomcreation.example2.adapter.CountdownAdapter;
import com.nightroomcreation.example2.model.Product;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ImageView imgRefreshData;
    private RecyclerView rvTimer;

    private List<Product> listProduct = new ArrayList<>();
    private CountdownAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = ((AppCompatActivity) MainActivity.this).getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        imgRefreshData = (ImageView) findViewById(R.id.imgRefreshData);
        rvTimer = (RecyclerView) findViewById(R.id.rvTimer);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this
                , LinearLayoutManager.VERTICAL
                , false);
        rvTimer.setLayoutManager(linearLayoutManager);

        listProduct.clear();
        adapter = new CountdownAdapter(MainActivity.this, listProduct);

        rvTimer.setAdapter(adapter);

        imgRefreshData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listProduct.clear();

                adapter = new CountdownAdapter(MainActivity.this, listProduct);
                rvTimer.setAdapter(adapter);
            }
        });

    }
}
