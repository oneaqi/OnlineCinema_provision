package ba.pehli.cinema.service;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ba.pehli.cinema.domain.Movie;
import ba.pehli.cinema.domain.Rating;
import ba.pehli.cinema.domain.User;

/**
 * Implementation of service for ratings.
 * 
 * @author almir
 *
 */
@Service("ratingDao")
@Transactional
public class RatingDaoImpl implements RatingDao{
	
private SessionFactory sessionFactory;
	
	@Autowired
	public RatingDaoImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public Rating findById(int id) {
		return (Rating)sessionFactory.getCurrentSession().getNamedQuery("Rating.findById").setParameter("id", id).uniqueResult();
	}

	@Override
	public Rating findByMovieAndUser(Movie movie, User user) {
		return (Rating) sessionFactory.getCurrentSession().getNamedQuery("Rating.findByUserAndMovie").setParameter("userId", user.getId()).setParameter("movieId", movie.getId()).uniqueResult();
	}

	@Override
	public Rating save(Rating rating) {
		sessionFactory.getCurrentSession().saveOrUpdate(rating);
		return rating;
	}

}
