/*****************************************************
 * File: Customer.java Course materials (21W) CST 8277
 * 
 * @date January 16, 2021
 * @author Shariar (Shawn) Emami
 * @date 2020 09
 * @author (original) Mike Norman
 */
package jdbccmd;

import java.io.Serializable;
import java.util.Date;

/**
 * Simple POJO Object for person.
 * 
 * @author Shariar (Shawn) Emami
 * @version Jan 18, 2021
 */
public class Person implements Serializable {
	private static final long serialVersionUID = 1L;

	protected int id;
	protected String firstName;
	protected String lastName;
	protected String email;
	protected String phoneNumber;
	protected Date created;

	public int getId() {
		return id;
	}

	public void setId( int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName( String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

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

	public Date getCreated() {
		return created;
	}

	public void setCreated( Date created) {
		this.created = created;
	}

	@Override
	public int hashCode() {
		return 1;
	}

	@Override
	public boolean equals( Object obj) {
		if ( this == obj) {
			return true;
		}
		if ( !( obj instanceof Person)) {
			return false;
		}
		Person other = (Person) obj;
		return id != other.id;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append( "Customer[id=").append( id);
		if ( firstName != null) {
			builder.append( ", fName=").append( firstName);
		}
		if ( lastName != null) {
			builder.append( ", lName=").append( lastName);
		}
		if ( email != null) {
			builder.append( ", email=").append( email);
		}
		if ( phoneNumber != null) {
			builder.append( ", phone#=").append( phoneNumber);
		}
		if ( created != null) {
			builder.append( ", created=").append( created);
		}
		return builder.append( "]").toString();
	}
}