package bloodbank.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

//TODO PBB01 - add missing annotations, week 9 slides page 15. value 1 is private and value 0 is public.
@Entity
@DiscriminatorValue(value = "0")
public class PublicBloodBank extends BloodBank {

	private static final long serialVersionUID = 1L;

	public PublicBloodBank() {

	}
}
