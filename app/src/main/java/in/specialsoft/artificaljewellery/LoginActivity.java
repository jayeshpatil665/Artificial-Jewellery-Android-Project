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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import in.specialsoft.artificaljewellery.Model.Users;
import in.specialsoft.artificaljewellery.Prevalent.PreValent;
import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    private EditText phone,pass;
    private Button btnLogin;

    private TextView admin_pannel;

    private ProgressDialog loading;
    private String dbName="Users";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        phone = findViewById(R.id.phone);
        pass = findViewById(R.id.pass);
        btnLogin = findViewById(R.id.btnLogin);

        admin_pannel = findViewById(R.id.admin_pannel);

        loading = new ProgressDialog(this);
        //Remember user
        Paper.init(this);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        admin_pannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnLogin.setText("Login as Admin");
                admin_pannel.setVisibility(View.INVISIBLE);

                dbName = "Admins";
            }
        });
    }

    private void loginUser() {
        String uPhone = phone.getText().toString();
        String uPass = pass.getText().toString();

        if (TextUtils.isEmpty(uPhone) || TextUtils.isEmpty(uPass))
        {
            Toast.makeText(this, "All fields are compulsary !", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loading.setTitle("Login in user");
            loading.setMessage("...");
            loading.setCanceledOnTouchOutside(false);
            loading.show();

            access(phone,pass);
        }
    }

    private void access(final EditText phone, final EditText pass) {

        final DatabaseReference Rootref;
        Rootref = FirebaseDatabase.getInstance().getReference();

        Rootref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(dbName).child(phone.getText().toString()).exists())
                {
                    Users usersData = dataSnapshot.child(dbName).child(phone.getText().toString()).getValue(Users.class);
                    if (usersData.getPhone().equals(phone.getText().toString()))
                    {
                        if (usersData.getPassword().equals(pass.getText().toString()))
                        {
                            /*
                            Toast.makeText(LoginActivity.this, " Logged in succesfully .. ", Toast.LENGTH_SHORT).show();

                            //Save user credentials--Remember user
                            Paper.book().write(PreValent.UserPhoneKey,phone.getText().toString());
                            Paper.book().write(PreValent.UserPasswordKey,pass.getText().toString());

                            loading.dismiss();
                            Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                            startActivity(intent); */

                            if (dbName.equals("Admins"))
                            {
                                //goto Admin Panel
                                Toast.makeText(LoginActivity.this, " To the Admin pannel.. ", Toast.LENGTH_SHORT).show();
                                loading.dismiss();
                                Intent intent = new Intent(LoginActivity.this,AdminPannelActivity.class);
                                startActivity(intent);
                                finish();

                            }
                            else if (dbName.equals("Users"))
                            {
                                //Go to user pannel
                                Toast.makeText(LoginActivity.this, " Logged in succesfully .. ", Toast.LENGTH_SHORT).show();

                                //Save user credentials--Remember user
                                Paper.book().write(PreValent.UserPhoneKey,phone.getText().toString());
                                Paper.book().write(PreValent.UserPasswordKey,pass.getText().toString());

                                loading.dismiss();
                                Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                                PreValent.onlineUser = usersData;
                                startActivity(intent);
                                finish();
                            }

                        }
                        else
                        {
                            loading.dismiss();
                            Toast.makeText(LoginActivity.this, " Incorrect Password ! ", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else
                {
                    Toast.makeText(LoginActivity.this, " Account not Exist ! ", Toast.LENGTH_SHORT).show();
                    loading.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
