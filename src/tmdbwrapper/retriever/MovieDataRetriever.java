package tmdbwrapper.retriever;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

import tmdbwrapper.model.Movie;
import tmdbwrapper.model.MediaType;

public class MovieDataRetriever extends MediaDataRetriever {
    
    public MovieDataRetriever(String apiKey) {
	super(apiKey);
    }

    public Movie searchExact(String movieTitleForQuery) {
	JsonObject movieData = fetchMediaData(MediaType.MOVIE, movieTitleForQuery);
	if (movieData == null) {
	    return null;
	}
	Movie movie = getMovieFromJsonObject(movieData);
	return movie;
    }
    
    private Movie getMovieFromJsonObject(JsonObject movieData) {
	String movieStatus = movieData.get("status").getAsString();
	long budget = movieData.get("budget").getAsLong();
	String[] genres = getGenresFromJson(movieData);
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

	String posterPath = null;
	if (movieData.get("poster_path") == JsonNull.INSTANCE)
	    posterPath = null;
	else
	    posterPath = movieData.get("poster_path").getAsString();

	BufferedImage poster;

	try {
	    poster = getPoster(posterPath);
	} catch (IOException e) {
	    poster = null;
	}

	return new Movie(movieTitle, movieStatus, budget, revenue, genres, releaseDate, runtime, voteAverage, voteCount,
		posterPath, poster);
    }
    
    public List<Movie> searchByQuery(String query) {
	JsonObject rootObject = Objects.requireNonNull(getMediaBasedOnQueryAsJson(MediaType.MOVIE, query));

	int searchResultSize = rootObject.get("results").getAsJsonArray().size();

	if (searchResultSize == 0)
	    return Collections.emptyList();

	List<Movie> searchResults = new ArrayList<>();
	for (int i = 0; i < searchResultSize; i++) {
	    String movieTitle = rootObject.get("results").getAsJsonArray().get(i).getAsJsonObject().get("title")
		    .getAsString();
	    Movie searchResult = searchExact(movieTitle);
	    if(searchResult != null) {
		searchResults.add(searchResult);
	    }
	}

	return searchResults;
    }
}