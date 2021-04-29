package bloodbank.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.Hibernate;

/**
 * The persistent class for the phone database table.
 */
//TODO PH01 - add the missing annotations.
//TODO PH02 - do we need a mapped super class? which one?
@Entity
@Table(name = "phone")
@AttributeOverride(name = "id", column = @Column(name = "phone_id"))
public class Phone extends PojoBase implements Serializable {
	private static final long serialVersionUID = 1L;

	// TODO PH03 - add missing annotations.
	@Basic(optional = false)
	@Column(name = "area_code", nullable = false, length = 10)
	private String areaCode;

	// TODO PH04 - add missing annotations.
	@Basic(optional = false)
	@Column(name = "country_code", nullable = false, length = 10)
	private String countryCode;

	// TODO PH05 - add missing annotations.
	@Basic(optional = false)
	@Column(nullable = false, length = 10)
	private String number;

	// TODO PH06 - add annotations for 1:M relation. insertable, updatable are
	// false. remove should not cascade.
	@OneToMany(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "phone_id", referencedColumnName = "phone_id", insertable = false, updatable = false)
	private Set<Contact> contacts = new HashSet<>();

	public Phone setNumber(String countryCode, String areaCode, String number) {
		setCountryCode(countryCode);
		setAreaCode(areaCode);
		setNumber(number);

		return this;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public Set<Contact> getContacts() {
		return contacts;
	}

	public void setContacts(Set<Contact> contacts) {
		this.contacts = contacts;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		return prime + Objects.hash(getAreaCode(), getCountryCode(), getNumber());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (this == obj)
			return true;
		if (!(getClass() == obj.getClass() || Hibernate.getClass(obj) == getClass()))
			return false;
		Phone other = (Phone) obj;
		return Objects.equals(getAreaCode(), other.getAreaCode())
				&& Objects.equals(getCountryCode(), other.getCountryCode())
				&& Objects.equals(getNumber(), other.getNumber());
	}

}