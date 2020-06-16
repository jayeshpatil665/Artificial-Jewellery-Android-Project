package in.specialsoft.artificaljewellery.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import in.specialsoft.artificaljewellery.InterfacePackage.ItemClickListener;
import in.specialsoft.artificaljewellery.R;

public class OrderDetailsHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{

    public TextView admin_details_name,admin_details_quantity,admin_details_price;
    private ItemClickListener itemClickListener;
    public OrderDetailsHolder(@NonNull View itemView) {
        super(itemView);

        admin_details_name = itemView.findViewById(R.id.admin_details_name);
        admin_details_quantity = itemView.findViewById(R.id.admin_details_quantity);
        admin_details_price = itemView.findViewById(R.id.admin_details_price);
    }

    @Override
    public void onClick(View v) {
            itemClickListener.onClick(v,getAdapterPosition(),false);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
