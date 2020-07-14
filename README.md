## quickquerytmdb
Small library for basic queries on The Movie Database (themoviedb.org)

To get an API-Key, read this:  
https://developers.themoviedb.org/3/getting-started/introduction
# Features
- Get movie details (Title, Genre, Release date, Runtime ...)
- Get tv-show details (Title, Genre, Number of seasons, Last air date ...)
- Get movie/tv-show poster
- Get collection of movies based on query
- Get collection of tv-shows based on query

# How-to
Note that this library can't be used without an API-Key. Use the link above to get one.  
Once you have an API-Key, replace the empty API_KEY field in the MediaDataRetriever class with your own key.

```java
public abstract class MediaDataRetriever {
    private static final String API_KEY = "<YOUR API KEY HERE>";
    ......
```
## Searching for specific movies/TV-shows

To search for a specific movie, simply create a MovieDataRetriever object and call the fetch method. A few examples on how it works can be seen below.  

```Java
MovieDataRetriever retriever = new MovieDataRetriever("Hereditary");
Movie someMovie = retriever.fetchData();
```

The fetchData Method returns the actual data. More specifically, it returns an immutable Movie object which has several convenience methods to access the data. Calling the overridden toString() method returns the String representation of the Movie object. For the query above, this representation looks like this

```
Title: Hereditary
Status: Released
Genres: [Horror, Mystery, Thriller]
US release date: 2018-06-07
Runtime: 127 mins
Revenue (USD): 79336821
Budget (USD): 10000000
TMdb vote count: 4076
TMdb vote average: 7.1
Poster file path: /lHV8HHlhwNup2VbpiACtlKzaGIQ.jpg
```

Which is just a summary of all fields containing movie data in the Movie class. It's also possible to access the fields separately (using getters).

Note that searching for a TV-show using the MovieDataRetriever will **not** work. Use the TvDataRetriever class for TV-shows.

```java
TvShowDataRetriever retriever = new TvShowDataRetriever("The Wire");
TvShow show = retriever.fetchData();
```

Also note that TvShows do not have the same fields as movies. 

For queries using the Retriever constructors, the search term (title of the movie/show) has to be **exact**. The constructors aren't case-sensitive however, so the following queries would return the same movie.

```java
MovieDataRetriever retriever = new MovieDataRetriever("Hereditary");
retriever = new MovieDataRetriever("hereditary");
retriever = new MovieDataRetriever("heReDitaRy");
retriever = new MovieDataRetriever("HEREDITARY");
```

## Non-exact search

Using a query string and the static factory method getMovieDataByQuery which has the following signature, it is possible to conduct non exact queries.
```
public static MovieDataRetriever[] getMovieDataByQuery(String query)
```

The method returns an array of MovieDataRetriever objects based on the given search query. You can fetch the data one by one or use the method
```
public static Movie[] fetchAll(MovieDataRetriever[] movieData)
```
which returns an array of Movie objects based on the search. It is also possible to save unrelated MovieDataRetriever objects and call the fetchAll method.

An example query looks like this
```java
MovieDataRetriever[] retrievers = getMovieDataByQuery("dracula");
Movie[] movies = fetchAll(retrievers);
```

Which would return a maximum of 20 movies based on the given query. 

The same process can be repeated for TV-shows

```java
TvShowDataRetriever[] retrievers = TvShowDataRetriever.getTvShowDataByQuery("walking dead");
TvShow[] shows = TvShowDataRetriever.fetchAll(retrievers);
```

# Example client with GUI    
![moviequery](screenshots/movieexample.png)  

## Links
- Google gson library (required to convert JSON files into Java objects): https://github.com/google/gson
- themoviedb.org API (Version 3): https://www.themoviedb.org/documentation/api
