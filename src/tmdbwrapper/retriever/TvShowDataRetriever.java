package tmdbwrapper.retriever;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

import tmdbwrapper.model.MediaType;
import tmdbwrapper.model.TvShow;

public class TvShowDataRetriever extends MediaDataRetriever {

    public TvShowDataRetriever(String apiKey) {
	super(apiKey);
    }

    public TvShow searchExact(String tvTitleForQuery) {
	JsonObject tvData = fetchMediaData(MediaType.TV_SHOW, tvTitleForQuery);
	if (tvData == null) {
	    return null;
	}
	TvShow tvShow = getTvShowFromJsonObject(tvData);
	return tvShow;
    }
    
    private TvShow getTvShowFromJsonObject(JsonObject tvData) {
	String tvTitle = tvData.get("name").getAsString();
	String tvStatus = tvData.get("status").getAsString();
	String[] genres = getGenresFromJson(tvData);

	String firstAirDate = "";
	if (tvData.get("first_air_date") == JsonNull.INSTANCE)
	    firstAirDate = "Unavailable";
	else
	    firstAirDate = tvData.get("first_air_date").getAsString();
	
	String lastAirDate = "";
	if (tvData.get("last_air_date") == JsonNull.INSTANCE)
	    lastAirDate = "Unavailable";
	else
	   lastAirDate = tvData.get("last_air_date").getAsString();

	String network = "";
	if (tvData.get("networks").getAsJsonArray().size() == 0)
	    network = "Unavailable";
	else
	    network = tvData.get("networks").getAsJsonArray().get(0).getAsJsonObject().get("name").getAsString();
	
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

    public List<TvShow> searchByQuery(String query) {
	JsonObject rootObject = Objects.requireNonNull(getMediaBasedOnQueryAsJson(MediaType.TV_SHOW, query));
	
	int searchResultSize = rootObject.get("results").getAsJsonArray().size();

	if (searchResultSize == 0)
	    return Collections.emptyList();

	List<TvShow> searchResults = new ArrayList<>();
	for (int i = 0; i < searchResultSize; i++) {
	    String tvShowTitle = rootObject.get("results").getAsJsonArray().get(i).getAsJsonObject().get("name")
		    .getAsString();
	    TvShow searchResult= searchExact(tvShowTitle);
	    if(searchResult != null) {
		searchResults.add(searchResult);
	    }
	}
	return searchResults;
    }


}