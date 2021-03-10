package edu.rentals.frontend;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.internal.LinkedTreeMap;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CustomerHomeAdapter extends RecyclerView.Adapter<edu.rentals.frontend.CustomerHomeAdapter.ViewHolder>{

    private List<CustomerHomeActivity.Invoice> invoiceList;
    final private InvoiceListClickListener mOnClickListener;

    public CustomerHomeAdapter(List<CustomerHomeActivity.Invoice> invoiceList, InvoiceListClickListener mOnClickListener) {
        this.invoiceList = invoiceList;
        this.mOnClickListener = mOnClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvStoreName;
        TextView tvTotalPrice;
        TextView tvTransactionDate;


        public ViewHolder(View itemView) {
            super(itemView);

            tvStoreName = itemView.findViewById(R.id.storeName);
            tvTotalPrice = itemView.findViewById(R.id.totalCost);
            tvTransactionDate = itemView.findViewById(R.id.transactionDate);
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
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.invoice_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerHomeAdapter.ViewHolder holder, int position) {
        String storeName = invoiceList.get(position).getStoreName();
        Double totalCost = invoiceList.get(position).getTotalCost();
        String transactionDate= invoiceList.get(position).getTransactionDate();
//        Date date = new Date(transactionDate.getTime());
//        String toDate = new SimpleDateFormat("MM/dd/yyyy").format(date);

        holder.tvStoreName.setText(storeName);
        holder.tvTotalPrice.setText("$ " + totalCost);
        holder.tvTransactionDate.setText(transactionDate);

    }

    @Override
    public int getItemCount() {
        return invoiceList.size();
    }


    public interface InvoiceListClickListener {
        void onInvoiceClick(int position);
    }

}
