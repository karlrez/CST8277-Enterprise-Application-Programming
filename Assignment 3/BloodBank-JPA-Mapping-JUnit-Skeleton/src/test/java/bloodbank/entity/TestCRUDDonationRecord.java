package bloodbank.entity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import common.JUnitBase;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestCRUDDonationRecord extends JUnitBase {

	private EntityManager em;
	private EntityTransaction et;

	private static BloodBank bloodBank;
	private static String NAME = "Karls Blood Bank";
	private static Set<BloodDonation> donations;
	private static BloodType bloodType;
	private static DonationRecord donationRecord;
	private static Person person;

	@BeforeAll
	static void setupAllInit() {
		// Creating a single PrivateBloodBank object

		bloodBank = new PrivateBloodBank();
		BloodDonation bloodDonation = new BloodDonation();

		// BloodType
		bloodType = new BloodType();
		bloodType.setType("O", "+");

		// Person
		person = new Person();
		person.setFirstName("Karl");
		person.setLastName("Rez");

		// DonationRecord
		donationRecord = new DonationRecord();
		donationRecord.setDonation(bloodDonation);
		donationRecord.setOwner(person);
		donationRecord.setTested(false);

		// setting values for bloodDonation
		bloodDonation.setBank(bloodBank);
		bloodDonation.setRecord(donationRecord);
		bloodDonation.setMilliliters(5);
		bloodDonation.setBloodType(bloodType);

		// Setting values for bloodBank
		donations = new HashSet<BloodDonation>();
		donations.add(bloodDonation);
		bloodBank.setName(NAME);
		bloodBank.setDonations(donations);
	}

	@BeforeEach
	void setup() {
		em = getEntityManager();
		et = em.getTransaction();
	}

	@AfterEach
	void tearDown() {
		em.close();
	}

	@Order(1)
	@Test
	void testEmpty() {
		long result = getTotalCount(em, DonationRecord.class);

		assertThat(result, is(comparesEqualTo(0L)));
	}

	@Order(2)
	@Test
	void testCreate() {
		et.begin();
		em.persist(person);
		em.persist(donationRecord);
		em.persist(bloodBank);
		et.commit();

		long result = getCountWithId(em, DonationRecord.class, Integer.class, BloodBank_.id, donationRecord.getId());

		assertThat(result, is(comparesEqualTo(1L)));
	}

	@Order(3)
	@Test
	void testCreateInvalid() {
		et.begin();
		DonationRecord invalidDonationRecord = new DonationRecord();

		// We expect a fail because all the fields are null
		assertThrows(PersistenceException.class, () -> em.persist(invalidDonationRecord));
		et.commit();
	}

	@Test
	void testRead() {
		List<DonationRecord> donationRecordList = (List<DonationRecord>) getAll(em, DonationRecord.class);

		assertThat(donationRecordList.get(0), equalTo(donationRecord));
	}

	@Test
	void ReadDependencies() {
		DonationRecord returnedDonationRecord = getWithId(em, DonationRecord.class, Integer.class, DonationRecord_.id,
				donationRecord.getId());

		assertThat(returnedDonationRecord.getOwner(), equalTo(donationRecord.getOwner()));
		assertThat(returnedDonationRecord.getDonation(), equalTo(donationRecord.getDonation()));
	}

	@Test
	void testUpdate() {
		DonationRecord returnedDonationRecord = getWithId(em, DonationRecord.class, Integer.class, DonationRecord_.id,
				donationRecord.getId());
		
		returnedDonationRecord.setTested(true);
		byte updatedTested = returnedDonationRecord.getTested();
		
		et.begin();
		em.merge(returnedDonationRecord);
		et.commit();
		
		returnedDonationRecord = getWithId(em, DonationRecord.class, Integer.class, DonationRecord_.id,
				donationRecord.getId());
		
		assertThat(returnedDonationRecord.getTested(), equalTo(updatedTested));
	}
	
	@Test
	void testUpdateDependencies() {
		String updatedFirstName = "Joe";
		String updatedLastName = "Mama";

		DonationRecord returnedDonationRecord = getWithId(em, DonationRecord.class, Integer.class, DonationRecord_.id,
				donationRecord.getId());
		returnedDonationRecord.getOwner().setFullName(updatedFirstName, updatedLastName);

		et.begin();
		em.merge(returnedDonationRecord);
		et.commit();

		returnedDonationRecord = getWithId(em, DonationRecord.class, Integer.class, DonationRecord_.id,
				donationRecord.getId());

		//assertThat(returnedDonationRecord.getOwner(), equalTo(donationRecord.getOwner()));
		assertThat(returnedDonationRecord.getOwner().getFirstName(), equalTo(updatedFirstName));
		assertThat(returnedDonationRecord.getOwner().getLastName(), equalTo(updatedLastName));
	}

	@Order(Integer.MAX_VALUE - 1)
	@Test
	void testDeleteDependency() {
		DonationRecord returnedDonationRecord = getWithId(em, DonationRecord.class, Integer.class, DonationRecord_.id,
				donationRecord.getId());
		returnedDonationRecord.setDonation(null);

		et.begin();
		em.merge(returnedDonationRecord);
		et.commit();

		returnedDonationRecord = getWithId(em, DonationRecord.class, Integer.class, DonationRecord_.id,
				donationRecord.getId());

		assertThat(returnedDonationRecord.getDonation(), is(nullValue()));
	}
	
	@Order(Integer.MAX_VALUE)
	@Test
	void testDelete() {
		DonationRecord donationRecordToDelete = getWithId(em, DonationRecord.class, Integer.class, DonationRecord_.id,
				donationRecord.getId());
		
		et.begin();
		em.merge(donationRecordToDelete);
		et.commit();
		
		long count = getCountWithId(em, DonationRecord.class, Integer.class, DonationRecord_.id,
				donationRecordToDelete.getId());
		
		assertThat(count, is(equalTo(0L)));
	}

}
