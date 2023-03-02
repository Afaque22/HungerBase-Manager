package com.example.hungerbasemanager.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.hungerbasemanager.Adapters.MenuAdapter;
import com.example.hungerbasemanager.Models.DetialsModel;
import com.example.hungerbasemanager.Models.MenuModel;
import com.example.hungerbasemanager.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;
import java.util.Objects;


public class MenuFragment extends Fragment {


    public MenuFragment() {
        // Required empty public constructor
    }

    FirebaseDatabase firebaseDatabase;
    DatabaseReference dbRef;
    TextInputEditText itemName, itemDesc, itemPrice;
    ImageView itemImg;
    Button btnAddItem;
    MenuAdapter adapter;
    RecyclerView recyclerView;
    String userID;
    Uri url;
    FirebaseAuth auth;
    FirebaseStorage storage;
    ActivityResultLauncher<String> launcher;
    Uri imgUri;
    String menuItemId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        itemName = view.findViewById(R.id.itemName);
        itemDesc = view.findViewById(R.id.itemDesc);
        itemPrice = view.findViewById(R.id.itemPrice);
        btnAddItem = view.findViewById(R.id.itemAddBtn);
        recyclerView = view.findViewById(R.id.menuRecycler);
        itemImg = view.findViewById(R.id.itemImg);

        firebaseDatabase = FirebaseDatabase.getInstance();

        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();

        launcher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        imgUri = result;
                        itemImg.setImageURI(imgUri);
                    }
                });


        itemImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               launcher.launch("image/*");
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // auth = FirebaseAuth.getInstance();

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();


        dbRef = firebaseDatabase.getReference();


        FirebaseRecyclerOptions<MenuModel> options = new FirebaseRecyclerOptions.Builder<MenuModel>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("Restaurants").child(userID).child("Menu"), MenuModel.class)
                .build();


        adapter = new MenuAdapter(options);
        adapter.notifyDataSetChanged();
        recyclerView.setItemAnimator(null);
        recyclerView.setAdapter(adapter);

      //  StorageReference reference = storage.getReference().child("menu_img").child(FirebaseAuth.getInstance().getUid());
      //  reference.putFile(url)


        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    itemInsertion();
                }catch (Exception e){
                    Toast.makeText(getContext(),"(Please select the Image)"+ e.getMessage(), Toast.LENGTH_SHORT).show();
                }


            }
        });


        return view;

    }


    public void itemInsertion() {


        String orderName = itemName.getText().toString();
        String desc = itemDesc.getText().toString();
        String price = itemPrice.getText().toString();

        if (orderName.equals("") || desc.equals("") || price.equals("")){
            Toast.makeText(getContext(), "Insert All Details", Toast.LENGTH_SHORT).show();
        }else {


            String userid = FirebaseAuth.getInstance().getUid();

            DatabaseReference menuRef = dbRef.child("Restaurants").child(userid).child("Menu");

            menuItemId = menuRef.push().getKey();
            StorageReference reference = storage.getReference().child("menu_img").child(new Date().getTime()+"");

            reference.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            MenuModel menuModel = new MenuModel(orderName, desc, price, userid, uri.toString());

                            menuRef.child(menuItemId).setValue(menuModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    itemName.setText("");
                                    itemDesc.setText("");
                                    itemPrice.setText("");
                                    Toast.makeText(getContext(), "Menu Added", Toast.LENGTH_SHORT).show();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    });
                }
            });

          /*  reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {

                    MenuModel menuModel = new MenuModel(orderName, desc, price, userid, uri.toString());

                    menuRef.child(menuItemId).setValue(menuModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            itemName.setText("");
                            itemDesc.setText("");
                            itemPrice.setText("");
                            Toast.makeText(getContext(), "Menu Added", Toast.LENGTH_SHORT).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }); */
        }





      /*  reference.putFile(url).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        MenuModel menuModel = new MenuModel(orderName, desc, price, userid,uri.toString());

                        String menuItemId = menuRef.push().getKey();

                        menuRef.child(menuItemId).setValue(menuModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                itemName.setText("");
                                itemDesc.setText("");
                                itemPrice.setText("");
                                Toast.makeText(getContext(), "Menu Added", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "Error Occurred", Toast.LENGTH_SHORT).show();
                            }
                        });



                    }
                });

            }
        }); */



    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}

