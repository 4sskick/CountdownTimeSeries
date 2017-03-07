package com.nightroomcreation.example1.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nightroomcreation.example1.R;
import com.nightroomcreation.example1.model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 18-Feb-17.
 */

public class CountdownAdapter extends ArrayAdapter<Product> {

    private List<ViewHolderCountdownAdapter> listHolder;
    private List<Product> listProducts;

    private Handler handler = new Handler();

    private int VIEW_TYPE_ROW = 0;
    private int VIEW_TYPE_FOOTER = 1;
    private int nameOfAdapter = 1;

    private boolean isFirstInit = false;

    //creating constructor
    public CountdownAdapter(Context context, List<Product> listProducts) {
        super(context, 0, listProducts);

        this.listProducts = listProducts;
        listHolder = new ArrayList<>();
        isFirstInit = true;
        initializeData(isFirstInit);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Log.e("viewType", "getView: " + getItemViewType(position)
                + "\nposition " + position);

        int resLayout = R.layout.item_list;
        if (getItemViewType(position) == VIEW_TYPE_FOOTER) {
            resLayout = R.layout.item_list_add;
        }

        ViewHolderCountdownAdapter holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(resLayout, parent, false);

            holder = new ViewHolderCountdownAdapter(convertView);
            holder.setViewType(getItemViewType(position));
            holder.initializeView();

            convertView.setTag(holder);

            //this gonna prevent the layout is not same with list add to holder list
            if (getItemViewType(position) != VIEW_TYPE_FOOTER) {
                synchronized (listHolder) {
                    listHolder.add(holder);
                }
            }
        } else {
            holder = (ViewHolderCountdownAdapter) convertView.getTag();
        }

        if (getItemViewType(position) != VIEW_TYPE_FOOTER) {
            holder.setData(getItem(position));
            startUpdateTimer();
        }

        return convertView;
    }

    @Override
    public int getCount() {
        return listProducts.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == listProducts.size()) ? VIEW_TYPE_FOOTER : VIEW_TYPE_ROW;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    //CUSTOM METHOD HERE
    private void initializeData(boolean isFirstInit) {

        if (isFirstInit) {
            listProducts.add(new Product(
                    "" + ((char) (65))
                    , System.currentTimeMillis() + (nameOfAdapter * 30000)));
            listProducts.add(new Product(
                    "" + ((char) (66))
                    , System.currentTimeMillis() + ((nameOfAdapter + 1) * 30000)));

            this.isFirstInit = false;
        } else {
            listProducts.add(new Product(
                    "" + ((char) (nameOfAdapter + 65))
                    , System.currentTimeMillis() + (nameOfAdapter * 30000)));

            nameOfAdapter++;
        }

        notifyDataSetChanged();
    }

    //start to counting time
    private void startUpdateTimer() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        synchronized (listHolder) {
                            long currentTime = System.currentTimeMillis();
                            for (ViewHolderCountdownAdapter holder : listHolder) {
                                holder.updateTimeRemaining(currentTime);
                            }
                        }
                    }
                });
            }
        }, 1000, 1000);
    }

    //HOLDER CLASS OF VIEW ITEM LIST
    private class ViewHolderCountdownAdapter {
        TextView txtTvProduct;
        TextView txtTvTimeRemaining;
        TextView txtAddData;
        ImageView imgAddData;
        View view;
        LinearLayout layoutAddData;

        Product product;

        int viewType;

        //CONSTRUCTOR HERE
        public ViewHolderCountdownAdapter(View view) {
            this.view = view;
        }

        //declare and initialize based on what type of the view on adapter
        private void initializeView() {
            if (viewType == VIEW_TYPE_FOOTER) {
                initializeFooter();
            }
            if (viewType == VIEW_TYPE_ROW) {
                initalizeRow();
            }
        }

        private void initalizeRow() {
            txtTvProduct = (TextView) view.findViewById(R.id.tvProduct);
            txtTvTimeRemaining = (TextView) view.findViewById(R.id.tvTimeRemaining);
        }

        private void initializeFooter() {
            layoutAddData = (LinearLayout) view.findViewById(R.id.layoutAddData);
            txtAddData = (TextView) view.findViewById(R.id.txtAddData);
            imgAddData = (ImageView) view.findViewById(R.id.imgAddData);

            txtAddData.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    initializeData(isFirstInit);
                }
            });

            imgAddData.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    initializeData(isFirstInit);
                }
            });

            layoutAddData.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    initializeData(isFirstInit);
                }
            });
        }

        //set the view type of adapter
        public void setViewType(int viewType) {
            this.viewType = viewType;
        }

        public void setData(Product product) {
            this.product = product;
            txtTvProduct.setText(product.name);

            updateTimeRemaining(System.currentTimeMillis());
        }

        private void updateTimeRemaining(long currentTime) {
            long timeDiff = product.expirationTime - currentTime;
            if (timeDiff > 0) {
                int seconds = (int) (timeDiff / 1000) % 60;
                int minutes = (int) ((timeDiff / (1000 * 60)) % 60);
                int hours = (int) ((timeDiff / (1000 * 60 * 60)) % 60);

                txtTvTimeRemaining.setText(hours + " hrs " + minutes + " mins " + seconds + " secs");
            } else {
                txtTvTimeRemaining.setText("Expired Time Remaining");
            }
        }
    }
}
