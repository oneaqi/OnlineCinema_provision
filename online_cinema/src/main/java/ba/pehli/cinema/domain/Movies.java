package ba.pehli.cinema.domain;

import java.util.List;

/**
 * Utility class that support web service which gets all movies in one request (castor oxm)
 * @author almir
 *
 */
public class Movies {
	private List<Movie> movies;
	
	public Movies() {
		
	}

	public Movies(List<Movie> movies) {
		this.movies = movies;
	}

	public List<Movie> getMovies() {
		return movies;
	}

	public void setMovies(List<Movie> movies) {
		this.movies = movies;
	}
	
	
}
