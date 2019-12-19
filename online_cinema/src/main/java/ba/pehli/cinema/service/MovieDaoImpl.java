package ba.pehli.cinema.service;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ba.pehli.cinema.domain.Movie;
import ba.pehli.cinema.domain.Rating;
import ba.pehli.cinema.domain.User;

/**
 * Implementation of service for movies in database. Using Hibernate.
 * 
 * @author almir
 *
 */
@Service("movieDao")
@Transactional
public class MovieDaoImpl implements MovieDao{
	
	private SessionFactory sessionFactory;
	
	@Autowired
	public MovieDaoImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	@Transactional(readOnly=true)
	public List<Movie> findAll() {
		return sessionFactory.getCurrentSession().createQuery("select m from Movie m order by m.id").list();
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<Movie> findAll(int page,int size) {
		return sessionFactory.getCurrentSession().createQuery("select m from Movie m order by m.id").setFirstResult(page-1).setMaxResults(size).list();
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<Movie> findAllWithRatings() {
		return sessionFactory.getCurrentSession().getNamedQuery("Movie.findAllWithRatings").list();
	}

	@Override
	@Transactional(readOnly=true)
	public Movie findById(int id) {
		return (Movie)sessionFactory.getCurrentSession().getNamedQuery("Movie.findById").setParameter("id", id).uniqueResult();
	}
	
	@Override
	@Transactional(readOnly=true)
	public int findCount() {
		Long count = (Long)sessionFactory.getCurrentSession().getNamedQuery("Movie.findCount").uniqueResult(); 
		return count.intValue();
	}

	@Override
	public Movie save(Movie movie) {
		sessionFactory.getCurrentSession().saveOrUpdate(movie);
		return movie;
	}

	@Override
	public void delete(Movie movie) {
		sessionFactory.getCurrentSession().delete(movie);
	}
	
}
