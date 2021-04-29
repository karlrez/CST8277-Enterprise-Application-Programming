package bloodbank.entity;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.AttributeOverride;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.Hibernate;

/**
 * The persistent class for the blood_bank database table.
 */
//TODO BB01 - add the missing annotations.
@Entity
@Table(name = "blood_bank")
@AttributeOverride(name = "id", column = @Column(name = "bank_id"))
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "privately_owned", columnDefinition = "BIT", discriminatorType = DiscriminatorType.INTEGER, length = 1)
//TODO BB02 - BloodBank has subclasses PrivateBloodBank and PublicBoodBank. Look at week 9 slides for InheritanceType.
//TODO BB03 - do we need a mapped super class? which one?
public abstract class BloodBank extends PojoBase implements Serializable {
	private static final long serialVersionUID = 1L;

	// TODO BB04 - add the missing annotations.
	@Basic(optional = false)
	@Column(nullable = false, length = 100)
	private String name;

	// TODO BB05 - add the 1:M annotation. This list should be effected by changes
	// to this object (cascade).
	// TODO BB06 - add the join column details.
	@OneToMany(mappedBy = "bank", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set< BloodDonation> donations;

	protected BloodBank() {
	}

	public Set<BloodDonation> getDonations() {
		return donations;
	}

	public void setDonations(Set<BloodDonation> donations) {
		this.donations = donations;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		return prime + Objects.hash(getName());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (this == obj)
			return true;
		if (!(getClass() == obj.getClass() || Hibernate.getClass(obj) == getClass()))
			return false;
		BloodBank other = (BloodBank) obj;
		return Objects.equals(getName(), other.getName());
	}

}