package in.specialsoft.artificaljewellery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import io.paperdb.Paper;

public class AdminPannelActivity extends AppCompatActivity {

    private ImageButton btnAdminViewOrders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_pannel);

        Toast.makeText(this, "You are on Admin Pannel !", Toast.LENGTH_SHORT).show();

        btnAdminViewOrders = findViewById(R.id.btnAdminViewOrders);

        btnAdminViewOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminPannelActivity.this,AdminNewOrdersActivity.class);
                startActivity(intent);
            }
        });
    }

    public void out(View view) {
        Paper.book().destroy();
        Intent intent = new Intent(AdminPannelActivity.this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public void addItemsActicity(View view) {
        Intent intent = new Intent(AdminPannelActivity.this,AdminCategoryActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}