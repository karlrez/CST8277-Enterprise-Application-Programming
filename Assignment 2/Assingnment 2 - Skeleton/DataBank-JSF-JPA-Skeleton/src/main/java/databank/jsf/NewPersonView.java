/*****************************************************************
 * File: PersonPojo.java Course materials (21W) CST8277
 *
 * @author Shahriar (Shawn) Emami
 * @author (original) Mike Norman
 */
package databank.jsf;

import java.io.Serializable;

import javax.faces.annotation.ManagedProperty;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import databank.model.PersonPojo;

/**
 * This class represents the scope of adding a new person to the DB.
 * 
 * TODO 09 - This class is a managed been. Use the name "newPerson".<br>
 * TODO 10 - Unlike previous assignment where PersonPojo was view scoped, now this class is.<br>
 * TODO 11 - Add the missing variables and their getters and setters. Have in mind dates and version are internal
 * only.<br>
 * 
 */
@ViewScoped
@Named("newPerson")
public class NewPersonView implements Serializable {
	/** explicit set serialVersionUID */
	private static final long serialVersionUID = 1L;

	protected String firstName;
	protected String lastName;
	protected String phoneNumber;
	protected String email;

	@Inject
	@ManagedProperty( "#{personController}")
	protected PersonController personController;

	public NewPersonView() {
	}

	/**
	 * @return firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName firstName to set
	 */
	public void setFirstName( String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param LastName LastName to set
	 */
	public void setLastName( String lastName) {
		this.lastName = lastName;
	}
	
	/**
	 * @return phoneNumber
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * @param phoneNumber to set
	 */
	public void setPhoneNumber( String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	/**
	 * @return email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email to set
	 */
	public void setEmail( String email) {
		this.email = email;
	}

	public void addPerson() {
		if ( allNotNullOrEmpty( firstName, lastName, phoneNumber, email /* TODO 11 - don't forget the other variables */)) {
			PersonPojo theNewPerson = new PersonPojo();
			theNewPerson.setFirstName( getFirstName());
			theNewPerson.setLastName( getLastName());
			//TODO 12 - call other setters
			theNewPerson.setPhoneNumber(getPhoneNumber());
			theNewPerson.setEmail(getEmail());
			personController.addNewPerson( theNewPerson);
			//clean up
			personController.toggleAdding();
			setFirstName( null);
			setLastName( null);
			//TODO 13 - set everything to null
			setPhoneNumber(null);
			setEmail(null);
		}
	}

	static boolean allNotNullOrEmpty( final Object... values) {
		if ( values == null) {
			return false;
		}
		for ( final Object val : values) {
			if ( val == null) {
				return false;
			}
			if ( val instanceof String) {
				String str = (String) val;
				if ( str.isEmpty()) {
					return false;
				}
			}
		}
		return true;
	}
}