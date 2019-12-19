package ba.pehli.cinema.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * Every user can rate the movies with grades 1 - 5. This is one such rating.
 * 
 * @author almir
 *
 */
@Entity
@Table(name="ratings")
@NamedQueries({
	@NamedQuery(name="Rating.findById",query="select r from Rating r left join fetch r.user u left join fetch r.movie m where r.id=:id"),
	@NamedQuery(name="Rating.findByUserAndMovie",query="select r from Rating r left join fetch r.user u left join fetch r.movie m where r.user.id=:userId and r.movie.id=:movieId")
})
public class Rating {
	private int id;
	private int rating;
	private User user;
	private Movie movie;
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	@Column(name="rating")
	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}
	
	@ManyToOne
	@JoinColumn(name="user_id")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	@ManyToOne
	@JoinColumn(name="movie_id")
	public Movie getMovie() {
		return movie;
	}

	public void setMovie(Movie movie) {
		this.movie = movie;
	}
	
	
	
}
