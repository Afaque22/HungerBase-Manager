package com.example.hungerbasemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.example.hungerbasemanager.Models.DetialsModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends AppCompatActivity {

    Button btnSave;
    AutoCompleteTextView etProvince, etCity;
    TextInputEditText etName, etResName, etPhoneNo, etAddress;
    DatabaseReference dropdownRef;
    DatabaseReference detailsRef;
    FirebaseDatabase firebaseDatabase;
    ArrayList<String> province;
    String userClick;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        etProvince = findViewById(R.id.etProvince);
        etCity = findViewById(R.id.etCity);
        etName = findViewById(R.id.etName);
        etResName = findViewById(R.id.etResName);
        etPhoneNo = findViewById(R.id.etPhone);
        etAddress = findViewById(R.id.etAddress);
        btnSave = findViewById(R.id.btnSave);




        firebaseDatabase = FirebaseDatabase.getInstance();
        dropdownRef = firebaseDatabase.getReference("DropDowns").child("Province");


        auth = FirebaseAuth.getInstance();

        dropdownRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                province = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String state = snapshot.getKey();
                    province.add(state);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(DetailsActivity.this, R.layout.drop_down, province);

                adapter.setDropDownViewResource(R.layout.drop_down);
                etProvince.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                processInsert();

            }
        });


        dropDownItemClick();

        //  FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        //   dbRef.keepSynced(true);

        onPause();

    }

    // ----- Method dropDownItemClick Start ---->
    public void dropDownItemClick() {


        etProvince.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                userClick = province.get(position);

                dropdownRef.child(userClick).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<String> cities = new ArrayList<>();
                        for (DataSnapshot snapshot1 : snapshot.getChildren()){
                            String city = snapshot1.getKey();
                            cities.add(city);
                        }
                        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(DetailsActivity.this,R.layout.drop_down,cities);

                        etCity.setAdapter(adapter2);
                        etCity.setText("");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(DetailsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
    // ----- method dropDownItemClick end -----


    // ------ method processInsert start ------


    public void processInsert() {

        String ownerName = etName.getText().toString();
        String resName = etResName.getText().toString();
        String phoneNo = etPhoneNo.getText().toString();
        String province = etProvince.getText().toString();
        String city = etCity.getText().toString();
        String address = etAddress.getText().toString();

        String userID = FirebaseAuth.getInstance().getUid();

        DetialsModel model = new DetialsModel(ownerName, resName, phoneNo, province, city, address,userID);

        detailsRef = firebaseDatabase.getReference();

        if (ownerName.equals("") || resName.equals("") || phoneNo.equals("") ||
        province.equals("") || city.equals("") || address.equals("")){
            Toast.makeText(this, "Enter All Detail", Toast.LENGTH_SHORT).show();
        }else {

            detailsRef.child("Restaurants").child(userID).setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {

                    etName.setText("");
                    etResName.setText("");
                    etPhoneNo.setText("");
                    etProvince.setText("");
                    etCity.setText("");
                    etAddress.setText("");
                    Toast.makeText(DetailsActivity.this, "Details Inserted", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(DetailsActivity.this, Home.class));


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(DetailsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();


                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this,DetailsActivity.class));
        Toast.makeText(this, "Please Enter Details", Toast.LENGTH_SHORT).show();
    }
}
