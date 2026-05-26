package com.gustavo.termus;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class RankingActivity extends AppCompatActivity {

    RecyclerView recyclerRanking;

    List<Jogador> lista =
            new ArrayList<>();

    RankingAdapter adapter;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ranking);

        recyclerRanking =
                findViewById(
                        R.id.recyclerRanking
                );

        recyclerRanking.setLayoutManager(
                new LinearLayoutManager(this)
        );

        adapter =
                new RankingAdapter(lista);

        recyclerRanking.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        carregarRanking();
    }

    // =========================
    // RANKING REALTIME
    // =========================

    private void carregarRanking() {

        db.collection("ranking")
                .orderBy(
                        "vitorias",
                        Query.Direction.DESCENDING
                )
                .limit(50)
                .addSnapshotListener(
                        new EventListener<QuerySnapshot>() {

                            @Override
                            public void onEvent(
                                    QuerySnapshot value,
                                    com.google.firebase.firestore.FirebaseFirestoreException error
                            ) {

                                if (value == null) {
                                    return;
                                }

                                lista.clear();

                                for (DocumentSnapshot doc :
                                        value.getDocuments()) {

                                    Jogador p =
                                            doc.toObject(
                                                    Jogador.class
                                            );

                                    lista.add(p);
                                }

                                adapter.notifyDataSetChanged();
                            }
                        }
                );
    }
}