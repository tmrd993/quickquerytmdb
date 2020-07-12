package tmdbwrapper;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.imageio.ImageIO;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public abstract class MediaDataRetriever {
    private static final String API_KEY = "<YOUR API KEY HERE>";

    /*
     * guarantees to return a valid JsonObject containing data about the specified
     * movie/tv-show (if it exists) returns null if the movie/tv-show does not exist
     * on the TMdb servers or an error occurs while retrieving the JsonObject
     *
     * @param mediaType Media type as a string ("movie" or "tv-show" )
     * 
     * @param mediaTitle Title of the movie/tv-show as a string
     * 
     * @returns mediaData a valid JsonObject containing all relevant information
     * about the specified movie/tv-show
     */
    protected final JsonObject fetchMediaData(final String mediaType, final String mediaTitle) {
	try {
	    JsonObject mediaData = getMediaDataAsJson(mediaType, mediaTitle);
	    return mediaData;
	} catch (IOException e) {
	    return null;
	}
    }

    /*
     * private helper method that returns a JSON file that contains complete
     * information about a movie/tv-show as a JsonObject
     *
     * @params mediaType Media type as a string ("movie" or "tv-show")
     * 
     * @param mediaTitle Title of the movie/tv-show as a string
     * 
     * @returns rootObject a JsonObject containing all relevant information about
     * the searched for movie/tv-show
     * 
     * @throws IOException if an error occurs while decoding the URL's
     */
    private final JsonObject getMediaDataAsJson(final String mediaType, final String mediaTitle) throws IOException {
	if (!mediaType.equals("movie") && !mediaType.equals("tv")) {
	    throw new IllegalArgumentException("Possible values for mediaType are \"movie\" and \"tv\" only");
	}

	JsonParser mediaParser;
	JsonElement rootElement;
	JsonObject rootObject;

	/*
	 * the data is obtained with a simple HTTP request. To get detailed movie/tv
	 * show information, it is neccessary to do a standard search with limited
	 * return values first to obtain the ID of the movie/tv show. The ID is needed
	 * to start a detailed search for additional information like the runtime, vote
	 * count, vote score, budget ...
	 */
	String standardQuery = "https://api.themoviedb.org/3/search/" + mediaType + "?api_key=" + API_KEY + "&query="
		+ mediaTitle;
	URL standardSearchUrl = new URL(standardQuery);
	HttpURLConnection requestStandardData = (HttpURLConnection) standardSearchUrl.openConnection();
	requestStandardData.connect();

	try (InputStreamReader standardQueryStream = new InputStreamReader(
		(InputStream) requestStandardData.getContent())) {
	    mediaParser = new JsonParser(); // create a new JsonParser instance
	    rootElement = mediaParser.parse(standardQueryStream); // convert the input stream to a json element
	    rootObject = rootElement.getAsJsonObject(); // get the element as a json object
	}

	/*
	 * The http request returns several fields, the field that contains the movie
	 * information is called "results". This field will have a size of zero if the
	 * searched for movie does not exist
	 */
	if (rootObject.get("results").getAsJsonArray().size() == 0)
	    return null;

	// the movie data is contained in the first sub-field of the results field
	// in this case, we only obtain the movie ID to start a detailed query
	String mediaId = rootObject.get("results").getAsJsonArray().get(0).getAsJsonObject().get("id").getAsString();

	// URL format for the detailed query
	String detailedQuery = "https://api.themoviedb.org/3/" + mediaType + "/" + mediaId + "?api_key=" + API_KEY;

	URL detailedQueryUrl = new URL(detailedQuery);
	HttpURLConnection requestDetailed = (HttpURLConnection) detailedQueryUrl.openConnection();
	requestDetailed.connect();

	try (InputStreamReader detailedQueryStream = new InputStreamReader(
		(InputStream) requestDetailed.getContent())) {
	    rootElement = mediaParser.parse(detailedQueryStream); // convert the input stream to a json element
	    rootObject = rootElement.getAsJsonObject(); // get the json element as a json object
	}

	return rootObject;
    }

    /*
     * returns a gson JsonObject that contains several JSON fields with information
     * required to start a detailed search for a collection of movies/tv-shows based
     * on the user submitted query
     *
     * @param mediaType string representing the media type (movie or tv-show)
     * 
     * @param query user submitted query string
     * 
     * @returns rootObject a gson JsonObject containing several JSON fields with
     * name and ID fields
     * 
     * @throws IOException if an error occurs while decoding the URL
     */
    protected static JsonObject getMediaBasedOnQueryAsJson(final String mediaType, final String query)
	    throws IOException {
	if (!mediaType.equals("movie") && !mediaType.equals("tv")) {
	    throw new IllegalArgumentException("Possible values for mediaType are \"movie\" and \"tv\" only");
	}

	String queryString = "https://api.themoviedb.org/3/search/" + mediaType + "?api_key=" + API_KEY + "&query="
		+ query.replace(" ", "+");
	URL searchUrl = new URL(queryString);
	HttpURLConnection searchRequest = (HttpURLConnection) searchUrl.openConnection();

	JsonParser mediaParser;
	JsonElement rootElement;
	JsonObject rootObject;

	try (InputStreamReader standardQueryStream = new InputStreamReader((InputStream) searchRequest.getContent())) {
	    mediaParser = new JsonParser(); // create a new JsonParser instance
	    rootElement = mediaParser.parse(standardQueryStream); // convert the input stream to a json element
	    rootObject = rootElement.getAsJsonObject(); // get the element as a json object
	}

	return rootObject;
    }

    /*
     * returns the poster of the movie
     *
     * @param posterPath filepath of the poster as described in the TMdb API
     * 
     * @returns poster BufferedImage object representing the poster of the
     * movie/tv-show
     * 
     * @throws IOException if an error occurs while decoding the URL
     */
    protected BufferedImage getPoster(final String posterPath) throws IOException {
	// The poster_path field contains the file path as a String that looks like
	// this: /xxxxxxxxxx.png
	if (posterPath == null)
	    return null;

	// The file path of the poster has to be appended to the base url
	String posterUrl = "https://image.tmdb.org/t/p/w185/" + posterPath;
	URL posterQuery = new URL(posterUrl);
	BufferedImage poster = ImageIO.read(posterQuery);

	return poster;
    }
}