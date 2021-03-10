package edu.rentals.frontend;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class StoreListAdapter extends RecyclerView.Adapter<StoreListAdapter.ViewHolder> {
    private StoreList storeList;
    private OnStoreListener mOnStoreListener;

    StoreListAdapter(StoreList list, OnStoreListener mOnStoreListener) {
        this.storeList = list;
        this.mOnStoreListener = mOnStoreListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView storeName;
        TextView storeAddress;
        TextView storeCategory;
        OnStoreListener onStoreListener;
        
        ViewHolder(View storeView, OnStoreListener onStoreListener) {
            super(storeView);
            storeName = storeView.findViewById(R.id.store_name);
            storeAddress = storeView.findViewById(R.id.store_address);
            storeCategory = storeView.findViewById(R.id.store_category);
            this.onStoreListener = onStoreListener;

            storeView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            System.out.println("Clicked in StoreListAdapter!");

            System.out.println("Inside the onStoreClicked :" + storeList.storeList.size());
            Store selectedStore = storeList.storeList.get(getAdapterPosition());
            System.out.println("Selected: ");
            System.out.println(selectedStore.getStoreName());
            System.out.println("Store ID is : " + selectedStore.getStoreId());

            onStoreListener.onStoreClicked(selectedStore.getStoreId());

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.store_row, parent, false);
        return new ViewHolder(view, mOnStoreListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        StoreList list = storeList;
        viewHolder.storeName.setText(list.storeList.get(position).getStoreName());
        viewHolder.storeAddress.setText(list.storeList.get(position).getStoreAddress());
        viewHolder.storeCategory.setText(list.storeList.get(position).getStoreCategory());

    }

    @Override
    public int getItemCount() { return storeList.storeList.size(); }

}
