package tmdbwrapper.retriever;

import java.awt.image.BufferedImage;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Objects;

import javax.imageio.ImageIO;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import tmdbwrapper.model.MediaType;

public abstract class MediaDataRetriever {
    protected final String API_KEY;
    
    protected MediaDataRetriever(String apiKey){
	this.API_KEY = Objects.requireNonNull(apiKey, "API-Key can not be null.");
    }
    
    protected JsonObject fetchMediaData(MediaType mediaType, String mediaTitle) {
	JsonObject mediaData = null;
	try {
	    mediaData = getMediaDataAsJson(mediaType, mediaTitle);
	} catch (IOException e) {
	    e.printStackTrace();
	}
	return mediaData;
    }

    private JsonObject getMediaDataAsJson(MediaType mediaType, String mediaTitle) throws IOException {
	String standardQuery = "https://api.themoviedb.org/3/search/" + encode(mediaType.toString().toLowerCase()) + "?api_key=" + API_KEY + "&query="
		+ encode(mediaTitle);
	
	JsonObject standardQueryJsonObject = getJsonObjectFromTmdbQuery(standardQuery);

	if (standardQueryJsonObject.get("results").getAsJsonArray().size() == 0)
	    return null;

	// the movie data is contained in the first sub-field of the results field
	// in this case, we only obtain the movie ID to start a detailed query
	String mediaId = standardQueryJsonObject.get("results").getAsJsonArray().get(0).getAsJsonObject().get("id").getAsString();

	String detailedQuery = "https://api.themoviedb.org/3/" + encode(mediaType.toString().toLowerCase()) + "/" + mediaId + "?api_key=" + API_KEY;

	JsonObject rootJsonObject = getJsonObjectFromTmdbQuery(detailedQuery);

	return rootJsonObject;
    }
    
    private JsonObject getJsonObjectFromTmdbQuery(String queryUrlString) throws IOException {
	JsonParser mediaParser;
	JsonElement rootElement;
	JsonObject rootObject;

	URL standardSearchUrl = new URL(queryUrlString);
	HttpURLConnection requestStandardData = (HttpURLConnection) standardSearchUrl.openConnection();
	requestStandardData.connect();

	try (InputStreamReader standardQueryStream = new InputStreamReader(
		(InputStream) requestStandardData.getContent())) {
	    mediaParser = new JsonParser();
	    rootElement = mediaParser.parse(standardQueryStream);
	    rootObject = rootElement.getAsJsonObject();
	}
	
	return rootObject;
    }

    protected JsonObject getMediaBasedOnQueryAsJson(MediaType mediaType, String query) {
	String queryString = "https://api.themoviedb.org/3/search/" + encode(mediaType.toString().toLowerCase()) + "?api_key=" + API_KEY + "&query="
		+ encode(query);
	JsonObject rootObject = null;
	try {
	    rootObject = getJsonObjectFromTmdbQuery(queryString);
	} catch (IOException e) {
	    e.printStackTrace();
	}
	return rootObject;
    }

    protected BufferedImage getPoster(String posterPath) throws IOException {
	if (posterPath == null)
	    return null;
	
	String posterUrl = "https://image.tmdb.org/t/p/w185/" + posterPath;
	URL posterQuery = new URL(posterUrl);
	BufferedImage poster = ImageIO.read(posterQuery);
	return poster;
    }
    
    protected String[] getGenresFromJson(JsonObject tvData) {
	JsonArray genresAsJsonArray = tvData.get("genres").getAsJsonArray();
	String[] genres = new String[genresAsJsonArray.size()];
	for (int i = 0; i < genres.length; i++) {
	    genres[i] = genresAsJsonArray.get(i).getAsJsonObject().get("name").getAsString();
	}
	return genres;
    }
    
    protected static String encode(String query) {
	try {
	    return URLEncoder.encode(query, "UTF-8");
	} catch (UnsupportedEncodingException e) {
	    e.printStackTrace();
	}
	return null;
    }
}