package com.nightroomcreation.example2.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nightroomcreation.example2.R;
import com.nightroomcreation.example2.model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 18-Feb-17.
 */

public class CountdownAdapter extends RecyclerView.Adapter<CountdownAdapter.ViewHolderCountdownAdapter> {

    private List<Product> listProducts;
    private List<ViewHolderCountdownAdapter> listHolder;
    private Context context;
    private Handler handler = new Handler();

    private boolean isFirstInit = false;

    private int VIEW_TYPE_FOOTER = 1;
    private int VIEW_TYPE_ROW = 0;
    private int nameOfAdapter = 2;

    //constructor here
    public CountdownAdapter(Context context, List<Product> listProducts) {
        this.listProducts = listProducts;
        this.context = context;

        listHolder = new ArrayList<>();
        isFirstInit = true;
    }

    @Override
    public ViewHolderCountdownAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.e("viewType", "onCreateViewHolder: view type " + viewType);

        int resLayout = R.layout.item_list;
        if (viewType == VIEW_TYPE_FOOTER) {
            resLayout = R.layout.item_list_add;
        }

        View view = LayoutInflater.from(parent.getContext()).inflate(resLayout, parent, false);
        ViewHolderCountdownAdapter holder = new ViewHolderCountdownAdapter(view);
        holder.setViewType(viewType);
        holder.initializeView();

        view.setTag(holder);
        if (viewType == VIEW_TYPE_ROW) {
            synchronized (listHolder) {
                listHolder.add(holder);
            }
        }

        return holder;
    }

    //this method gonna called frequently
    @Override
    public void onBindViewHolder(final ViewHolderCountdownAdapter holder, int position) {
        Log.e("viewType", "onBindViewHolder: position " + position
                + "\nviewtype " + holder.getItemViewType()
                + "\nview type on position " + getItemViewType(position));

        if (holder.getItemViewType() == VIEW_TYPE_ROW) {

            holder.setData(listProducts.get(position));
            startUpdateTimer();
        }

        if (listProducts.size() == 0 && isFirstInit) {
            holder.initializeData(true);
            isFirstInit = false;
        }

    }

    @Override
    public int getItemCount() {
        return listProducts.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        Log.e("viewType", "getItemViewType: position " + position);
        return (position == listProducts.size()) ? VIEW_TYPE_FOOTER : VIEW_TYPE_ROW;
    }

    //CUSTOM METHOD HERE
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
    class ViewHolderCountdownAdapter extends RecyclerView.ViewHolder {
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
            super(view);
            this.view = view;
        }

        //set the view type of adapter
        public void setViewType(int viewType) {
            this.viewType = viewType;
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
                    initializeData(false);
                }
            });

            imgAddData.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    initializeData(false);
                }
            });

            layoutAddData.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    initializeData(false);
                }
            });
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

        void initializeData(boolean isFirstInit) {

            if (isFirstInit) {
                listProducts.add(new Product(
                        "" + ((char) (65))
                        , System.currentTimeMillis() + (nameOfAdapter * 30000)));
                listProducts.add(new Product(
                        "" + ((char) (66))
                        , System.currentTimeMillis() + ((nameOfAdapter + 1) * 30000)));
            } else {
                listProducts.add(new Product(
                        "" + ((char) (nameOfAdapter + 65))
                        , System.currentTimeMillis() + (nameOfAdapter * 30000)));

                nameOfAdapter++;
            }

            Handler handler = new Handler();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    /*notifyItemInserted(listProducts.size() - 1);*/
                    notifyItemRangeChanged(0, listProducts.size());
                }
            };
            handler.post(runnable);
        }
    }
}
