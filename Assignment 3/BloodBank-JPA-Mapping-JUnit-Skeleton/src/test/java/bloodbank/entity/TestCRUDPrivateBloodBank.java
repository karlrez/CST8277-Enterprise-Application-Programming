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
public class TestCRUDPrivateBloodBank extends JUnitBase {

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
		// Get all results from PrivateBloodBank table
		long result = getTotalCount(em, BloodBank.class);

		// result should be 0
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
		long result = getCountWithId(em, BloodBank.class, Integer.class, BloodBank_.id, bloodBank.getId());

		// Checking for count of 1
		assertThat(result, is(comparesEqualTo(1L)));
	}

	@Order(3)
	@Test
	void testCreateInvalid() {
		et.begin();
		BloodBank invalidBloodBank = new PrivateBloodBank();

		// We expect a fail because all the fields are null
		assertThrows(PersistenceException.class, () -> em.persist(invalidBloodBank));
		et.commit();
	}

	@SuppressWarnings("unchecked")
	@Test
	void testRead() {
		List<BloodBank> bloodBankList = (List<BloodBank>) getAll(em, BloodBank.class);

		assertThat(bloodBankList.get(0), equalTo(bloodBank));
	}

	@Test
	void testReadDependencies() {
		BloodBank returnedBloodBank = getWithId(em, BloodBank.class, Integer.class, BloodBank_.id,
				bloodBank.getId());

		assertThat(returnedBloodBank.getDonations(), equalTo(bloodBank.getDonations()));
	}

	@Test
	void testUpdate() {
		String updatedName = "updated blood bank";

		// Getting BloodBank and changing name
		BloodBank returnedBloodBank = getWithId(em, BloodBank.class, Integer.class, BloodBank_.id,
				bloodBank.getId());
		returnedBloodBank.setName(updatedName);

		// Commit to db
		et.begin();
		em.merge(returnedBloodBank);
		et.commit();

		// Getting BloodBank again and checking updated value is correct
		returnedBloodBank = getWithId(em, BloodBank.class, Integer.class, BloodBank_.id, bloodBank.getId());
		
		assertThat(returnedBloodBank.getName(), equalTo(updatedName));
	}

	@Test
	void testUpdateDependencies() {
		// updating milliliters in donation
		int milliliters = 5;

		// Getting BloodBank and changing donations
		BloodBank returnedBloodBank = getWithId(em, BloodBank.class, Integer.class, BloodBank_.id,
				bloodBank.getId());
		returnedBloodBank.getDonations().stream().findFirst().get().setMilliliters(milliliters);

		Set<BloodDonation> updatedBloodDonations = returnedBloodBank.getDonations();

		// Commit to db
		et.begin();
		em.merge(returnedBloodBank);
		et.commit();

		// Getting BloodBank again and checking updated value is correct
		returnedBloodBank = getWithId(em, BloodBank.class, Integer.class, BloodBank_.id, bloodBank.getId());

		assertThat(returnedBloodBank.getDonations(), equalTo(updatedBloodDonations));
	}

	@Order(Integer.MAX_VALUE - 1)
	@Test
	void testDeleteDependency() {
		// Getting BloodBank and deleting donations
		BloodBank returnedBloodBank = getWithId(em, BloodBank.class, Integer.class, BloodBank_.id,
				bloodBank.getId());
		returnedBloodBank.setDonations(null);

		// Commit to db
		et.begin();
		em.merge(returnedBloodBank);
		et.commit();

		// Getting BloodBank again and checking donations is null
		returnedBloodBank = getWithId(em, BloodBank.class, Integer.class, BloodBank_.id, bloodBank.getId());

		assertThat(returnedBloodBank.getDonations(), is(nullValue()));
	}

	@Order(Integer.MAX_VALUE)
	@Test
	void testDelete() {
		// Adding another row to db to make sure only this row is deleted
		BloodBank deletedBloodBank = new PrivateBloodBank();
		deletedBloodBank.setName(NAME);
		
		et.begin();
		em.persist(deletedBloodBank);
		et.commit();

		// Removing our new BloodBank
		et.begin();
		em.remove(deletedBloodBank);
		et.commit();

		// Getting our two BloodBanks from db as a count
		long deletedBloodBankCount = getCountWithId(em, BloodBank.class, Integer.class, BloodBank_.id,
				deletedBloodBank.getId());
		long bloodBankCount = getCountWithId(em, BloodBank.class, Integer.class, BloodBank_.id, bloodBank.getId());

		// Asserting count is 0 for deleted record and our old BloodBank is still there
		assertThat(deletedBloodBankCount, is(equalTo(0L)));
		assertThat(bloodBankCount, is(equalTo(1L)));
	}
}
