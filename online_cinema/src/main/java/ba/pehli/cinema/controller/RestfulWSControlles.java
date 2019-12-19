package ba.pehli.cinema.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import ba.pehli.cinema.domain.Movie;
import ba.pehli.cinema.domain.Movies;
import ba.pehli.cinema.service.MovieDao;

/**
 * Controller for handling web services (restful ws).
 * @author almir
 *
 */
@Controller
@RequestMapping(value="/ws")
public class RestfulWSControlles {
	private MovieDao movieDao;
	
	/**
	 * Lists all movies wrapped in Movies object. Actual transformation in JSON/XML is
	 * handled by Castor library, as defined in <mvc:message-converters>. 
	 * @return Movies object
	 */
	@RequestMapping(value="/list", method=RequestMethod.GET)
	@ResponseBody
	public Movies list(){
		return new Movies(movieDao.findAll());
	}
	
	/**
	 * Lists informations about specific movie. Actual transformation in JSON/XML is
	 * handled by Castor library, as defined. 
	 * @param id movie id
	 * @return Movie
	 */
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	@ResponseBody
	public Movie getMovieInformation(@PathVariable("id") int id){
		Movie movie = movieDao.findById(id);
		return movie;
	}
	
	@Autowired
	public void setMovieDao(MovieDao movieDao) {
		this.movieDao = movieDao;
	}
	
	
}
