package in.specialsoft.artificaljewellery;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

import in.specialsoft.artificaljewellery.Prevalent.PreValent;

public class ConfirmFinalOrderActivity extends AppCompatActivity {

    private EditText confirm_name,confirm_number,confirm_address;
    private Button confirm_order_btn;
    private String totalAmount="",orderID;
    private TextView confirm_price,confirm_ID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);

        totalAmount = getIntent().getStringExtra("TotalPrice");
        Toast.makeText(this, "Total Amount : "+totalAmount+" ₹", Toast.LENGTH_SHORT).show();

        confirm_name =findViewById(R.id.confirm_name);
        confirm_number=findViewById(R.id.confirm_number);
        confirm_address=findViewById(R.id.confirm_address);
        confirm_order_btn=findViewById(R.id.confirm_order_btn);

        confirm_price = findViewById(R.id.confirm_price);
        confirm_ID = findViewById(R.id.confirm_ID);

        setUserDetailsToText();
        confirm_price.setText("Amount to Pay : "+totalAmount+" ₹");
        genrateProductID();

        confirm_order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkDetails();
            }
        });
    }

    private void setUserDetailsToText() {

        confirm_name.setText(PreValent.onlineUser.getName().toString());
        confirm_address.setText(PreValent.onlineUser.getAddress().toString());
        confirm_number.setText(PreValent.onlineUser.getPhone());
    }

    private void genrateProductID() {
        String saveCurrentTime,saveCurrentDate;

        Random random = new Random();

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HHmmss");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        orderID =random.nextInt(1000)+saveCurrentTime.toString().toLowerCase()+random.nextInt(100);
        confirm_ID.setText("Order ID : "+orderID);

    }

    private void checkDetails() {
        if (TextUtils.isEmpty(confirm_name.getText().toString()) || TextUtils.isEmpty(confirm_number.getText().toString()) || TextUtils.isEmpty(confirm_address.getText().toString()))
        {
            Toast.makeText(this, "All Fields are mendatory !!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            confirmOrder();
        }
    }

    private void confirmOrder() {
        final String saveCurrentDate,saveCurrentTime;
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentDate.format(calForDate.getTime());

        final DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference()
                .child("Orders")
                .child("TransportDetails")
                .child(orderID);
        HashMap<String,Object> orderMap =new HashMap<>();
        orderMap.put("totalAmount",totalAmount);
        orderMap.put("name",confirm_name.getText().toString());
        orderMap.put("phone",confirm_number.getText().toString());
        orderMap.put("userID",PreValent.onlineUser.getPhone());
        orderMap.put("address",confirm_address.getText().toString());
        orderMap.put("date",saveCurrentDate);
        orderMap.put("time",saveCurrentTime);
        orderMap.put("orderID",orderID);
        orderMap.put("state","not shipped");

        orderRef.updateChildren(orderMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {   //Save Items to Orders
                    //..........

                    final DatabaseReference sourceRef = FirebaseDatabase.getInstance().getReference()
                            .child("CartList").child("User View")
                            .child(PreValent.onlineUser.getPhone())
                            .child("Products");

                    final DatabaseReference destinationRef = FirebaseDatabase.getInstance().getReference()
                            .child("Orders")
                            .child(orderID);
                    //cutting & pasting data
                    cutPastData(sourceRef,destinationRef);

                    //Adding order number to user data----------------
                    HashMap<String,Object> orderListChildsMap =new HashMap<>();
                    orderListChildsMap.put("orderID",orderID);
                    orderListChildsMap.put("state","not shipped");
                    final DatabaseReference orderListRef = FirebaseDatabase.getInstance().getReference().child("Users").child(PreValent.onlineUser.getPhone())
                            .child("OrderList").child(orderID);
                    orderListRef
                            .updateChildren(orderListChildsMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {
                                Toast.makeText(ConfirmFinalOrderActivity.this, "Order LIst Updated", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    //Empty Cart

                    FirebaseDatabase.getInstance().getReference()
                            .child("CartList").child("User View")
                            .child(PreValent.onlineUser.getPhone()).removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(ConfirmFinalOrderActivity.this, "Final Order placed Succesfully", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(ConfirmFinalOrderActivity.this,HomeActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                }
            }
        });
    }

    private void cutPastData(final DatabaseReference sourceRef, final DatabaseReference destinationRef) {

        sourceRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                destinationRef.setValue(dataSnapshot.getValue(), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                        if (databaseError != null )
                        {
                            Toast.makeText(ConfirmFinalOrderActivity.this, "Copy Failed", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(ConfirmFinalOrderActivity.this, "Success to copy Data", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}