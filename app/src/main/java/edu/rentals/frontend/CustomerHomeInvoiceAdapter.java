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

public class CustomerHomeInvoiceAdapter extends RecyclerView.Adapter<edu.rentals.frontend.CustomerHomeInvoiceAdapter.ViewHolder> {

    private List<CustomerHomeInvoiceActivity.Rental> invoiceRental;


    public CustomerHomeInvoiceAdapter(List<CustomerHomeInvoiceActivity.Rental> invoiceRental) {
        this.invoiceRental = invoiceRental;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvEName;
        TextView tvEQty;

        public ViewHolder(View itemView) {
            super(itemView);
            tvEName = itemView.findViewById(R.id.eName);
            tvEQty = itemView.findViewById(R.id.eQuantity);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_rental_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerHomeInvoiceAdapter.ViewHolder holder, int position) {
        String eName = invoiceRental.get(position).getName();
        int eQty = invoiceRental.get(position).getQuantity();

        holder.tvEName.setText(eName);
        holder.tvEQty.setText("Qty : " + eQty);
    }

    @Override
    public int getItemCount() {
        return invoiceRental.size();
    }


}
