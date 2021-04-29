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
public class TestCRUDBloodDonation extends JUnitBase {

	private EntityManager em;
	private EntityTransaction et;

	private static BloodBank bloodBank;
	private static String NAME = "Karls Blood Bank";
	private static BloodDonation bloodDonation;
	private static Set<BloodDonation> donations;
	private static BloodType bloodType;
	private static DonationRecord donationRecord;
	private static Person person;

	@BeforeAll
	static void setupAllInit() {
		// Creating a single PrivateBloodBank object

		bloodBank = new PrivateBloodBank();
		bloodDonation = new BloodDonation();

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
		// Get all results from BloodDonation table
		long result = getTotalCount(em, BloodDonation.class);

		// Result should be 0
		assertThat(result, is(comparesEqualTo(0L)));
	}

	@Order(2)
	@Test
	void testCreate() {
		et.begin();
		em.persist(person);
		em.persist(donationRecord);
		em.persist(bloodBank);

		// em.persist(bloodBank);
		et.commit();

		// Getting count of records with id of PrivateBloodBank obj
		long result = getCountWithId(em, BloodDonation.class, Integer.class, BloodDonation_.id,
				bloodDonation.getId());

		// Checking for count of 1
		assertThat(result, is(comparesEqualTo(1L)));
	}

	@Order(3)
	@Test
	void testCreateInvalid() {
		et.begin();
		BloodDonation invalidBloodDonation = new BloodDonation();

		// We expect a fail because all the fields are null
		assertThrows(PersistenceException.class, () -> em.persist(invalidBloodDonation));
		et.commit();
	}

	@SuppressWarnings("unchecked")
	@Test
	void testRead() {
		List<BloodDonation> bloodDonationList = (List<BloodDonation>) getAll(em, BloodDonation.class);

		assertThat(bloodDonationList.get(0), equalTo(bloodDonation));
	}

	@Test
	void testReadDependencies() {
		BloodDonation returnedBloodDonation = getWithId(em, BloodDonation.class, Integer.class, BloodDonation_.id,
				bloodDonation.getId());

		assertThat(returnedBloodDonation.getBank(), equalTo(bloodDonation.getBank()));
		assertThat(returnedBloodDonation.getRecord(), equalTo(bloodDonation.getRecord()));
		assertThat(returnedBloodDonation.getMilliliters(), equalTo(bloodDonation.getMilliliters()));
		assertThat(returnedBloodDonation.getBloodType(), equalTo(bloodDonation.getBloodType()));
	}

	@Test
	void testUpdate() {
		int milliliters = 5;

		// Getting BloodDonation and changing milliliters
		BloodDonation updatedBloodDonation = getWithId(em, BloodDonation.class, Integer.class, BloodDonation_.id,
				bloodDonation.getId());
		updatedBloodDonation.setMilliliters(milliliters);

		// Commit to db
		et.begin();
		em.merge(updatedBloodDonation);
		et.commit();

		// Getting BloodDonation again and checking updated value is correct
		updatedBloodDonation = getWithId(em, BloodDonation.class, Integer.class, BloodBank_.id,
				bloodDonation.getId());

		assertThat(updatedBloodDonation.getMilliliters(), equalTo(milliliters));
	}

	@Test
	void testUpdateDependencies() {
		BloodDonation returnedBloodDonation = getWithId(em, BloodDonation.class, Integer.class, BloodDonation_.id,
				bloodDonation.getId());

		// Making updates
		returnedBloodDonation.getBloodType().setType("B", "+");
		returnedBloodDonation.getBank().setName("Updated Bank");
		returnedBloodDonation.getRecord().setTested(true);

		// Commit to db
		et.begin();
		em.merge(returnedBloodDonation);
		et.commit();

		// Getting BloodDonation again and checking updated value is correct
		BloodDonation updatedBloodDonation = getWithId(em, BloodDonation.class, Integer.class, BloodDonation_.id,
				returnedBloodDonation.getId());

		// Testing BloodType, BloodBank, and DonationRecord
		assertThat(returnedBloodDonation.getBloodType(), equalTo(updatedBloodDonation.getBloodType()));
		assertThat(returnedBloodDonation.getBank(), equalTo(updatedBloodDonation.getBank()));
		assertThat(returnedBloodDonation.getRecord(), equalTo(updatedBloodDonation.getRecord()));
	}

	@Order(Integer.MAX_VALUE - 1)
	@Test
	void testDeleteDependency() {
		// Getting BloodDonation and deleting DonationRecord
		BloodDonation returnedBloodDonation = getWithId(em, BloodDonation.class, Integer.class, BloodDonation_.id,
				bloodDonation.getId());
		returnedBloodDonation.setRecord(null);

		// Commit to db
		et.begin();
		em.merge(returnedBloodDonation);
		et.commit();

		// Getting BloodBank again and checking donations is null
		returnedBloodDonation = getWithId(em, BloodDonation.class, Integer.class, BloodDonation_.id,
				bloodDonation.getId());

		assertThat(returnedBloodDonation.getRecord(), is(nullValue()));
	}

	@Order(Integer.MAX_VALUE)
	@Test
	void testDelete() {
		BloodDonation bloodDonationToDelete = getWithId(em, BloodDonation.class, Integer.class, BloodDonation_.id,
				bloodDonation.getId());
		
		et.begin();
		em.remove(bloodDonationToDelete);
		et.commit();

		// Getting BloodDonation again as count
		long deletedBloodDonationCount = getCountWithId(em, BloodDonation.class, Integer.class, BloodDonation_.id,
				bloodDonationToDelete.getId());

		// Asserting count is 0 for deleted record
		assertThat(deletedBloodDonationCount, is(equalTo(0L)));
	}
}
