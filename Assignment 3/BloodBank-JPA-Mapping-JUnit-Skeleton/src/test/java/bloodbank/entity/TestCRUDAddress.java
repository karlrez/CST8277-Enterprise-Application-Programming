package bloodbank.entity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import common.JUnitBase;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestCRUDAddress extends JUnitBase {

	private EntityManager em;
	private EntityTransaction et;

	private static Address address;
	private static final String STREET_NUMBER = "104";
	private static final String STREET = "Chesterton Dr";
	private static final String CITY = "Ottawa";
	private static final String PROVINCE = "ON";
	private static final String COUNTRY = "CA";
	private static final String ZIPCODE = "K2E5T6";

	@BeforeAll
	static void setupAllInit() {
		address = new Address();
		address.setAddress(STREET_NUMBER, STREET, CITY, PROVINCE, COUNTRY, ZIPCODE);
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
		// Get all results from Addresses
		long result = getTotalCount(em, Address.class);

		// result should be 0
		assertThat(result, is(comparesEqualTo(0L)));
	}

	@Order(2)
	@Test
	void testCreate() {
		et.begin();
		em.persist(address);
		et.commit();

		// Getting count of records with id of address obj
		long result = getCountWithId(em, Address.class, Integer.class, Address_.id, address.getId());

		// Checking for count of 1
		assertThat(result, is(comparesEqualTo(1L)));
	}

	@Order(3)
	@Test
	void testCreateInvalid() {
		Address invalidAddress = address;
		invalidAddress.setCity(null);

		et.begin();

		// We expect a fail because city is a required field
		assertThrows(PersistenceException.class, () -> em.persist(invalidAddress));
		et.commit();
	}

	@SuppressWarnings("unchecked")
	@Test
	void testRead() {
		List<Address> addresses = (List<Address>) getAll(em, Address.class);

		// For some reason the memory address for this object changes
		// Just checking id matches
		assertThat(addresses.get(0).id, equalTo(address.getId()));
	}

	@Test
	void testReadDependencies() {
		// Getting our address record
		Address returnedAddress = getWithId(em, Address.class, Integer.class, Address_.id, address.getId());

		// Check all fields are correct
		assertThat(returnedAddress.getStreetNumber(), equalTo(STREET_NUMBER));
		assertThat(returnedAddress.getStreet(), equalTo(STREET));
		assertThat(returnedAddress.getCity(), equalTo(CITY));
		assertThat(returnedAddress.getProvince(), equalTo(PROVINCE));
		assertThat(returnedAddress.getCountry(), equalTo(COUNTRY));
		assertThat(returnedAddress.getZipcode(), equalTo(ZIPCODE));
	}

	@Test
	void testUpdate() {
		// Getting our address record
		Address returnedAddress = getWithId(em, Address.class, Integer.class, Address_.id, address.getId());

		// Updating our address record
		String newCity = "Vancouver";
		String newZipcode = "ABC123";
		et.begin();
		returnedAddress.setCity(newCity);
		returnedAddress.setZipcode(newZipcode);
		em.merge(returnedAddress);
		et.commit();

		// Getting our updated address from db
		Address updatedAddress = getWithId(em, Address.class, Integer.class, Address_.id, address.getId());

		// Checking updated fields are correct
		assertThat(updatedAddress.getCity(), equalTo(newCity));
		assertThat(updatedAddress.getZipcode(), equalTo(newZipcode));
	}

	// No dependencies for this class
	// @Test
	// void testUpdateDependencies() {}

	// @Order(Integer.MAX_VALUE - 1)
	// @Test
	// void testDeleteDependency() {}

	@Order(Integer.MAX_VALUE)
	@Test
	void testDelete() {
		// Adding another row to db to make sure only this row is deleted
		Address deleteAddress = new Address();
		deleteAddress.setAddress("222", "test street", "torono", "on", "ca", "abc222");
		et.begin();
		em.persist(deleteAddress);
		et.commit();
		
		// Removing our new address
		et.begin();
		em.remove(deleteAddress);
		et.commit();
		
		// Getting our two addresses from db as a count
		long deletedAddressCount = getCountWithId(em, Address.class, Integer.class, Address_.id, deleteAddress.getId());
		long addressCount = getCountWithId(em, Address.class, Integer.class, Address_.id, address.getId());
		
		// Asserting count is 0 for deleted record and our old address is still there
		assertThat( deletedAddressCount, is( equalTo( 0L)));
		assertThat( addressCount, is( equalTo( 1L)));
	}

}
