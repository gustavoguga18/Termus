package com.gustavo.termus;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.squareup.picasso.Picasso;

public class PerfilActivity extends AppCompatActivity {

    ImageView imgPerfil;

    TextView txtNome;

    TextView txtEmail;

    TextView txtStats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_perfil);

        imgPerfil = findViewById(R.id.imgPerfil);

        txtNome = findViewById(R.id.txtNome);

        txtEmail = findViewById(R.id.txtEmail);

        txtStats = findViewById(R.id.txtStats);

        FirebaseUser user =
                FirebaseAuth
                        .getInstance()
                        .getCurrentUser();

        if (user != null) {

            txtNome.setText(
                    user.getDisplayName()
            );

            txtEmail.setText(
                    user.getEmail()
            );

            if (user.getPhotoUrl() != null) {

                Picasso.get()
                        .load(user.getPhotoUrl())
                        .into(imgPerfil);
            }
        }

        txtStats.setText(
                "🔥 Jogador Termus Premium"
        );
    }
}