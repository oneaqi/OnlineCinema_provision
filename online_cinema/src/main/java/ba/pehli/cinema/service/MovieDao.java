package ba.pehli.cinema.service;

import java.util.List;

import ba.pehli.cinema.domain.Movie;
import ba.pehli.cinema.domain.Rating;
import ba.pehli.cinema.domain.User;

/**
 * Service for movies.
 * 
 * @author almir
 *
 */
public interface MovieDao {
	List<Movie> findAll();
	List<Movie> findAll(int page,int size);
	List<Movie> findAllWithRatings();
	int findCount();
	Movie findById(int id);
	Movie save(Movie movie);
	void delete(Movie movie);
}
