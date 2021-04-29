package bloodbank.entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.AttributeOverride;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.Hibernate;

/**
 * The persistent class for the blood_donation database table.
 */
//TODO BD01 - add the missing annotations.
@Entity
@Table(name = "blood_donation")
@AttributeOverride(name = "id", column = @Column(name = "donation_id"))
//TODO BD02 - do we need a mapped super class? which one? // Nope
public class BloodDonation extends PojoBase implements Serializable {
	private static final long serialVersionUID = 1L;

	// TODO BD03 - add annotations for M:1. changes to this class should cascade to
	// BloodBank.
	// UML shows 1:1 ?
	@ManyToOne(cascade = CascadeType.ALL, optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "bank_id", referencedColumnName = "bank_id", nullable = false)
	private BloodBank bank;

	// TODO BD04 - add annotations for 1:1. changes to this class should not cascade
	// to DonationRecord.
	// TODO BD05 - this object should not be insertable or updatable;
	@OneToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "donation_id", referencedColumnName = "donation_id", nullable = true, insertable = false, updatable = false)
	private DonationRecord record;

	// TODO BD06 - add annotations
	@Basic(optional = false)
	@Column(nullable = false, length = 100)
	private int milliliters;

	// TODO BD07 - this object is embedded
	@Embedded
	private BloodType bloodType;

	public BloodBank getBank() {
		return bank;
	}

	public void setBank(BloodBank bank) {
		this.bank = bank;
	}

	public DonationRecord getRecord() {
		return record;
	}

	public void setRecord(DonationRecord record) {
		this.record = record;
	}

	public int getMilliliters() {
		return milliliters;
	}

	public void setMilliliters(int milliliters) {
		this.milliliters = milliliters;
	}

	public BloodType getBloodType() {
		return bloodType;
	}

	public void setBloodType(BloodType bloodType) {
		this.bloodType = bloodType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		return prime + Objects.hash(getBank().getId(), getBloodType(), getMilliliters());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (this == obj)
			return true;
		if (!(getClass() == obj.getClass() || Hibernate.getClass(obj) == getClass()))
			return false;
		BloodDonation other = (BloodDonation) obj;
		return Objects.equals(getBank().getId(), other.getBank().getId())
				&& Objects.equals(getBloodType(), other.getBloodType()) && getMilliliters() == other.getMilliliters();
	}
}