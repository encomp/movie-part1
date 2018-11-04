package movies.toolinc.com.movies;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.Optional;

import movies.toolinc.com.movies.model.Movie;
import movies.toolinc.com.movies.model.Movies;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * This activity performs an API call to {@code api.themoviedb.org} to retrieve the top movies.
 */
public class MainActivity extends AppCompatActivity implements OnMovieSelected {

    private static final String MOVIE_TOP = "movie/top_rated";
    private static final String MOVIE_POPULAR = "movie/popular";
    private RecyclerView rvMovies;
    private TextView tvErrorMessage;
    private ProgressBar pbLoadingIndicator;
    private String url = MOVIE_POPULAR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvErrorMessage = (TextView) findViewById(R.id.tv_error_message_display);
        pbLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        rvMovies = (RecyclerView) findViewById(R.id.recyclerview_movies);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        rvMovies.setLayoutManager(layoutManager);
        rvMovies.setItemAnimator(new DefaultItemAnimator());
        rvMovies.setHasFixedSize(true);
        loadMovies();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sort, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (R.id.mi_movie == item.getItemId()) {
            String title = item.getTitle().toString();
            if (title.equals(getString(R.string.popular))) {
                url = MOVIE_POPULAR;
                item.setTitle(R.string.top);
            } else {
                url = MOVIE_TOP;
                item.setTitle(R.string.popular);
            }
            loadMovies();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSelected(Movie movie) {
        Intent intent = new Intent(this, MovieActivity.class);
        intent.putExtra(Intent.EXTRA_KEY_EVENT, movie);
        startActivity(intent);
    }

    private void loadMovies() {
        pbLoadingIndicator.setVisibility(View.VISIBLE);
        Toast.makeText(this, getString(R.string.loading), Toast.LENGTH_SHORT).show();
        new MovieTask().execute(url);
    }

    private void showMovies() {
        tvErrorMessage.setVisibility(View.INVISIBLE);
        rvMovies.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        rvMovies.setVisibility(View.INVISIBLE);
        tvErrorMessage.setVisibility(View.VISIBLE);
    }

    /**
     * Performs an asynchronous task to retrieve the information from the
     * {@code api.themoviedb.org}. Upon completion such information is render on the screen. If case
     * of a failure an error message is displayed on the screen.
     */
    private final class MovieTask extends AsyncTask<String, Void, Optional<Movies>> {
        private static final String TAG = "MovieTask";
        private static final String BASE_URL = "https://api.themoviedb.org/3/%s?api_key=%s";
        private static final String API_KEY = "";
        private final Gson gson = new GsonBuilder()
                .registerTypeAdapter(Movies.class, new Movies.Builder())
                .create();

        @Override
        protected Optional<Movies> doInBackground(String... urls) {
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/octet-stream");
            RequestBody body = RequestBody.create(mediaType, "{}");
            Request request = new Request.Builder()
                    .url(String.format(BASE_URL, urls[0], API_KEY))
                    .get()
                    .build();
            try {
                Response response = client.newCall(request).execute();
                Movies movies = gson.fromJson(response.body().string(), Movies.class);
                return Optional.ofNullable(movies);
            } catch (IOException exc) {
                Log.e(TAG, exc.getMessage());
            }
            return Optional.empty();
        }

        @Override
        protected void onPostExecute(Optional<Movies> movies) {
            pbLoadingIndicator.setVisibility(View.INVISIBLE);
            if (movies.isPresent()) {
                MoviesAdapter moviesAdapter =
                        new MoviesAdapter(movies.get().movies(), MainActivity.this);
                rvMovies.setAdapter(moviesAdapter);
                showMovies();
            } else {
                showErrorMessage();
            }
        }
    }
}
