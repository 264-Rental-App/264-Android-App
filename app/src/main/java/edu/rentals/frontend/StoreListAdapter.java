package edu.rentals.frontend;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class StoreListAdapter extends RecyclerView.Adapter<StoreListAdapter.ViewHolder> {
    private StoreList storeList;

    StoreListAdapter(StoreList list) { this.storeList = list; }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView storeName;
        TextView storeAddress;
        TextView storeCategory;
        
        ViewHolder(View storeView) {
            super(storeView);
            storeName = storeView.findViewById(R.id.store_name);
            storeAddress = storeView.findViewById(R.id.store_address);
            storeCategory = storeView.findViewById(R.id.store_category);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.store_row, parent, false);
        return new ViewHolder(view);
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
