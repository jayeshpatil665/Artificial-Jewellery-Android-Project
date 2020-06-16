package in.specialsoft.artificaljewellery.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import in.specialsoft.artificaljewellery.InterfacePackage.ItemClickListener;
import in.specialsoft.artificaljewellery.R;

public class ProductViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener
{
    public TextView p_product_name,p_product_description,p_product_price;
    public ImageView p_product_image;

    public ItemClickListener listener;

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);

        p_product_image = itemView.findViewById(R.id.p_product_image);
        p_product_name = itemView.findViewById(R.id.p_product_name);
        p_product_description = itemView.findViewById(R.id.p_product_description);
        p_product_price = itemView.findViewById(R.id.p_product_price);
    }

    public void setItemClickListener(ItemClickListener listener)
    {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        listener.onClick(v,getAdapterPosition(),false);
    }
}
