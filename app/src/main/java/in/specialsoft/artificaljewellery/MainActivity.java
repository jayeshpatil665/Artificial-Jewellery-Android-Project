package in.specialsoft.artificaljewellery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import in.specialsoft.artificaljewellery.Model.Users;
import in.specialsoft.artificaljewellery.Prevalent.PreValent;
import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    private Button login_button,signup_button;
    private ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login_button = findViewById(R.id.login_button);
        signup_button = findViewById(R.id.signup_button);

        loading = new ProgressDialog(this);
        //---remember
        Paper.init(this);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

        String UserPhoneKey = Paper.book().read(PreValent.UserPhoneKey);
        String UserPasswordKey = Paper.book().read(PreValent.UserPasswordKey);
        if(UserPhoneKey !="" && UserPasswordKey !="")
        {
            if (!TextUtils.isEmpty(UserPhoneKey) && !TextUtils.isEmpty(UserPasswordKey))
            {
                allowAccess(UserPhoneKey,UserPasswordKey);

                loading.setTitle("Login in user");
                loading.setMessage("...Already Loged in..");
                loading.setCanceledOnTouchOutside(false);
                loading.show();
            }
        }

    }

    private void allowAccess(final String phone, final String pass) {

        final DatabaseReference Rootref;
        Rootref = FirebaseDatabase.getInstance().getReference();

        Rootref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("Users").child(phone).exists())
                {
                    Users usersData = dataSnapshot.child("Users").child(phone).getValue(Users.class);
                    if (usersData.getPhone().equals(phone))
                    {
                        if (usersData.getPassword().equals(pass))
                        {
                            Toast.makeText(MainActivity.this, " Logged in succesfully .. ", Toast.LENGTH_SHORT).show();
                            loading.dismiss();
                            Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                            PreValent.onlineUser = usersData;
                            startActivity(intent);
                        }
                        else
                        {
                            loading.dismiss();
                            Toast.makeText(MainActivity.this, " Incorrect Password ! ", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else
                {
                    Toast.makeText(MainActivity.this, " Account not Exist ! ", Toast.LENGTH_SHORT).show();
                    loading.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onBackPressed() {

    }
}
