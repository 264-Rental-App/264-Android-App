package edu.rentals.frontend;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class OwnerStoreAdapter extends RecyclerView.Adapter<edu.rentals.frontend.OwnerStoreAdapter.ViewHolder>{
    private List<Equipment> ownerEquipmentList;
    final private EquipmentListClickListener mOnClickListener;

    public OwnerStoreAdapter(List<Equipment> ownerEquipmentList, EquipmentListClickListener mOnClickListener) {
        this.ownerEquipmentList = ownerEquipmentList;
        this.mOnClickListener = mOnClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvEquipmentName;
        TextView tvPrice;
        TextView tvTotalQuantity;

        public ViewHolder(View itemView) {
            super(itemView);

            tvEquipmentName = itemView.findViewById(R.id.eName);
            tvPrice = itemView.findViewById(R.id.ePrice);
            tvTotalQuantity = itemView.findViewById(R.id.totalQuantity);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mOnClickListener.onInvoiceClick(position);
        }
    }

    @NonNull
    @Override
    public OwnerStoreAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.owner_equipment_row, parent, false);
        return new OwnerStoreAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OwnerStoreAdapter.ViewHolder holder, int position) {
        String equipmentName = ownerEquipmentList.get(position).geteName();
        int price = ownerEquipmentList.get(position).getePrice();
        int quantity = ownerEquipmentList.get(position).getQuantity();

        holder.tvEquipmentName.setText(equipmentName);
        holder.tvPrice.setText("$ " + String.valueOf(price));
        holder.tvTotalQuantity.setText("Total Quantity: " + String.valueOf(quantity));
    }

    @Override
    public int getItemCount() {
        return ownerEquipmentList.size();
    }

    public interface EquipmentListClickListener {
        void onInvoiceClick(int position);
    }
}
