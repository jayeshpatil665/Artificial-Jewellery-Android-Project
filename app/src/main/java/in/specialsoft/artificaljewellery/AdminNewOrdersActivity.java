package in.specialsoft.artificaljewellery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import in.specialsoft.artificaljewellery.Model.AdminOrders;
import in.specialsoft.artificaljewellery.Model.Users;

public class AdminNewOrdersActivity extends AppCompatActivity {

    private RecyclerView ordersList;
    private DatabaseReference ordersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_orders);

        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child("TransportDetails");

        ordersList = findViewById(R.id.all_orders_list);
        ordersList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<AdminOrders> options = new FirebaseRecyclerOptions.Builder<AdminOrders>()
                .setQuery(ordersRef,AdminOrders.class)
                .build();
        FirebaseRecyclerAdapter<AdminOrders,AdminOrdersViewHolder> adapter = new FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AdminOrdersViewHolder holder, final int position, @NonNull final AdminOrders model) {
                    holder.admin_view_orders_total_price.setText("Total Price : "+model.getTotalAmount()+" ₹");
                    holder.admin_view_orders_date.setText("Date : "+model.getDate());
                    holder.admin_view_orders_state.setText("Order State : "+model.getState());
                    holder.admin_view_orders_name.setText("Name : "+model.getName());
                    holder.admin_view_orders_address.setText("Address : "+model.getAddress());
                    holder.admin_view_orders_phone.setText("Phone No : "+model.getPhone());
                    holder.admin_view_orders_id.setText("Order ID : "+model.getOrderID());

                    holder.admin_view_show_order_details.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String OrderID = getRef(position).getKey();

                            Intent intent = new Intent(AdminNewOrdersActivity.this,AdminUserProductsActivity.class);
                            intent.putExtra("OrderID",OrderID);
                            startActivity(intent);
                        }
                    });
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            CharSequence options[] = new CharSequence[]
                                    {
                                      "state : shipped",
                                      "state : Delivered",
                                            "Remove from List"
                                    };
                            AlertDialog.Builder builder = new AlertDialog.Builder(AdminNewOrdersActivity.this);
                            builder.setTitle("Product Options");
                            builder.setItems(options, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                        if (which == 0)
                                        {
                                            if (model.getState().equals("not shipped"))
                                            {
                                                String OrderID = getRef(position).getKey();
                                                String userID = model.getUserID();
                                                changeOrderState(OrderID,userID,"shipped");
                                            }
                                            else if (model.getState().equals("shipped"))
                                            {
                                                Toast.makeText(AdminNewOrdersActivity.this, "Product shiped already", Toast.LENGTH_SHORT).show();
                                            }
                                            else if (model.getState().equals("Delivered"))
                                            {
                                                Toast.makeText(AdminNewOrdersActivity.this, "Already delivered", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        else if(which == 1)
                                        {
                                            if (model.getState().equals("shipped"))
                                            {
                                                String OrderID = getRef(position).getKey();
                                                String userID = model.getUserID();
                                                changeOrderState(OrderID,userID,"Delivered");
                                            }
                                            else if (model.getState().equals("not shipped"))
                                            {
                                                Toast.makeText(AdminNewOrdersActivity.this, "First ship the product !", Toast.LENGTH_SHORT).show();
                                            }
                                            else if (model.getState().equals("Delivered"))
                                            {
                                                Toast.makeText(AdminNewOrdersActivity.this, "Already delivered", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        else if(which == 2)
                                        {
                                            String OrderID = getRef(position).getKey();
                                            String tempState = model.getState();
                                            if(tempState.equals("Delivered"))
                                            {
                                                removeOrder(OrderID);
                                                Toast.makeText(AdminNewOrdersActivity.this, "Removed ...", Toast.LENGTH_SHORT).show();
                                            }
                                            else if (tempState.equals("shipped"))
                                            {
                                                Toast.makeText(AdminNewOrdersActivity.this, "Product not Delivered yet !", Toast.LENGTH_SHORT).show();
                                            }
                                            else if (tempState.equals("not shipped"))
                                            {
                                                Toast.makeText(AdminNewOrdersActivity.this, "Product not shipped yet !", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                }
                            });
                            builder.show();
                        }
                    });
            }
 
            @NonNull
            @Override
            public AdminOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_all_orders_layout,parent,false);
                return new AdminOrdersViewHolder(view);
            }
        };
        ordersList.setAdapter(adapter);
        adapter.startListening();
    }

    private void removeOrder(String orderID) {
        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders");
        ordersRef.child(orderID).removeValue();
        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child("TransportDetails");
        ordersRef.child(orderID).removeValue();
        //restore refrence
        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child("TransportDetails");
    }

    private void changeOrderState(String orderID,String userID, String state) {
        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child("TransportDetails");
        ordersRef.child(orderID).child("state").setValue(state);

        ordersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userID).child("OrderList");
        ordersRef.child(orderID).child("state").setValue(state);

        //restore refrence
        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child("TransportDetails");
    }

    public static class AdminOrdersViewHolder extends RecyclerView.ViewHolder
    {
        public TextView admin_view_orders_total_price,admin_view_orders_date,admin_view_orders_state,admin_view_orders_name,admin_view_orders_address,admin_view_orders_phone,admin_view_orders_id;
        public Button admin_view_show_order_details;
        public AdminOrdersViewHolder(@NonNull View itemView) {
            super(itemView);
            admin_view_orders_total_price = itemView.findViewById(R.id.admin_view_orders_total_price);
            admin_view_orders_date = itemView.findViewById(R.id.admin_view_orders_date);
            admin_view_orders_state = itemView.findViewById(R.id.admin_view_orders_state);
            admin_view_orders_name = itemView.findViewById(R.id.admin_view_orders_name);
            admin_view_orders_address = itemView.findViewById(R.id.admin_view_orders_address);
            admin_view_orders_phone = itemView.findViewById(R.id.admin_view_orders_phone);
            admin_view_orders_id = itemView.findViewById(R.id.admin_view_orders_id);

            admin_view_show_order_details =  itemView.findViewById(R.id.admin_view_show_order_details);
        }
    }
}