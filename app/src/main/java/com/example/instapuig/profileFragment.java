package com.example.instapuig;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class profileFragment extends Fragment {
    ImageView photoImageView;
    TextView displayNameTextView, emailTextView;

    public profileFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        photoImageView = view.findViewById(R.id.photoImageView);
        displayNameTextView = view.findViewById(R.id.displayNameTextView);
        emailTextView = view.findViewById(R.id.emailTextView);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            displayNameTextView.setText(user.getDisplayName());
            emailTextView.setText(user.getEmail());

            // Obtener la foto de perfil del usuario
            Uri photoUrl = user.getPhotoUrl();

            if (photoUrl != null) {
                // Si la foto de perfil está disponible, cargarla en el ImageView
                Glide.with(requireView()).load(photoUrl).into(photoImageView);
            } else {
                // Si la foto de perfil no está disponible, cargar una foto predeterminada
                Glide.with(requireView()).load(R.drawable.default_profile_image).into(photoImageView);
            }
        }
    }

}
