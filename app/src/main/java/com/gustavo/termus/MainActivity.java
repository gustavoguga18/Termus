package com.gustavo.termus;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.animation.AnimationUtils;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.firestore.FirebaseFirestore;

import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    GridLayout grade;

    LinearLayout teclado;

    TextView[][] letras =
            new TextView[6][5];

    String palavraSecreta = "";

    int linhaAtual = 0;

    int colunaAtual = 0;

    String tentativaAtual = "";

    List<String> palavrasSecretas =
            new ArrayList<>();

    List<String> palavrasValidas =
            new ArrayList<>();

    SharedPreferences prefs;

    int jogos = 0;

    int vitorias = 0;

    int streakAtual = 0;

    int melhorStreak = 0;

    // FIREBASE

    ImageView imgUser;

    TextView txtNome;

    TextView txtStreak;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        grade = findViewById(R.id.grade);

        teclado = findViewById(R.id.teclado);

        imgUser = findViewById(R.id.imgUser);

        txtNome = findViewById(R.id.txtNome);

        txtStreak = findViewById(R.id.txtStreak);

        db = FirebaseFirestore.getInstance();

        prefs =
                getSharedPreferences(
                        "termus",
                        MODE_PRIVATE
                );

        carregarEstatisticas();

        carregarPalavrasSecretas();

        carregarPalavrasValidas();

        escolherPalavra();

        criarGrade();

        criarTeclado();

        carregarUsuario();
    }

    // =========================
    // USUARIO FIREBASE
    // =========================

    private void carregarUsuario() {

        FirebaseUser user =
                FirebaseAuth
                        .getInstance()
                        .getCurrentUser();

        if (user == null) {
            return;
        }

        txtNome.setText(
                user.getDisplayName()
        );

        txtStreak.setText(
                "🔥 " + streakAtual
        );

        if (user.getPhotoUrl() != null) {

            Picasso.get()
                    .load(user.getPhotoUrl())
                    .into(imgUser);
        }

        salvarRankingOnline(user);
    }

    private void salvarRankingOnline(
            FirebaseUser user
    ) {

        HashMap<String, Object> jogador =
                new HashMap<>();

        jogador.put(
                "nome",
                user.getDisplayName()
        );

        jogador.put(
                "email",
                user.getEmail()
        );

        jogador.put(
                "foto",
                String.valueOf(
                        user.getPhotoUrl()
                )
        );

        jogador.put(
                "streak",
                streakAtual
        );

        jogador.put(
                "vitorias",
                vitorias
        );

        jogador.put(
                "jogos",
                jogos
        );

        db.collection("ranking")
                .document(user.getUid())
                .set(jogador);
    }

    // =========================
    // ESTATISTICAS
    // =========================

    private void carregarEstatisticas() {

        jogos =
                prefs.getInt("jogos", 0);

        vitorias =
                prefs.getInt("vitorias", 0);

        streakAtual =
                prefs.getInt("streak", 0);

        melhorStreak =
                prefs.getInt("melhor", 0);
    }

    private void salvarEstatisticas() {

        SharedPreferences.Editor editor =
                prefs.edit();

        editor.putInt("jogos", jogos);

        editor.putInt("vitorias", vitorias);

        editor.putInt("streak", streakAtual);

        editor.putInt("melhor", melhorStreak);

        editor.apply();
    }

    // =========================
    // PALAVRAS
    // =========================

    private void carregarPalavrasSecretas() {

        try {

            InputStream is =
                    getAssets().open(
                            "palavras_secretas.txt"
                    );

            BufferedReader br =
                    new BufferedReader(
                            new InputStreamReader(is)
                    );

            String linha;

            while ((linha = br.readLine()) != null) {

                linha =
                        linha
                                .trim()
                                .toLowerCase();

                if (linha.length() == 5) {

                    palavrasSecretas.add(linha);
                }
            }

            br.close();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private void carregarPalavrasValidas() {

        try {

            InputStream is =
                    getAssets().open(
                            "palavras_aceitas.txt"
                    );

            BufferedReader br =
                    new BufferedReader(
                            new InputStreamReader(is)
                    );

            String linha;

            while ((linha = br.readLine()) != null) {

                linha =
                        linha
                                .trim()
                                .toLowerCase();

                if (linha.length() == 5) {

                    palavrasValidas.add(linha);
                }
            }

            br.close();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private void escolherPalavra() {

        if (palavrasSecretas.isEmpty()) {

            palavraSecreta = "tempo";

            return;
        }

        Random random = new Random();

        palavraSecreta =
                palavrasSecretas.get(
                        random.nextInt(
                                palavrasSecretas.size()
                        )
                );
    }

    // =========================
    // GRADE
    // =========================

    private void criarGrade() {

        grade.removeAllViews();

        for (int linha = 0; linha < 6; linha++) {

            for (int coluna = 0; coluna < 5; coluna++) {

                TextView tv = new TextView(this);

                GridLayout.LayoutParams params =
                        new GridLayout.LayoutParams();

                params.width = 120;

                params.height = 120;

                params.setMargins(
                        6,
                        6,
                        6,
                        6
                );

                tv.setLayoutParams(params);

                tv.setGravity(Gravity.CENTER);

                tv.setTextSize(28);

                tv.setTextColor(0xFFFFFFFF);

                tv.setBackgroundResource(
                        R.drawable.bg_celula
                );

                letras[linha][coluna] = tv;

                grade.addView(tv);
            }
        }
    }

    // =========================
    // TECLADO
    // =========================

    private void criarTeclado() {

        teclado.removeAllViews();

        String[] linhas = {
                "QWERTYUIOP",
                "ASDFGHJKL",
                "ZXCVBNM"
        };

        for (String linha : linhas) {

            LinearLayout row =
                    new LinearLayout(this);

            row.setGravity(Gravity.CENTER);

            for (char letra :
                    linha.toCharArray()) {

                TextView tecla =
                        criarTecla(
                                String.valueOf(letra)
                        );

                row.addView(tecla);
            }

            teclado.addView(row);
        }

        LinearLayout ultima =
                new LinearLayout(this);

        ultima.setGravity(Gravity.CENTER);

        TextView enter =
                criarTecla("ENTER");

        TextView apagar =
                criarTecla("⌫");

        ultima.addView(enter);

        ultima.addView(apagar);

        teclado.addView(ultima);
    }

    private TextView criarTecla(String texto) {

        TextView tv = new TextView(this);

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        90,
                        110
                );

        params.setMargins(
                4,
                4,
                4,
                4
        );

        tv.setLayoutParams(params);

        tv.setGravity(Gravity.CENTER);

        tv.setText(texto);

        tv.setTextColor(0xFFFFFFFF);

        tv.setTextSize(18);

        tv.setBackgroundResource(
                R.drawable.bg_tecla
        );

        tv.setOnClickListener(v -> {

            if (texto.equals("ENTER")) {

                verificarTentativa();

            } else if (texto.equals("⌫")) {

                apagarLetra();

            } else {

                adicionarLetra(texto);
            }
        });

        return tv;
    }

    // =========================
    // JOGO
    // =========================

    private void adicionarLetra(String letra) {

        if (colunaAtual >= 5) {
            return;
        }

        letras[linhaAtual][colunaAtual]
                .setText(letra);

        tentativaAtual += letra.toLowerCase();

        colunaAtual++;
    }

    private void apagarLetra() {

        if (colunaAtual <= 0) {
            return;
        }

        colunaAtual--;

        letras[linhaAtual][colunaAtual]
                .setText("");

        tentativaAtual =
                tentativaAtual.substring(
                        0,
                        tentativaAtual.length() - 1
                );
    }

    private void verificarTentativa() {

        if (tentativaAtual.length() != 5) {

            return;
        }

        if (!palavrasValidas.contains(
                tentativaAtual
        )) {

            grade.startAnimation(
                    AnimationUtils.loadAnimation(
                            this,
                            android.R.anim.slide_in_left
                    )
            );

            Toast.makeText(
                    this,
                    "Palavra inválida",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        for (int i = 0; i < 5; i++) {

            String letra =
                    String.valueOf(
                            tentativaAtual.charAt(i)
                    );

            TextView tv =
                    letras[linhaAtual][i];

            if (letra.equals(
                    String.valueOf(
                            palavraSecreta.charAt(i)
                    )
            )) {

                tv.setBackgroundColor(
                        0xFF6AAA64
                );

            } else if (
                    palavraSecreta.contains(letra)
            ) {

                tv.setBackgroundColor(
                        0xFFC9B458
                );

            } else {

                tv.setBackgroundColor(
                        0xFF787C7E
                );
            }
        }

        if (tentativaAtual.equals(
                palavraSecreta
        )) {

            jogos++;

            vitorias++;

            streakAtual++;

            if (streakAtual > melhorStreak) {

                melhorStreak = streakAtual;
            }

            salvarEstatisticas();

            Toast.makeText(
                    this,
                    "Você venceu!",
                    Toast.LENGTH_LONG
            ).show();

            return;
        }

        linhaAtual++;

        colunaAtual = 0;

        tentativaAtual = "";

        if (linhaAtual >= 6) {

            jogos++;

            streakAtual = 0;

            salvarEstatisticas();

            Toast.makeText(
                    this,
                    "Palavra: " + palavraSecreta,
                    Toast.LENGTH_LONG
            ).show();
        }
    }
}