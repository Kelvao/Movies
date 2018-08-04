package io.github.kelvao.movies.ui.adapters;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.kelvao.movies.R;
import io.github.kelvao.movies.models.MovieListModel;
import io.github.kelvao.movies.tasks.OnLoadMoreListener;
import io.github.kelvao.movies.ui.utils.GlideApp;

public class MovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    @SuppressWarnings("FieldCanBeLocal")
    private final int VIEW_LOAD = 0;
    private final int VIEW_ITEM = 1;
    private ArrayList<MovieListModel.Movie> movieList;
    private final int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;
    private final Callback callback;


    public MovieAdapter(ArrayList<MovieListModel.Movie> movieList, RecyclerView rv_movies, Callback callback) {
        this.movieList = movieList;
        this.callback = callback;
        if (rv_movies.getLayoutManager() instanceof LinearLayoutManager) {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) rv_movies.getLayoutManager();
            rv_movies.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager
                            .findLastVisibleItemPosition();
                    if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                        }
                        loading = true;
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_ITEM) {
            return new MovieViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false));
        } else {
            return new LoadingViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.loading_item, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_ITEM) {
            final MovieListModel.Movie movie = movieList.get(position);
            final View movieItem = holder.itemView;
            final MovieViewHolder movieViewHolder = (MovieViewHolder) holder;
            GlideApp.with(movieItem.getContext())
                    .load(movie.getPoster())
                    .centerCrop()
                    .error(R.drawable.ic_broken_image)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            movieViewHolder.iv_poster.setPadding(20, 0, 20, 0);
                            return true;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(movieViewHolder.iv_poster);
            movieViewHolder.tv_name_year.setText(String.format("%s (%s)", movie.getTitle(), movie.getYear()));
            movieItem.setOnClickListener(view -> callback.onItemClick(movie));
        }
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public interface Callback {
        void onItemClick(MovieListModel.Movie movie);
    }

    public void setLoaded() {
        loading = false;
    }


    @Override
    public int getItemCount() {
        return movieList == null ? 0 : movieList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return movieList.get(position) == null ? VIEW_LOAD : VIEW_ITEM;
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_poster) ImageView iv_poster;
        @BindView(R.id.tv_name_year) TextView tv_name_year;

        MovieViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class LoadingViewHolder extends RecyclerView.ViewHolder {

        LoadingViewHolder(View itemView) {
            super(itemView);
        }
    }
}
