package movies.toolinc.com.movies;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.squareup.picasso.Picasso;

import movies.toolinc.com.movies.model.Movie;

/**
 * Provide a binding from the movies retrieve from the api which displayed within a
 * {@link RecyclerView}.
 */
public final class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder> {

    private static final String BASE_URL = "https://image.tmdb.org/t/p/w500/%s";
    private final ImmutableList<Movie> movies;
    private final OnMovieSelected onMovieSelected;

    public MoviesAdapter(ImmutableList<Movie> movies, OnMovieSelected onMovieSelected) {
        this.movies = Preconditions.checkNotNull(movies, "Movies are missing.");
        this.onMovieSelected = Preconditions.checkNotNull(onMovieSelected, "Missing the selection movie listener.");
    }

    @NonNull
    @Override
    public MoviesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.movie_list_item, viewGroup, false);
        return new MoviesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesViewHolder moviesViewHolder, int position) {
        Movie movie = movies.get(position);
        Picasso.get()
                .load(String.format(BASE_URL, movie.posterPath()))
                .into(moviesViewHolder.ivPoster);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    /**
     * Describes a movie item view about its place within the RecyclerView.
     */
    public final class MoviesViewHolder extends RecyclerView.ViewHolder implements OnClickListener {

        private final ImageView ivPoster;

        public MoviesViewHolder(View view) {
            super(view);
            ivPoster = (ImageView) view.findViewById(R.id.iv_movie_poster);
            ivPoster.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onMovieSelected.onSelected(movies.get(getAdapterPosition()));
        }
    }
}
