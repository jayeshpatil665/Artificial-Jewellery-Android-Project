package in.specialsoft.artificaljewellery;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.view.SupportActionModeWrapper;
import androidx.legacy.app.ActionBarDrawerToggle;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;
import in.specialsoft.artificaljewellery.Model.Products;
import in.specialsoft.artificaljewellery.Prevalent.PreValent;
import in.specialsoft.artificaljewellery.ViewHolder.ProductViewHolder;
import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    private DatabaseReference productsRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);


        productsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        Paper.init(this);
        //Tite
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                Intent intent = new Intent(HomeActivity.this,CartActivity.class);
                startActivity(intent);
            }
        });
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.


        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home,
                R.id.nav_cart, R.id.nav_orders, R.id.nav_category,R.id.nav_settings,R.id.nav_logout)
                .setDrawerLayout(drawer)
                .build();

       NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);


        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);


        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId())
                {
                    case R.id.nav_cart :
                        Intent intentCart = new Intent(HomeActivity.this,CartActivity.class);
                        startActivity(intentCart);
                        break;
                    case R.id.nav_orders :
                        Intent myOrdersIntent =  new Intent(HomeActivity.this,MyOrdersActivity.class);
                        startActivity(myOrdersIntent);
                        break;
                    case R.id.nav_category :
                        Intent categoryInfo = new Intent(HomeActivity.this,CategoryInfoActivity.class);
                        startActivity(categoryInfo);
                        break;
                    case R.id.nav_settings :
                        Intent intent = new Intent(HomeActivity.this,SettingsActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_logout :
                        Paper.book().destroy();
                        Intent SettingsIntent = new Intent(HomeActivity.this,MainActivity.class);
                        startActivity(SettingsIntent);
                        finish();
                        break;
                }
                //close the drower
                drawer.closeDrawers();
                return false;
            }
        });

        //user Name remember & display
        View headerView = navigationView.getHeaderView(0);

        recyclerView = findViewById(R.id.recycler_menu);

        recyclerView.setHasFixedSize(true);


        //trial coment 2 lines uncoment it
       //layoutManager = new LinearLayoutManager(this);
       //recyclerView.setLayoutManager(layoutManager);

        //sroll Horizontaly
        //LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        //recyclerView.setLayoutManager(layoutManager);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(gridLayoutManager);


        TextView user_profile_name = headerView.findViewById(R.id.user_profile_name);
        CircleImageView user_profile_image = headerView.findViewById(R.id.user_profile_image);

        user_profile_name.setText(PreValent.onlineUser.getName());
        Picasso.get().load(PreValent.onlineUser.getImage()).placeholder(R.drawable.profile).into(user_profile_image);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>().setQuery(productsRef,Products.class).build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Products model) {

                holder.p_product_name.setText(model.getName());
                holder.p_product_description.setText(model.getDescription());
                holder.p_product_price.setText("Price : "+model.getPrice()+" ₹ ");

                Picasso.get().load(model.getImage()).into(holder.p_product_image);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(HomeActivity.this,ProductDetailsActivity.class);
                        intent.putExtra("pid",model.getPid());
                        startActivity(intent);
                    }
                });

            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout,parent,false);
                ProductViewHolder holder = new ProductViewHolder(view);
                return holder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {

       // super.onBackPressed();
    }
}