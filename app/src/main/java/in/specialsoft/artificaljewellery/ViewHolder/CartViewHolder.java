package in.specialsoft.artificaljewellery.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import in.specialsoft.artificaljewellery.InterfacePackage.ItemClickListener;
import in.specialsoft.artificaljewellery.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView cart_items_name,cart_items_quantity,cart_items_price;
    private ItemClickListener itemClickListener;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);

        cart_items_name = itemView.findViewById(R.id.cart_items_name);
        cart_items_quantity = itemView.findViewById(R.id.cart_items_quantity);
        cart_items_price = itemView.findViewById(R.id.cart_items_price);
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
