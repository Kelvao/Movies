package io.github.kelvao.movies.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.kelvao.movies.R;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.GenreViewHolder> {

    private ArrayList<String> genres;

    public GenreAdapter(ArrayList<String> genres) {
        this.genres = genres;
    }

    @NonNull
    @Override
    public GenreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GenreViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.genre_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull GenreViewHolder holder, int position) {
        final String genre = genres.get(position);
        holder.tv_genre.setText(genre);
    }

    @Override
    public int getItemCount() {
        return genres == null ? 0 : genres.size();
    }

    class GenreViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_genre)
        TextView tv_genre;

        GenreViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
