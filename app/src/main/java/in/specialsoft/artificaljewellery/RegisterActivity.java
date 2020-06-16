package in.specialsoft.artificaljewellery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.drawable.CircularProgressDrawable;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private EditText register_Uname,register_phone,register_pass;
    private Button btnRegister;


    private ProgressDialog loading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        register_Uname = findViewById(R.id.register_Uname);
        register_phone = findViewById(R.id.register_phone);
        register_pass = findViewById(R.id.register_pass);
        btnRegister = findViewById(R.id.btnRegister);

        loading = new ProgressDialog(this);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });


    }

    private void registerUser(){
        String uName = register_Uname.getText().toString();
        String uPhone = register_phone.getText().toString();
        String uPass = register_pass.getText().toString();

        if (TextUtils.isEmpty(uName) || TextUtils.isEmpty(uPhone) || TextUtils.isEmpty(uPass))
        {
            Toast.makeText(this, "All fields are compulsary !", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loading.setTitle("registering user");
            loading.setMessage("...");
            loading.setCanceledOnTouchOutside(false);
            loading.show();

            validateUser(uName,uPhone,uPass);
        }
    }

    private void validateUser(final String uName, final String uPhone, final String uPass) {
        final DatabaseReference Rootref;
        Rootref = FirebaseDatabase.getInstance().getReference();

        Rootref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!(dataSnapshot.child("Users").child(uPhone).exists()))
                {
                    HashMap<String,Object> userdataMap = new HashMap<>();
                    userdataMap.put("phone",uPhone);
                    userdataMap.put("phoneOrder",uPhone);
                    userdataMap.put("password",uPass);
                    userdataMap.put("name",uName);
                    userdataMap.put("address","address");

                    Rootref.child("Users").child(uPhone).updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {
                                Toast.makeText(RegisterActivity.this, " User Registered succesfully .. ", Toast.LENGTH_SHORT).show();
                                loading.dismiss();
                                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else
                            {
                                loading.dismiss();
                                Toast.makeText(RegisterActivity.this, "check Internet Connection...", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else
                {
                    Toast.makeText(RegisterActivity.this, " User already Exist ! ", Toast.LENGTH_SHORT).show();
                    loading.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
