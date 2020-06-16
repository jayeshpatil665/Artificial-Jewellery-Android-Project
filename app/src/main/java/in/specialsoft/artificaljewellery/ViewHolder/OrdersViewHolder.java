package in.specialsoft.artificaljewellery.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import in.specialsoft.artificaljewellery.InterfacePackage.ItemClickListener;
import in.specialsoft.artificaljewellery.R;

public class OrdersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView my_orders_oid,my_orders_state;
    private ItemClickListener itemClickListener;

    public OrdersViewHolder(@NonNull View itemView) {
        super(itemView);
        my_orders_oid = itemView.findViewById(R.id.my_orders_oid);
        my_orders_state = itemView.findViewById(R.id.my_orders_state);
    }

    @Override
    public void onClick(View v) {
            itemClickListener.onClick(v,getAdapterPosition(),false);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
