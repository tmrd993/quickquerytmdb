package tmdbwrapper.model;

import java.awt.image.BufferedImage;

public class TvShow {
    private final String TV_TITLE;
    private final String TV_STATUS;
    private final String[] GENRES;
    private final String FIRST_AIR_DATE;
    private final String LAST_AIR_DATE;
    private final String NETWORK;
    private final int NUMBER_OF_SEASONS;
    private final double VOTE_AVERAGE;
    private final long VOTE_COUNT;
    private final String POSTER_PATH;
    private final BufferedImage POSTER;

    public TvShow(String tvTitle, String tvStatus, String[] genres, String firstAirDate, String lastAirDate,
	    String network, int numOfSeasons, double voteAverage, long voteCount, String posterPath,
	    BufferedImage poster) {
	this.TV_TITLE = tvTitle;
	this.TV_STATUS = tvStatus;
	this.GENRES = genres;
	this.FIRST_AIR_DATE = firstAirDate;
	this.LAST_AIR_DATE = lastAirDate;
	this.NETWORK = network;
	this.NUMBER_OF_SEASONS = numOfSeasons;
	this.VOTE_AVERAGE = voteAverage;
	this.VOTE_COUNT = voteCount;
	this.POSTER_PATH = posterPath;
	this.POSTER = poster;
    }

    public String getTitle() {
	return TV_TITLE;
    }

    /*
     * returns the status of the tv-show possible values are: "Rumored", "Planned",
     * "In Production", "Post Production", "Released", "Canceled"
     *
     * @returns TV_STATUS a string representing the state of the tv-show
     */
    public String getStatus() {
	return TV_STATUS;
    }

    public String[] getGenres() {
	return GENRES;
    }

    /*
     * returns the US air date of the first episode
     *
     * @returns FIRST_AIR_DATE a string representing the us air date
     */
    public String getFirstAirDate() {
	return FIRST_AIR_DATE;
    }

    public String getLastAirDate() {
	return LAST_AIR_DATE;
    }

    public String getNetwork() {
	return NETWORK;
    }

    public int getNumberOfSeasons() {
	return NUMBER_OF_SEASONS;
    }

    public double getVoteAverage() {
	return VOTE_AVERAGE;
    }

    public long getVoteCount() {
	return VOTE_COUNT;
    }

    @Override
    public String toString() {
	String tvShowInformation = "Title: " + this.TV_TITLE + "\n" + "Status: " + this.TV_STATUS + "\n" + "Genres: "
		+ java.util.Arrays.toString(this.GENRES) + "\n" + "Network: " + this.NETWORK + "\n"
		+ "Number of seasons: " + this.NUMBER_OF_SEASONS + "\n" + "First air date: " + this.FIRST_AIR_DATE
		+ "\n" + "Last air date: " + this.LAST_AIR_DATE + "\n" + "TMdb vote count: " + this.VOTE_COUNT + "\n"
		+ "TMdb vote average: " + this.VOTE_AVERAGE;

	if (this.POSTER_PATH != null) {
	    tvShowInformation = tvShowInformation + "\nPoster file path: " + this.POSTER_PATH;
	}

	return tvShowInformation;
    }

    public BufferedImage getPoster() {
	return POSTER;
    }

}