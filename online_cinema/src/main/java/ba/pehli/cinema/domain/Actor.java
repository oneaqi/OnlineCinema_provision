package ba.pehli.cinema.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**
 * Movie actor.
 * 
 * @author almir
 *
 */

@Entity
@Table(name="actor")
public class Actor {
	private int id;
	private String name;
	
	private Set<Movie> filmography = new HashSet<Movie>();
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@Column(name="name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@ManyToMany
	@JoinTable(name="movie_actor", joinColumns=@JoinColumn(name="actor_id"), inverseJoinColumns=@JoinColumn(name="movie_id"))
	public Set<Movie> getFilmography() {
		return filmography;
	}
	public void setFilmography(Set<Movie> filmography) {
		this.filmography = filmography;
	}
	
}
