package com.example.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class login extends Fragment {

    private FirebaseAuth mAuth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        mAuth = FirebaseAuth.getInstance();
        Button Login=view.findViewById(R.id.login);

        EditText phoneEditText=view.findViewById(R.id.editTextPhone);



        Login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String phone = phoneEditText.getText().toString();


                if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(getActivity(), "Please Rnter phone number", Toast.LENGTH_SHORT).show();
                    return;
                }
                checkUser(phone);
            }
        });

        Button create_userbtn = view.findViewById(R.id.create_user);
        create_userbtn.setOnClickListener(v -> {
            // Replace the current fragment with the login fragment
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentContainerView, new create_user());
            transaction.addToBackStack(null);
            transaction.commit();
        });
        return view;
    }

    private void checkUser(String phone) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");

        // Check if the user exists in the database
        usersRef.orderByChild("phone").equalTo(phone).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // User exists, launch login function
                    // Example:
                    // launchLoginFunction();
                    Toast.makeText(getActivity(), "User Exist", Toast.LENGTH_SHORT).show();
                    // Create user logic here
                    Toast.makeText(getActivity(), "Please verify your Phone number", Toast.LENGTH_SHORT).show();
                    Bundle bundle = new Bundle();
                    bundle.putString("phone", phone);

                    // Navigate to verification fragment
                    FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                    verify_login fragment = new verify_login();
                    fragment.setArguments(bundle);
                    transaction.replace(R.id.fragmentContainerView, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                } else {
                    // User doesn't exist, launch create user fragment
                    // Example:
                    // launchCreateUserFragment();
                    Toast.makeText(getActivity(), "User does mot Exist", Toast.LENGTH_SHORT).show();
                    FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                    create_user fragment = new create_user();
                    transaction.replace(R.id.fragmentContainerView, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Error occurred while querying the database
                Toast.makeText(requireContext(), "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void loginUser(String phone, String password) {
        mAuth.signInWithEmailAndPassword(phone, password)
                .addOnCompleteListener(requireActivity(), task -> {
                    if (task.isSuccessful()) {
                        // Login successful, navigate to home fragment or perform desired action
                        Toast.makeText(getActivity(), "Login successful", Toast.LENGTH_SHORT).show();
                        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragmentContainerView, new home());
                        transaction.addToBackStack(null);
                        transaction.commit();
                    } else {
                        // Login failed, display error message
                        Toast.makeText(getActivity(), "Login failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}