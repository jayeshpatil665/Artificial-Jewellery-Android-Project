package in.specialsoft.artificaljewellery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import in.specialsoft.artificaljewellery.Model.Products;
import in.specialsoft.artificaljewellery.Prevalent.PreValent;

public class ProductDetailsActivity extends AppCompatActivity {

    private ImageView details_product_image;
    private TextView details_product_name,details_product_description,details_product_price;
    private ElegantNumberButton details_product_quantity_btn;
    private  String productID="";
    private Button details_product_addtocart_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        productID = getIntent().getStringExtra("pid");

        details_product_quantity_btn = findViewById(R.id.details_product_quantity_btn);
        details_product_image = findViewById(R.id.details_product_image);

        details_product_name = findViewById(R.id.details_product_name);
        details_product_description = findViewById(R.id.details_product_description);
        details_product_price = findViewById(R.id.details_product_price);

        details_product_addtocart_btn = findViewById(R.id.details_product_addtocart_btn);

        getProductDetails(productID);

        details_product_addtocart_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addingToCartList();
            }
        });

    }

    private void addingToCartList() {

        String saveCurrentTime,saveCurrentDate;

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

      final  DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("CartList");
        final HashMap<String,Object> cartMap = new HashMap<>();
        cartMap.put("pid",productID);
        cartMap.put("name",details_product_name.getText().toString());
        cartMap.put("price",details_product_price.getText().toString());
        cartMap.put("date",saveCurrentDate);
        cartMap.put("time",saveCurrentTime);
        cartMap.put("quantity",details_product_quantity_btn.getNumber());

        cartListRef.child("User View").child(PreValent.onlineUser.getPhone()).child("Products")
                .child(productID).updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(ProductDetailsActivity.this, "Product Added To cart ", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(ProductDetailsActivity.this,HomeActivity.class);
                            startActivity(intent);
                        }
                        else
                        {
                            Toast.makeText(ProductDetailsActivity.this, "Problem in adding product !", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void getProductDetails(String productID) {

        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("Products");
        productRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    Products products = dataSnapshot.getValue(Products.class);
                    details_product_name.setText(products.getName());
                    details_product_description.setText(products.getDescription());
                    details_product_price.setText(products.getPrice());
                    Picasso.get().load(products.getImage()).into(details_product_image);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}