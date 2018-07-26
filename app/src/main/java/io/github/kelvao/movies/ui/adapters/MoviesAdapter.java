package io.github.kelvao.movies.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.kelvao.movies.R;
import io.github.kelvao.movies.models.MovieListModel;
import io.github.kelvao.movies.utils.GlideApp;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    private ArrayList<MovieListModel> movieList;
    private Callback callback;

    public MoviesAdapter(ArrayList<MovieListModel> movieList, Callback callback) {
        this.movieList = movieList;
        this.callback = callback;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MovieViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        final MovieListModel movie = movieList.get(position);
        final View movieItem = holder.itemView;
        GlideApp.with(movieItem.getContext())
                .load(movie.getPoster())
                .error(movieItem.getContext().getDrawable(R.drawable.ic_error))
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.iv_poster);
        holder.tv_name_year.setText(String.format("%s (%s)", movie.getTitle(), movie.getYear()));
        movieItem.setOnClickListener(view -> callback.onItemClick(movie));
    }

    public interface Callback{
        void onItemClick(MovieListModel movie);
    }

    @Override
    public int getItemCount() {
        return movieList == null ? 0 : movieList.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_poster)
        ImageView iv_poster;
        @BindView(R.id.tv_name_year)
        TextView tv_name_year;

        MovieViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
