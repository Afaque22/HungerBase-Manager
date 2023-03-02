package com.example.hungerbasemanager;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hungerbasemanager.Adapters.OrdersAdapter;
import com.example.hungerbasemanager.Fragments.MenuFragment;
import com.example.hungerbasemanager.Fragments.Profile;
import com.example.hungerbasemanager.Models.DetialsModel;
import com.example.hungerbasemanager.Models.OrdersModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class Home extends AppCompatActivity {

    private DrawerLayout drawer;
    FirebaseAuth firebaseAuth;
    NavigationView navigationView;
    RecyclerView recyclerView;
    OrdersAdapter adapter;
    ImageView img;
    FirebaseStorage storage;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference getDbRef;
    TextView txtResName, txtCity, txtAddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.orderRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        img = findViewById(R.id.img_v);
        txtResName = findViewById(R.id.txtresName);
        txtCity = findViewById(R.id.txtcity);
        txtAddress = findViewById(R.id.txtAddress);

        storage = FirebaseStorage.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        getDbRef = firebaseDatabase.getReference().child("Restaurants").child(FirebaseAuth.getInstance().getUid());
        getDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    DetialsModel model = snapshot.getValue(DetialsModel.class);
                    txtResName.setText(model.getResName());
                    txtCity.setText(" "+model.getCity()+", "+model.getProvince());
                    txtAddress.setText(" "+model.getAddress());
                    Picasso.get().load(model.getResImage()).into(img);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        ActivityResultLauncher<String> launcher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                img.setImageURI(result);

                StorageReference reference = storage.getReference().child("res_image").child(FirebaseAuth.getInstance().getUid());
                reference.putFile(result).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(Home.this, "Picture Uploaded Successfully", Toast.LENGTH_SHORT).show();
                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                getDbRef.child("resImage").setValue(uri.toString());
                            }
                        });
                    }
                });
            }
        });

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launcher.launch("image/*");
            }
        });


        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.nav_drawe_open, R.string.nav_drawe_close);
        toggle.syncState();

        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseRecyclerOptions<OrdersModel> options = new FirebaseRecyclerOptions.Builder<OrdersModel>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("Orders").child(firebaseAuth.getUid()),OrdersModel.class)
                .build();

        adapter = new OrdersAdapter(options);
        adapter.notifyDataSetChanged();
        recyclerView.setItemAnimator(null);
        recyclerView.setAdapter(adapter);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                item.setChecked(true);

                drawer.closeDrawer(GravityCompat.START);

                if (id == R.id.logOut_menu){
                    firebaseAuth.signOut();
                    startActivity(new Intent(Home.this,MainActivity.class));
                }if (id == R.id.etprofile){
                    replaceFragment(new Profile());
                }if (id == R.id.etmenu){
                    replaceFragment(new MenuFragment());
                }

                return true;
            }
        });

        onStart();


    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }

    public void replaceFragment(Fragment fragment){

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_fragments,fragment);
        fragmentTransaction.commit();
    }
    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}