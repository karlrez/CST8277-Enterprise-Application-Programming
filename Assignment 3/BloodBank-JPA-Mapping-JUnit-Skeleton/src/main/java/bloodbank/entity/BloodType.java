package bloodbank.entity;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.hibernate.Hibernate;

//TODO BT01 - this class is not an entity however it can be embedded in other entities. add missing annotation.
@Embeddable
public class BloodType {

	// TODO BT02 - add annotations
	@Column(name = "blood_group")
	private String bloodGroup;

	// TODO BT03 - add annotations
	@Column(name = "rhd")
	private byte rhd;

	public BloodType() {
	}

	public String getBloodGroup() {
		return bloodGroup;
	}

	public void setBloodGroup(String bloodGroup) {
		this.bloodGroup = bloodGroup;
	}

	public void setType(String group, String rhd) {
		setBloodGroup(group);
		byte p = 0b1;
		byte n = 0b0;
		setRhd(("+".equals(rhd) ? p : n));
	}

	public byte getRhd() {
		return rhd;
	}

	public void setRhd(byte rhd) {
		this.rhd = rhd;
	}

	@Override
	public int hashCode() {
		return Objects.hash(getBloodGroup(), getRhd());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (this == obj)
			return true;
		if (!(getClass() == obj.getClass() || Hibernate.getClass(obj) == getClass()))
			return false;
		BloodType other = (BloodType) obj;
		return Objects.equals(getBloodGroup(), other.getBloodGroup()) && getRhd() == other.getRhd();
	}
}
