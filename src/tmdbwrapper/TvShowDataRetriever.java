package tmdbwrapper;

import java.awt.image.BufferedImage;
import java.io.IOException;
import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

/* Retrieves information about the specified tv-show as JSON fields using the TMdb API (Version 3)
 * and converts the JSON data into Java objects for easy access
 * Returned values correspond to movie data from the US
 * To get data for other regions, check the TMdb API
 */
public class TvShowDataRetriever extends MediaDataRetriever {
    private final String TV_TITLE_FOR_QUERY;

    public TvShowDataRetriever(String tvShowTitle) {
	if (tvShowTitle == null) {
	    throw new IllegalArgumentException("TV-show title can't be null");
	}
	this.TV_TITLE_FOR_QUERY = tvShowTitle.replace(" ", "+");
    }

    public TvShow fetchData() {
	JsonObject tvData = fetchMediaData("tv", TV_TITLE_FOR_QUERY);
	if (tvData == null) {
	    return null;
	}
	String tvTitle = tvData.get("name").getAsString();
	String tvStatus = tvData.get("status").getAsString();
	String[] genres = getGenresFromJson(tvData);

	String firstAirDate = tvData.get("first_air_date").getAsString();
	if (tvData.get("first_air_date") == JsonNull.INSTANCE)
	    firstAirDate = "Unavailable";

	String lastAirDate = tvData.get("last_air_date").getAsString();
	if (tvData.get("last_air_date") == JsonNull.INSTANCE)
	    lastAirDate = "Unavailable";

	String network = tvData.get("networks").getAsJsonArray().get(0).getAsJsonObject().get("name").getAsString();
	if (tvData.get("networks").getAsJsonArray().size() == 0)
	    network = "Unavailable";

	int numOfSeasons = tvData.get("number_of_seasons").getAsInt();
	double voteAverage = tvData.get("vote_average").getAsDouble();
	long voteCount = tvData.get("vote_count").getAsLong();

	String posterPath = tvData.get("poster_path").getAsString();
	if (tvData.get("poster_path") == JsonNull.INSTANCE) {
	    posterPath = null;
	}

	BufferedImage poster;
	try {
	    poster = getPoster(posterPath);
	} catch (IOException e) {
	    poster = null;
	}

	return new TvShow(tvTitle, tvStatus, genres, firstAirDate, lastAirDate, network, numOfSeasons, voteAverage,
		voteCount, posterPath, poster);

    }

    @SuppressWarnings("unused")
    private TvShowDataRetriever() {
	this.TV_TITLE_FOR_QUERY = null;
    }

    /*
     * returns an array of TvShow objects by fetching the data of all
     * TvShowDataRetriever objects inside the parameter tvShowData
     *
     * @param tvShowData an array of TvShowDataRetriever objects
     * 
     * @returns tvShows an array of TvShow objects
     */
    public static TvShow[] fetchAll(TvShowDataRetriever[] tvShowData) {
	TvShow[] tvShows = new TvShow[tvShowData.length];
	for (int i = 0; i < tvShows.length; i++) {
	    tvShows[i] = tvShowData[i].fetchData();
	}
	return tvShows;
    }

    /*
     * returns an array of TvShowDataRetriever objects based on the user submitted
     * query String returns an empty array if no tv-shows match the query
     *
     * @param query user submitted query string
     * 
     * @returns searchResults an array of TvShowDataRetriever objects (the search
     * results)
     * 
     * @throws NullPointerException if the query string is null
     * 
     * @throws IOException if an error occurs while decoding the search URL
     */
    public static TvShowDataRetriever[] getTvShowDataByQuery(String query) throws IOException {
	if (query == null)
	    throw new NullPointerException();

	JsonObject rootObject = getMediaBasedOnQueryAsJson("tv", query.replace(" ", "+"));

	int searchResultSize = rootObject.get("results").getAsJsonArray().size();

	if (searchResultSize == 0)
	    return new TvShowDataRetriever[0];

	TvShowDataRetriever[] searchResults = new TvShowDataRetriever[searchResultSize];
	for (int i = 0; i < searchResultSize; i++) {
	    String tvShowTitle = rootObject.get("results").getAsJsonArray().get(i).getAsJsonObject().get("name")
		    .getAsString();
	    searchResults[i] = new TvShowDataRetriever(tvShowTitle);
	}
	return searchResults;
    }

    /*
     * helper method to extract the genres from the JsonObject object returns a
     * String array representing the genres returns an empty array if no genres are
     * available for the given tv-show
     *
     * @params tvData a JsonObject containing the relevant JSON data
     * 
     * @returns genres a String array containing the genres
     */
    private String[] getGenresFromJson(JsonObject tvData) {
	JsonArray genresAsJsonArray = tvData.get("genres").getAsJsonArray();

	String[] genres = new String[genresAsJsonArray.size()];
	for (int i = 0; i < genres.length; i++) {
	    genres[i] = genresAsJsonArray.get(i).getAsJsonObject().get("name").getAsString();
	}
	return genres;
    }

    public static void main(String[] args) throws IOException {
	TvShowDataRetriever show = new TvShowDataRetriever(null);
	System.out.println(show.toString());
    }
}