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
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import in.specialsoft.artificaljewellery.Model.Orders;
import in.specialsoft.artificaljewellery.Prevalent.PreValent;
import in.specialsoft.artificaljewellery.R;
import in.specialsoft.artificaljewellery.ViewHolder.OrdersViewHolder;

public class MyOrdersActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        //initializing recycler View
        recyclerView = findViewById(R.id.my_orders_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //Assigning layout to recycler View

    }

    @Override
    protected void onStart() {
        super.onStart();
        final DatabaseReference orderListRef = FirebaseDatabase.getInstance().getReference().child("Users");
       FirebaseRecyclerOptions<Orders> options = new  FirebaseRecyclerOptions.Builder<Orders>()
               .setQuery(orderListRef.child(PreValent.onlineUser.getPhone())
                       .child("OrderList"),Orders.class)
               .build();

        FirebaseRecyclerAdapter<Orders, OrdersViewHolder> adapter = new FirebaseRecyclerAdapter<Orders, OrdersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull OrdersViewHolder holder, int position, @NonNull final Orders model) {
                holder.my_orders_oid.setText("Order ID : "+model.getOrderID());
                holder.my_orders_state.setText("State : "+model.getState());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence options[] = new CharSequence[]
                                {
                                        "Cancle Order",
                                        "Remove from list"
                                };
                        AlertDialog.Builder builder = new AlertDialog.Builder(MyOrdersActivity.this);
                        builder.setTitle("Order Options");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0)
                                {
                                    if (model.getState().equals("Delivered"))
                                    {
                                        Toast.makeText(MyOrdersActivity.this, "Order is Delivered to you : you can remove it from list", Toast.LENGTH_SHORT).show();
                                    }
                                    else if (model.getState().equals("shipped"))
                                    {
                                        Toast.makeText(MyOrdersActivity.this, "Order is shiped to your Address : contact admin", Toast.LENGTH_SHORT).show();
                                    }
                                    else if (model.getState().equals("not shipped"))
                                    {
                                        Toast.makeText(MyOrdersActivity.this, "Contact Admin to cancel order !", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                if (which == 1)
                                {
                                    if (model.getState().equals("Delivered"))
                                    {
                                        orderListRef.child(PreValent.onlineUser.getPhone()).child("OrderList").child(model.getOrderID())
                                                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(MyOrdersActivity.this, "Item removed Succesfully .", Toast.LENGTH_SHORT).show();
                                               // Intent intent = new Intent(MyOrdersActivity.this,HomeActivity.class);
                                               // startActivity(intent);
                                            }
                                        });
                                    }
                                    else if (model.getState().equals("shipped"))
                                    {
                                        Toast.makeText(MyOrdersActivity.this, "Wait till order is Delivered to you !", Toast.LENGTH_SHORT).show();
                                    }
                                    else if (model.getState().equals("not shipped"))
                                    {
                                        Toast.makeText(MyOrdersActivity.this, "Wait till order is shiped to you !", Toast.LENGTH_SHORT).show();
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
            public OrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_orders_layout,parent,false);
                OrdersViewHolder holder = new OrdersViewHolder(view);
                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}