package tmdbwrapper;

import java.awt.image.BufferedImage;

public class Movie {
    private final String MOVIE_TITLE;
    private final String MOVIE_STATUS;
    private final long BUDGET;
    private final long REVENUE;
    private final String[] GENRES;
    private final String RELEASE_DATE;
    private final int RUNTIME;
    private final double VOTE_AVERAGE;
    private final long VOTE_COUNT;
    private final String POSTER_PATH;
    private final BufferedImage POSTER;

    public Movie(String movieTitle, String movieStatus, long budget, long revenue, String[] genres, String releaseDate,
	    int runtime, double voteAverage, long voteCount, String posterPath, BufferedImage poster) {
	this.MOVIE_TITLE = movieTitle;
	this.MOVIE_STATUS = movieStatus;
	this.BUDGET = budget;
	this.REVENUE = revenue;
	this.GENRES = genres;
	this.RELEASE_DATE = releaseDate;
	this.RUNTIME = runtime;
	this.VOTE_AVERAGE = voteAverage;
	this.VOTE_COUNT = voteCount;
	this.POSTER_PATH = posterPath;
	this.POSTER = poster;
    }

    public String getMovieTitle() {
	return MOVIE_TITLE;
    }

    public String getMovieStatus() {
	return MOVIE_STATUS;
    }

    public String[] getGenres() {
	return GENRES;
    }

    public String getReleaseDate() {
	return RELEASE_DATE;
    }

    public int getRuntime() {
	return RUNTIME;
    }

    public long getVoteCount() {
	return VOTE_COUNT;
    }

    public double getVoteAverage() {
	return VOTE_AVERAGE;
    }

    public long getBudget() {
	return BUDGET;
    }

    public long getRevenue() {
	return REVENUE;
    }

    @Override
    public String toString() {
	String movieInformation = "Title: " + this.MOVIE_TITLE + "\n" + "Status: " + this.MOVIE_STATUS + "\n"
		+ "Genres: " + java.util.Arrays.toString(this.GENRES) + "\n" + "US release date: " + this.RELEASE_DATE
		+ "\n" + "Runtime: " + this.RUNTIME + " mins\n" + "Revenue (USD): " + this.REVENUE + "\n"
		+ "Budget (USD): " + this.BUDGET + "\n" + "TMdb vote count: " + this.VOTE_COUNT + "\n"
		+ "TMdb vote average: " + this.VOTE_AVERAGE;
	if (POSTER_PATH != null) {
	    movieInformation = movieInformation + "\nPoster file path: " + POSTER_PATH;
	}

	return movieInformation;
    }

    public BufferedImage getPoster() {
	return this.POSTER;
    }
}