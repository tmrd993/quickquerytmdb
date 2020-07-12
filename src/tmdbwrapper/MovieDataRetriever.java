package tmdbwrapper;

import java.awt.image.BufferedImage;
import java.io.IOException;
import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

/* Retrieves information about the specified movie as JSON fields using the TMdb API (Version 3)
 * and converts the JSON data into Java objects for easy access
 * Returned values correspond to movie data from the US
 * To get data for other regions, check the TMdb API
 */
public class MovieDataRetriever extends MediaDataRetriever {
    private final String MOVIE_TITLE_FOR_QUERY;

    public MovieDataRetriever(String movieTitle) {
	if (movieTitle == null) {
	    throw new IllegalArgumentException("Movie title can't be null");
	}
	this.MOVIE_TITLE_FOR_QUERY = movieTitle.replace(" ", "+");
    }

    public Movie fetchData() {
	JsonObject movieData = fetchMediaData("movie", MOVIE_TITLE_FOR_QUERY);
	if (movieData == null) {
	    return null;
	} else {
	    String movieStatus = movieData.get("status").getAsString();
	    long budget = movieData.get("budget").getAsLong();
	    String[] genres = setGenres(movieData);
	    String movieTitle = movieData.get("title").getAsString();

	    String releaseDate = movieData.get("release_date").getAsString();
	    if (movieData.get("release_date") == JsonNull.INSTANCE)
		releaseDate = "Unavailable";

	    long revenue = movieData.get("revenue").getAsLong();

	    int runtime = movieData.get("runtime").getAsInt();
	    if (movieData.get("runtime") == JsonNull.INSTANCE)
		runtime = -1;

	    double voteAverage = movieData.get("vote_average").getAsDouble();
	    long voteCount = movieData.get("vote_count").getAsLong();

	    String posterPath = movieData.get("poster_path").getAsString();
	    if (movieData.get("poster_path") == JsonNull.INSTANCE)
		posterPath = null;

	    BufferedImage poster;

	    try {
		poster = getPoster(posterPath);
	    } catch (IOException e) {
		poster = null;
	    }

	    return new Movie(movieTitle, movieStatus, budget, revenue, genres, releaseDate, runtime, voteAverage,
		    voteCount, posterPath, poster);
	}
    }

    /*
     * static factory method that returns an array of movie objects by fetching the
     * data of all movies contained in movieData parameter
     *
     * @param movieData an array of MovieDataRetriever objects
     * 
     * @returns movies an array of movie objects
     */
    public static Movie[] fetchAll(MovieDataRetriever[] movieData) {
	Movie[] movies = new Movie[movieData.length];
	for (int i = 0; i < movies.length; i++) {
	    movies[i] = movieData[i].fetchData();
	}
	return movies;
    }

    @SuppressWarnings("unused")
    private MovieDataRetriever() {
	this.MOVIE_TITLE_FOR_QUERY = null;
    }

    /*
     * static factory method that returns an array of movie data objects based on
     * the query string
     *
     * @params keyword the query string
     * 
     * @returns an array of MovieDataRetriever objects (returns a maximum of 20
     * movies)
     */
    public static MovieDataRetriever[] getMovieDataByQuery(String query) throws IOException {
	JsonObject rootObject = getMediaBasedOnQueryAsJson("movie", query);

	int searchResultSize = rootObject.get("results").getAsJsonArray().size();

	if (searchResultSize == 0)
	    return new MovieDataRetriever[0];

	MovieDataRetriever[] searchResults = new MovieDataRetriever[searchResultSize];

	for (int i = 0; i < searchResultSize; i++) {
	    String movieTitle = rootObject.get("results").getAsJsonArray().get(i).getAsJsonObject().get("title")
		    .getAsString();
	    searchResults[i] = new MovieDataRetriever(movieTitle);
	}

	return searchResults;
    }

    // private helper method to set the genre field
    /*
     * @param movieData an array of MovieDataRetriever objects (returns a maximum of
     * 20 movies)
     * 
     * @returns A String array containing the genres
     */
    private String[] setGenres(JsonObject movieData) {
	// the genre field is a list of json objects in the form: {id: 1, name:
	// genre_1}, {id: 2, name: genre_2} ...
	// only the subfields "name" are of interest here
	JsonArray genresAsJsonArray = movieData.get("genres").getAsJsonArray();

	String[] genres = new String[genresAsJsonArray.size()];
	for (int i = 0; i < genres.length; i++) {
	    genres[i] = genresAsJsonArray.get(i).getAsJsonObject().get("name").getAsString();
	}
	return genres;
    }

    public static void main(String[] args) throws IOException {

	Movie someMovie = new MovieDataRetriever("the avengers").fetchData();
	System.out.println(someMovie.getMovieStatus());
    }
}