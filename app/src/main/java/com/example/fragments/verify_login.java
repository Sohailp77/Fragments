package com.example.fragments;

import static android.widget.Toast.LENGTH_SHORT;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class verify_login extends Fragment {

    private String phone;
    private FirebaseAuth mAuth;

    private FirebaseAuth auth;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference usersRef = database.getReference("users");
    private EditText editTextVerificationCode;
    private String verificationId = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_verify, container, false);
        mAuth = FirebaseAuth.getInstance();

        editTextVerificationCode = view.findViewById(R.id.verifedit);
        Button verifyButton = view.findViewById(R.id.verifybtn);

        verifyButton.setOnClickListener((v) -> {
            String verificationCode = editTextVerificationCode.getText().toString().trim();
            if (!verificationCode.isEmpty()) {
                // Create PhoneAuthCredential using verification ID and verification code
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, verificationCode);
                // Sign in with the credential
                signInWithPhoneAuthCredential(credential);
            } else {
                Toast.makeText(getActivity(), "Please enter verification code", LENGTH_SHORT).show();
            }
        });
        Bundle bundle = getArguments();
        if (bundle != null) {
            phone = bundle.getString("phone");


            // Initiate phone number verification upon fragment creation
            initiatePhoneNumberVerification(phone);
        }
        return view;
    }
    private void initiatePhoneNumberVerification(String phone) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+91"+ phone)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(requireActivity())       // (optional) Activity for callback binding
                        // If no activity is passed, reCAPTCHA verification can not be used.
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                                // Automatically handle verification completed
                                signInWithPhoneAuthCredential(credential);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                // Handle verification failure
                                Toast.makeText(getActivity(), "Phone number verification failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                                // Store the verification ID
                                verify_login.this.verificationId = verificationId;
                            }
                        })
                        .build();

        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Example: Navigate to a new fragment
                            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                            transaction.replace(R.id.fragmentContainerView, new home());
                            transaction.addToBackStack(null);
                            transaction.commit();
                        } else {
                            // Phone authentication failed
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // Invalid verification code
                                Toast.makeText(getActivity(), "Invalid verification code", LENGTH_SHORT).show();
                            } else {
                                // Other errors
                                Toast.makeText(getActivity(), "Phone authentication failed: " + task.getException().getMessage(), LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
}