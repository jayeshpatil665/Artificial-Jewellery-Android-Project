package in.specialsoft.artificaljewellery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import in.specialsoft.artificaljewellery.Model.AdminOrderDetails;
import in.specialsoft.artificaljewellery.ViewHolder.CartViewHolder;
import in.specialsoft.artificaljewellery.ViewHolder.OrderDetailsHolder;

public class AdminUserProductsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;


    private String orderID="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_products);

        orderID = getIntent().getStringExtra("OrderID");

        //setting Layout Manager
        recyclerView = findViewById(R.id.admin_orders_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

    }

    @Override
    protected void onStart() {
        super.onStart();
        final DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Orders");

        FirebaseRecyclerOptions<AdminOrderDetails> options =
                new FirebaseRecyclerOptions.Builder<AdminOrderDetails>()
                .setQuery(productsRef.child(orderID),AdminOrderDetails.class)
                .build();

        FirebaseRecyclerAdapter<AdminOrderDetails,OrderDetailsHolder> adapter = new FirebaseRecyclerAdapter<AdminOrderDetails, OrderDetailsHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull OrderDetailsHolder holder, int position, @NonNull AdminOrderDetails model) {

                holder.admin_details_quantity.setText("Quatity : "+model.getQuantity());
                holder.admin_details_price.setText("Price : "+model.getPrice());
                holder.admin_details_name.setText("Product Name : "+model.getName());
            }

            @NonNull
            @Override
            public OrderDetailsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_details_layout,parent,false);
                OrderDetailsHolder holder = new OrderDetailsHolder(view);
                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}