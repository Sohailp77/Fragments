package com.example.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class create_user extends Fragment {

    private FirebaseAuth mAuth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_user, container, false);
        Button loginButton = view.findViewById(R.id.loginpage);

        mAuth = FirebaseAuth.getInstance();

        EditText editTextName = view.findViewById(R.id.editTextName);
        EditText editTextPhone = view.findViewById(R.id.phone);
        EditText editTextPassword = view.findViewById(R.id.editTextPassword);
        EditText editTextEmail = view.findViewById(R.id.editTextEmail2);
        Button createUserButton = view.findViewById(R.id.buttonCreateUser);
        createUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString().trim();
                String phone = editTextPhone.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                String Email = editTextEmail.getText().toString().trim();

                // Validate input fields
                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(getActivity(), "Please Enter your name", Toast.LENGTH_SHORT).show();
                    return;
                } else if (name.length() < 6) {
                    Toast.makeText(getActivity(), "Name must be at least 6 characters", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(getActivity(), "Please Enter your Phone number", Toast.LENGTH_SHORT).show();
                    return;
                } else if (phone.length() != 10) {
                    Toast.makeText(getActivity(), "Phone number must be 10 digits", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getActivity(), "Please Enter your password", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(Email) || !isValidEmail(Email)) {
                    Toast.makeText(getActivity(), "Please Enter a valid email address", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Create user logic here
                Toast.makeText(getActivity(), "Please verify your Phone number", Toast.LENGTH_SHORT).show();
                Bundle bundle = new Bundle();
                bundle.putString("name", name);
                bundle.putString("phone", phone);
                bundle.putString("password", password);
                bundle.putString("Email", Email);

                // Navigate to verification fragment
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                verify fragment = new verify();
                fragment.setArguments(bundle);
                transaction.replace(R.id.fragmentContainerView, fragment);
                transaction.addToBackStack(null);
                transaction.commit();

            }
            private boolean isValidEmail(String Email) {
                String EMAIL_PATTERN = "^[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[ -z]+\\.)+[a-zA-Z]{2,}$";
                Pattern pattern = Pattern.compile(EMAIL_PATTERN);
                Matcher matcher = pattern.matcher(Email);
                return matcher.matches();
            }
        });


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace the current fragment with the login fragment
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentContainerView, new login());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });


        return view;
    }

}