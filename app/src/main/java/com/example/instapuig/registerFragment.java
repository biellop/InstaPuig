package com.example.instapuig;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class registerFragment extends Fragment {
    private FirebaseAuth mAuth;
    private Button registerButton;
    private EditText emailEditText, passwordEditText, displayNameEditText;
    NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        mAuth = FirebaseAuth.getInstance();
        emailEditText = view.findViewById(R.id.emailEditText);
        passwordEditText = view.findViewById(R.id.passwordEditText);
        displayNameEditText = view.findViewById(R.id.displayNameEditText);
        registerButton = view.findViewById(R.id.registerButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerWithEmailAndPassword();
            }
        });
    }

    private void registerWithEmailAndPassword() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        final String displayName = displayNameEditText.getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(displayName)) {
            Snackbar.make(requireView(), "All fields are required", Snackbar.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateProfile(user, displayName);
                        } else {
                            Snackbar.make(requireView(), "Registration failed: " + task.getException().getMessage(), Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void updateProfile(FirebaseUser user, String displayName) {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(displayName)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Profile updated successfully
                            navController.navigate(R.id.homeFragment);
                        } else {
                            Snackbar.make(requireView(), "Failed to update profile: " + task.getException().getMessage(), Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
