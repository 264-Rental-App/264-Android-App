package edu.rentals.frontend;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SummaryAdapter extends RecyclerView.Adapter<edu.rentals.frontend.SummaryAdapter.ViewHolder> {

    private static List<Equipment> equipmentSummaryList;


    public SummaryAdapter(List<Equipment> equipmentList) {
        List<Equipment> equipmentSummaryList = new ArrayList<>() ;
        for (int i=0; i< equipmentList.size(); i++) {
            if (equipmentList.get(i).getQuantity() > 0) {
                equipmentSummaryList.add(new Equipment(equipmentList.get(i).geteName(), equipmentList.get(i).getePrice(), equipmentList.get(i).getePhoto(), equipmentList.get(i).getQuantity()));
            }
        }

        this.equipmentSummaryList = equipmentSummaryList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvQuantity;


        public ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.eName);
            tvQuantity = itemView.findViewById(R.id.eQuantity);


        }
    }
    @Override
    public edu.rentals.frontend.SummaryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.summary_row, parent, false);
        return new edu.rentals.frontend.SummaryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull edu.rentals.frontend.SummaryAdapter.ViewHolder holder, int position) {
        String eName = equipmentSummaryList.get(position).geteName();
        String eQuantity = String.valueOf(equipmentSummaryList.get(position).getQuantity());

        holder.tvName.setText(eName);
        Log.d("name : ", eName);
        Log.d("quantity : ", eQuantity);
        holder.tvQuantity.setText("Qty: " + eQuantity);
    }

    @Override
    public int getItemCount() {
        return equipmentSummaryList.size();
    }


    public static int totalSum() {
        int total = 0;
        for (int i=0; i< equipmentSummaryList.size(); i++) {
            total += (equipmentSummaryList.get(i).getePrice() * equipmentSummaryList.get(i).getQuantity());
        }
        return total;
    }
}
