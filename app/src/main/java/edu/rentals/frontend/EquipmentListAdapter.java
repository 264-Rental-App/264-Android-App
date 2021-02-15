package edu.rentals.frontend;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class EquipmentListAdapter extends RecyclerView.Adapter<edu.rentals.frontend.EquipmentListAdapter.ViewHolder> {


    // total price
    int totalPrice;

    private List<Equipment> equipmentList;

    public EquipmentListAdapter(List<Equipment> equipmentList) {
        this.equipmentList = equipmentList;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPhoto;
        TextView tvName;
        TextView tvPrice;
        ImageButton ivAdd, ivSub;
        TextView tvQuantity;


        public ViewHolder(View itemView) {
            super(itemView);
            ivPhoto = (ImageView) itemView.findViewById(R.id.ePhoto);
            tvName = itemView.findViewById(R.id.eName);
            tvPrice = itemView.findViewById(R.id.ePrice);

            ivAdd = itemView.findViewById(R.id.addquantity);
            ivSub = itemView.findViewById(R.id.subquantity);
            tvQuantity = itemView.findViewById(R.id.quantity);


        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.equipment_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        // here we will find the position and start setting the output on our views

        String eName = equipmentList.get(position).geteName();
        String ePrice = String.valueOf(equipmentList.get(position).getePrice());
        int images = equipmentList.get(position).getePhoto();

        holder.tvName.setText(eName);
        holder.tvPrice.setText("$ " + ePrice);
        holder.ivPhoto.setImageResource(images);



        // getting particular equipment from equipment list
//        final Equipment equipment = equipmentList.get(position);

        // add quantity button
        holder.ivAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                equipmentList.get(position).setQuantity(equipmentList.get(position).getQuantity() + 1);
                totalPrice += equipmentList.get(position).getePrice();
//                Log.d("total price", String.valueOf(totalPrice));
//                Log.d("quantity in list", String.valueOf(equipmentList.get(position).getQuantity()));
                holder.tvQuantity.setText(String.valueOf(equipmentList.get(position).getQuantity()));

                // send total price to equipment list activity
                if(mOnTotalPriceChangeListener != null){
                    mOnTotalPriceChangeListener.onTotalPriceChanged(totalPrice);
                }
            }
        });

        // minus quantity button
        holder.ivSub.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (equipmentList.get(position).getQuantity() == 0) {
//                    Toast.makeText(EquipmentListActivity, "Cant decrease quantity < 0", Toast.LENGTH_SHORT).show();
                } else {
                    totalPrice -= equipmentList.get(position).getePrice();
                    equipmentList.get(position).setQuantity(equipmentList.get(position).getQuantity() - 1);
                }
//                Log.d("total price", String.valueOf(totalPrice));
//                Log.d("quantity in list", String.valueOf(equipmentList.get(position).getQuantity()));
                holder.tvQuantity.setText(String.valueOf(equipmentList.get(position).getQuantity()));

                // send total price to equipment list activity
                if(mOnTotalPriceChangeListener != null){
                    mOnTotalPriceChangeListener.onTotalPriceChanged(totalPrice);
                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return equipmentList.size();
    }


    // interface to send any price change to list activity
    public interface OnTotalPriceChangeListener {
        void onTotalPriceChanged(int totalPrice);
    }

    OnTotalPriceChangeListener mOnTotalPriceChangeListener;
    public void setOnTotalPriceChangeListener(OnTotalPriceChangeListener onTotalPriceChangeListener){
        mOnTotalPriceChangeListener = onTotalPriceChangeListener;
    }


}




