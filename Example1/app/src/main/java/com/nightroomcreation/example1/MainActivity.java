package com.nightroomcreation.example1;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.nightroomcreation.example1.adapter.CountdownAdapter;
import com.nightroomcreation.example1.model.Product;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Product> listProducts = new ArrayList<>();
    private CountdownAdapter adapter;

    private ImageView imgRefreshData;
    private ListView listItemTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = ((AppCompatActivity) MainActivity.this).getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        listItemTime = (ListView) findViewById(R.id.lvItems);
        imgRefreshData = (ImageView) findViewById(R.id.imgRefreshData);

        listProducts.clear();

        adapter = new CountdownAdapter(MainActivity.this, listProducts);
        listItemTime.setAdapter(adapter);

        imgRefreshData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listProducts.clear();

                adapter = new CountdownAdapter(MainActivity.this, listProducts);
                listItemTime.setAdapter(adapter);
            }
        });
    }
}
