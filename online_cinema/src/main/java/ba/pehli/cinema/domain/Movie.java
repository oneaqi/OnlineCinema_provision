package ba.pehli.cinema.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Movie domain object.
 * 
 * @author almir
 */

@Entity
@Table(name="movie")
@NamedQueries({
	@NamedQuery(name="Movie.findById", query="select m from Movie m where m.id = :id"),
	@NamedQuery(name="Movie.findAllWithRatings",query="select distinct m from Movie m  left join fetch m.ratings r"),
	@NamedQuery(name="Movie.findCount", query="select count(*) from Movie"),
})
public class Movie {
	private int id;
	private String name;
	private Date releaseDate;
	private String description;
	private String wiki;
	private String actors;
	private String director;
	private byte[] image;
	private String trailerUrl;
	private int version;
	
	private Set<Rating> ratings = new HashSet<Rating>();
	
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
	@NotEmpty(message="{validation.field.notempty}")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@NotNull(message="{validation.field.notempty}")
	@DateTimeFormat(pattern="dd.MM.yyyy")
	@Column(name="release_date")
	public Date getReleaseDate() {
		return releaseDate;
	}
	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}
	
	@Column(name="description")
	@NotEmpty(message="{validation.field.notempty}")
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Basic(fetch=FetchType.LAZY)
	@Lob
	@Column(name="image")
	public byte[] getImage() {
		return image;
	}
	public void setImage(byte[] image) {
		this.image = image;
	}
	
	@Column(name="trailer_url")
	@NotEmpty(message="{validation.field.notempty}")
	public String getTrailerUrl() {
		return trailerUrl;
	}
	public void setTrailerUrl(String trailerUrl) {
		this.trailerUrl = trailerUrl;
	}
	
	@Version
	@Column(name="version")
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	
	@OneToMany(mappedBy="movie")
	public Set<Rating> getRatings() {
		return ratings;
	}
	
	@Column(name="actors")
	public String getActors() {
		return actors;
	}
	public void setActors(String actors) {
		this.actors = actors;
	}
	
	@Column(name="director")
	public String getDirector() {
		return director;
	}
	public void setDirector(String director) {
		this.director = director;
	}
	public void setRatings(Set<Rating> ratings) {
		this.ratings = ratings;
	}
	
	@Column(name="wiki")
	public String getWiki() {
		return wiki;
	}
	public void setWiki(String wiki) {
		this.wiki = wiki;
	}
	public String toString() {
		return "[" + id + " " + name + "]"; 
	}
	
}
