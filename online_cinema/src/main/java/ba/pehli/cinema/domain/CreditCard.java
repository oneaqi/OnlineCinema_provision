package ba.pehli.cinema.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * Credit card of user
 * 
 * @author almir
 *
 */
@Entity
@Table(name="credit_cards")
public class CreditCard {
	private int id;
	private String issuer;
	private String number;
	private User user;
		
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@NotEmpty(message="{validation.field.notempty}")
	@Column(name="issuer")
	public String getIssuer() {
		return issuer;
	}
	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}
	
	@NotEmpty(message="{validation.field.notempty}")
	@Column(name="card_number")
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	
	
	@OneToOne(optional=false,mappedBy="creditCard")
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String toString() {
		return "[" + getIssuer() + " " + getNumber() + "]";
	}
}
