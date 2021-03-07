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

public class OwnerHomeAdapter extends RecyclerView.Adapter<edu.rentals.frontend.OwnerHomeAdapter.ViewHolder>{
    private List<OwnerHomeActivity.OwnerInvoice> invoiceList;
    final private InvoiceListClickListener mOnClickListener;

    public OwnerHomeAdapter(List<OwnerHomeActivity.OwnerInvoice> invoiceList, InvoiceListClickListener mOnClickListener) {
        this.invoiceList = invoiceList;
        this.mOnClickListener = mOnClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvUserFirstName;
        TextView tvTotalPrice;
        TextView tvTransactionDate;


        public ViewHolder(View itemView) {
            super(itemView);

            tvUserFirstName = itemView.findViewById(R.id.customerFirstName);
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
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.owner_record_row, parent, false);
        return new OwnerHomeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String userId = invoiceList.get(position).getUserId();
        float totalCost = invoiceList.get(position).getTotalCost();
        Timestamp transactionDate= invoiceList.get(position).getTransactionDate();
        Date date = new Date(transactionDate.getTime());
        String toDate = new SimpleDateFormat("MM/dd/yyyy").format(date);

        holder.tvUserFirstName.setText(userId);
        holder.tvTotalPrice.setText("$ " + totalCost);
        holder.tvTransactionDate.setText(toDate);
    }

    @Override
    public int getItemCount() {
        return invoiceList.size();
    }

    public interface InvoiceListClickListener {
        void onInvoiceClick(int position);
    }
}
