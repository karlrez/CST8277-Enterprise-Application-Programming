/******************************************************
 * File: PersonPojo.java Course materials (21W) CST8277
 *
 * @author Shariar (Shawn) Emami
 * @author (original) Mike Norman
 */
package databank.model;

import java.io.Serializable;
import java.time.Instant;

import javax.faces.view.ViewScoped;

/**
 *
 * Description: model for the Person object <br>
 * a little read about @ViewScoped <br>
 * https://stackoverflow.com/a/6026009/764951
 */
@ViewScoped
public class PersonPojo implements Serializable {
	private static final long serialVersionUID = 1L;

	private int id;
	private String firstName;
	private String lastName;
	private String email;
	private String phoneNumber;
	private Instant created;

	public int getId() {
		return id;
	}

	/**
	 * @param id new value for id
	 */
	public void setId( int id) {
		this.id = id;
	}

	/**
	 * @return the value for firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName new value for firstName
	 */
	public void setFirstName( String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the value for lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName new value for lastName
	 */
	public void setLastName( String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail( String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber( String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public void setCreated( Instant created) {
		this.created = created;
	}

	public void setCreated( long created) {
		this.created = Instant.ofEpochMilli( created);
	}

	public Instant getCreated() {
		return created;
	}

	public long getCreatedEpochMilli() {
		return created.toEpochMilli();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals( Object obj) {
		if ( this == obj) {
			return true;
		}
		if ( !( obj instanceof PersonPojo)) {
			return false;
		}
		PersonPojo other = (PersonPojo) obj;
		if ( id != other.id) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append( "Person [id=").append( id).append( ", ");
		if ( firstName != null) {
			builder.append( "firstName=").append( firstName).append( ", ");
		}
		if ( lastName != null) {
			builder.append( "lastName=").append( lastName).append( ", ");
		}
		if ( phoneNumber != null) {
			builder.append( "phoneNumber=").append( phoneNumber).append( ", ");
		}
		if ( email != null) {
			builder.append( "email=").append( email).append( ", ");
		}
		builder.append( "]");
		return builder.toString();
	}

}