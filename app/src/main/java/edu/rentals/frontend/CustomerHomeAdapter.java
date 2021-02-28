package edu.rentals.frontend;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.internal.LinkedTreeMap;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CustomerHomeAdapter extends RecyclerView.Adapter<edu.rentals.frontend.CustomerHomeAdapter.ViewHolder>{

    private List<CustomerHomeActivity.Invoice> invoiceList;

    public CustomerHomeAdapter(List<CustomerHomeActivity.Invoice> invoiceList) {
        this.invoiceList = invoiceList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvStoreName;
        TextView tvTotalPrice;
        TextView tvTransactionDate;


        public ViewHolder(View itemView) {
            super(itemView);

            tvStoreName = itemView.findViewById(R.id.storeName);
            tvTotalPrice = itemView.findViewById(R.id.totalCost);
            tvTransactionDate = itemView.findViewById(R.id.transactionDate);
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.invoice_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerHomeAdapter.ViewHolder holder, int position) {
        String storeName = invoiceList.get(position).getStoreName();
        float totalCost = invoiceList.get(position).getTotalCost();
        Timestamp transactionDate= invoiceList.get(position).getTransactionDate();
        Date date = new Date(transactionDate.getTime());
        String toDate = new SimpleDateFormat("MM/dd/yyyy").format(date);

        holder.tvStoreName.setText(storeName);
        holder.tvTotalPrice.setText("$ " + totalCost);
        holder.tvTransactionDate.setText(toDate);

    }

    @Override
    public int getItemCount() {
        return invoiceList.size();
    }
}
