/**
 * @author Karl Rezansoff
 */
package databank.ejb;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import databank.model.PersonPojo;

/**
 * 
 * Stateless Session Bean - PersonService
 *
 */
@Singleton
public class PersonService {
	private static final long serialVersionUID = 1L;

	@PersistenceContext(name = "PU_DataBank")
	protected EntityManager em;

	// get the log4j2 logger for this class
	private static final Logger LOG = LogManager.getLogger();

	// Default constructor
	public PersonService() {
	}

	public List<PersonPojo> readAllPeople() {
		LOG.debug("reading all people");
		// use the named JPQL query from the PersonPojo class to grab all the people
		TypedQuery<PersonPojo> allPeopleQuery = em.createNamedQuery(PersonPojo.PERSON_FIND_ALL, PersonPojo.class);
		// execute the query and return the result/s.
		return allPeopleQuery.getResultList();
	}

	@Transactional
	public PersonPojo createPerson(PersonPojo person) {
		LOG.debug("creating a person = {}", person);
		try {
			em.persist(person);
		} catch (Exception e) {
			LOG.catching(e);
		}
		return person;

	}

	public PersonPojo readPersonById(int personId) {
		LOG.debug("read a specific person = {}", personId);
		
		// for some reason this returns " Named parameter not bound : id"
		//TypedQuery<PersonPojo> queryByID = em.createNamedQuery(PersonPojo.PERSON_FIND_ID, PersonPojo.class);
		//return queryByID.getSingleResult();
		
		// workaround solution
		return em.find( PersonPojo.class, personId);
	}

	@Transactional
	public PersonPojo updatePerson(PersonPojo personWithUpdates) {
		LOG.debug("updating a specific person = {}", personWithUpdates);
		//PersonPojo mergedPersonPojo = readPersonById(personWithUpdates.getId());
		PersonPojo mergedPersonPojo = personWithUpdates;
		if (mergedPersonPojo != null) {
			try {
				em.merge(mergedPersonPojo);
			} catch (Exception e) {
				LOG.catching(e);
			}
		}
		return mergedPersonPojo;
	}

	@Transactional
	public void deletePersonById(int personId) {
		LOG.debug("deleting a specific personID = {}", personId);
		PersonPojo person = readPersonById(personId);
		LOG.debug("deleting a specific person = {}", person);
		if (person != null) {
			try {
				em.refresh(person);
				em.remove(person);
			} catch (Exception e) {
				LOG.catching(e);
			}

		}
	}

}
