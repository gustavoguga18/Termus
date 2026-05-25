package com.gustavo.termus;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.*;
import com.google.android.gms.common.api.ApiException;

import com.google.android.material.button.MaterialButton;

import com.google.firebase.auth.*;

public class LoginActivity extends AppCompatActivity {

    GoogleSignInClient googleSignInClient;

    FirebaseAuth auth;

    int RC_SIGN_IN = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {

            abrirMain();

            return;
        }

        GoogleSignInOptions gso =
                new GoogleSignInOptions.Builder(
                        GoogleSignInOptions.DEFAULT_SIGN_IN
                )
                        .requestIdToken(
                                getString(
                                        R.string.default_web_client_id
                                )
                        )
                        .requestEmail()
                        .build();

        googleSignInClient =
                GoogleSignIn.getClient(this, gso);

        MaterialButton btnGoogle =
                findViewById(R.id.btnGoogle);

        btnGoogle.setOnClickListener(v -> {

            Intent signInIntent =
                    googleSignInClient.getSignInIntent();

            startActivityForResult(
                    signInIntent,
                    RC_SIGN_IN
            );
        });
    }

    @Override
    protected void onActivityResult(
            int requestCode,
            int resultCode,
            @Nullable Intent data
    ) {

        super.onActivityResult(
                requestCode,
                resultCode,
                data
        );

        if (requestCode == RC_SIGN_IN) {

            try {

                GoogleSignInAccount account =
                        GoogleSignIn
                                .getSignedInAccountFromIntent(data)
                                .getResult(ApiException.class);

                firebaseAuth(account);

            } catch (Exception e) {

                e.printStackTrace();

                Toast.makeText(
                        this,
                        "Erro login Google",
                        Toast.LENGTH_LONG
                ).show();
            }
        }
    }

    private void firebaseAuth(
            GoogleSignInAccount account
    ) {

        AuthCredential credential =
                GoogleAuthProvider.getCredential(
                        account.getIdToken(),
                        null
                );

        auth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        abrirMain();

                    } else {

                        Toast.makeText(
                                this,
                                "Erro Firebase",
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });
    }

    private void abrirMain() {

        startActivity(
                new Intent(
                        this,
                        MainActivity.class
                )
        );

        finish();
    }
}