package bloodbank.entity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

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
public class TestCRUDPerson extends JUnitBase {

	private EntityManager em;
	private EntityTransaction et;

	private static Person person;

	@BeforeAll
	static void setupAllInit() {
		person = new Person();
		person.setFullName("Karl", "Rezansoff");
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
		long result = getTotalCount(em, Person.class);

		assertThat(result, is(comparesEqualTo(0L)));
	}

	@Order(2)
	@Test
	void testCreate() {
		et.begin();
		em.persist(person);

		et.commit();

		long result = getCountWithId(em, Person.class, Integer.class, Person_.id, person.getId());

		assertThat(result, is(comparesEqualTo(1L)));
	}

	@Order(3)
	@Test
	void testCreateInvalid() {
		et.begin();
		Person invalidPerson = new Person();

		assertThrows(PersistenceException.class, () -> em.persist(invalidPerson));
		et.commit();
	}

	@Test
	void testRead() {
		List<Person> personList = getAll(em, Person.class);

		assertThat(personList.get(0), equalTo(person));
	}

	// No dependencies for this class
	// @Test
	// void testReadDependencies() {}

	@Test
	void testUpdate() {
		String updatedFirstName = "Bob";
		String updatedLastName = "Bobertson";

		Person returnedPerson = getWithId(em, Person.class, Integer.class, Person_.id, person.getId());

		returnedPerson.setFullName(updatedFirstName, updatedLastName);

		assertThat(returnedPerson.getFirstName(), equalTo(updatedFirstName));
		assertThat(returnedPerson.getLastName(), equalTo(updatedLastName));
	}

	// No dependencies for this class
	// @Test
	// void testUpdateDependencies() {}

	// No dependencies for this class
	// @Test
	// void testDeleteDependencies() {}

	@Order(Integer.MAX_VALUE)
	@Test
	void testDelete() {
		Person personToDelete = new Person();
		personToDelete.setFullName("Tom", "Thomas");

		et.begin();
		em.persist(personToDelete);
		et.commit();

		et.begin();
		em.remove(personToDelete);
		et.commit();

		long deletedCount = getCountWithId(em, Person.class, Integer.class, Person_.id,
				personToDelete.getId());
		long personCount = getCountWithId(em, Person.class, Integer.class, Person_.id, person.getId());

		assertThat(deletedCount, is(equalTo(0L)));
		assertThat(personCount, is(equalTo(1L)));
	}
}
