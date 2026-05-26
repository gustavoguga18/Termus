package com.gustavo.termus;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class ConfigActivity extends AppCompatActivity {

    Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_config);

        btnLogout =
                findViewById(R.id.btnLogout);

        btnLogout.setOnClickListener(v -> {

            FirebaseAuth
                    .getInstance()
                    .signOut();

            Intent intent =
                    new Intent(
                            ConfigActivity.this,
                            LoginActivity.class
                    );

            startActivity(intent);

            finishAffinity();
        });
    }
}