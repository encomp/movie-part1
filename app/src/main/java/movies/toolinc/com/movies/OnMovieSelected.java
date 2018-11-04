package movies.toolinc.com.movies;

import movies.toolinc.com.movies.model.Movie;

/**
 * Specifies the behavior upon selection of a {@link Movie}.
 */
public interface OnMovieSelected {

    /**
     * Specifies the movie that has been selected by the user.
     */
    void onSelected(Movie movie);
}