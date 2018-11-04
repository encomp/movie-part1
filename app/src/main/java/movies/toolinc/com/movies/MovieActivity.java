package movies.toolinc.com.movies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import movies.toolinc.com.movies.model.Movie;

/**
 * This activity displays the detail for a given {@link Movie}.
 */
public class MovieActivity extends AppCompatActivity {
    private static final String BASE_URL = "https://image.tmdb.org/t/p/w500/%s";

    private ImageView ivPoster;
    private TextView tvTitle;
    private TextView tvOverview;
    private TextView tvVoteAverage;
    private TextView tvReleaseDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        ivPoster = (ImageView) findViewById(R.id.iv_movie_poster);
        tvTitle = (TextView) findViewById(R.id.tv_movie_title);
        tvOverview = (TextView) findViewById(R.id.tv_movie_overview);
        tvVoteAverage = (TextView) findViewById(R.id.tv_vote_average);
        tvReleaseDate = (TextView) findViewById(R.id.tv_release_date);

        if (getIntent().hasExtra(Intent.EXTRA_KEY_EVENT)) {
            Movie movie = (Movie) getIntent().getSerializableExtra(Intent.EXTRA_KEY_EVENT);
            Picasso.get()
                    .load(String.format(BASE_URL, movie.posterPath()))
                    .into(ivPoster);
            tvTitle.setText(movie.originalTitle());
            tvOverview.setText(movie.overview());
            tvVoteAverage.setText(movie.voteAverage());
            tvReleaseDate.setText(movie.releaseDate());
        }
    }
}
