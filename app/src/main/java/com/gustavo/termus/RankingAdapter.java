package com.gustavo.termus;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class RankingAdapter extends
        RecyclerView.Adapter<RankingAdapter.ViewHolder> {

    List<Jogador> lista;

    public RankingAdapter(
            List<Jogador> lista
    ) {

        this.lista = lista;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view =
                LayoutInflater.from(
                        parent.getContext()
                ).inflate(
                        R.layout.item_ranking,
                        parent,
                        false
                );

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position
    ) {

        Jogador jogador =
                lista.get(position);

        holder.txtPosicao.setText(
                "#" + (position + 1)
        );

        holder.txtNome.setText(
                jogador.nome
        );

        holder.txtVitorias.setText(
                "Vitórias: " + jogador.vitorias
        );

        holder.txtStreak.setText(
                "🔥 " + jogador.streak
        );

        if (jogador.foto != null &&
                !jogador.foto.isEmpty()) {

            Picasso.get()
                    .load(jogador.foto)
                    .into(holder.imgPlayer);
        }
    }

    @Override
    public int getItemCount() {

        return lista.size();
    }

    static class ViewHolder
            extends RecyclerView.ViewHolder {

        TextView txtPosicao;

        TextView txtNome;

        TextView txtVitorias;

        TextView txtStreak;

        ImageView imgPlayer;

        public ViewHolder(
                @NonNull View itemView
        ) {

            super(itemView);

            txtPosicao =
                    itemView.findViewById(
                            R.id.txtPosicao
                    );

            txtNome =
                    itemView.findViewById(
                            R.id.txtNome
                    );

            txtVitorias =
                    itemView.findViewById(
                            R.id.txtVitorias
                    );

            txtStreak =
                    itemView.findViewById(
                            R.id.txtStreak
                    );

            imgPlayer =
                    itemView.findViewById(
                            R.id.imgPlayer
                    );
        }
    }
}