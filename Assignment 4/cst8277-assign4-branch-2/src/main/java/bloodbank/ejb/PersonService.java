/**
 * File: RecordService.java
 * Course materials (21W) CST 8277
 *
 * @author Shariar (Shawn) Emami
 * @author (original) Mike Norman
 * 
 * update by : @author Group 41: dan brown, daniel dwyer, karl rezansoff
 *
 */
package bloodbank.ejb;

import static bloodbank.entity.BloodBank.ALL_BLOODBANKS_QUERY_NAME;
import static bloodbank.entity.Person.ALL_PERSONS_QUERY_NAME;
import static bloodbank.entity.SecurityRole.ROLE_BY_NAME_QUERY;
import static bloodbank.entity.SecurityUser.USER_FOR_OWNING_PERSON_QUERY;
import static bloodbank.utility.MyConstants.DEFAULT_KEY_SIZE;
import static bloodbank.utility.MyConstants.DEFAULT_PROPERTY_ALGORITHM;
import static bloodbank.utility.MyConstants.DEFAULT_PROPERTY_ITERATIONS;
import static bloodbank.utility.MyConstants.DEFAULT_SALT_SIZE;
import static bloodbank.utility.MyConstants.DEFAULT_USER_PASSWORD;
import static bloodbank.utility.MyConstants.DEFAULT_USER_PREFIX;
import static bloodbank.utility.MyConstants.PARAM1;
import static bloodbank.utility.MyConstants.PROPERTY_ALGORITHM;
import static bloodbank.utility.MyConstants.PROPERTY_ITERATIONS;
import static bloodbank.utility.MyConstants.PROPERTY_KEYSIZE;
import static bloodbank.utility.MyConstants.PROPERTY_SALTSIZE;
import static bloodbank.utility.MyConstants.PU_NAME;
import static bloodbank.utility.MyConstants.USER_ROLE;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.security.enterprise.identitystore.Pbkdf2PasswordHash;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Hibernate;

import bloodbank.entity.Address;
import bloodbank.entity.BloodBank;
import bloodbank.entity.BloodDonation;
import bloodbank.entity.Contact;
import bloodbank.entity.Person;
import bloodbank.entity.Phone;
import bloodbank.entity.SecurityRole;
import bloodbank.entity.SecurityUser;

/**
 * Stateless Singleton ejb Bean - PersonService
 */
@Singleton
public class PersonService implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final Logger LOG = LogManager.getLogger();

	@PersistenceContext(name = PU_NAME)
	protected EntityManager em;
	@Inject
	protected Pbkdf2PasswordHash pbAndjPasswordHash;

	public List<Person> getAllPeople() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Person> cq = cb.createQuery(Person.class);
		Root<Person> root = cq.from(Person.class);
		cq.select(root);
		TypedQuery<Person> q = em.createQuery(cq);
		return q.getResultList();
	}

	public Person getPersonId(int id) {
		return em.find(Person.class, id);
	}

	@Transactional
	public Person persistPerson(Person newPerson) {
		em.persist(newPerson);
		return newPerson;
	}

	@Transactional
	public void buildUserForNewPerson(Person newPerson) {
		SecurityUser userForNewPerson = new SecurityUser();
		userForNewPerson
				.setUsername(DEFAULT_USER_PREFIX + "_" + newPerson.getFirstName() + "." + newPerson.getLastName());
		Map<String, String> pbAndjProperties = new HashMap<>();
		pbAndjProperties.put(PROPERTY_ALGORITHM, DEFAULT_PROPERTY_ALGORITHM);
		pbAndjProperties.put(PROPERTY_ITERATIONS, DEFAULT_PROPERTY_ITERATIONS);
		pbAndjProperties.put(PROPERTY_SALTSIZE, DEFAULT_SALT_SIZE);
		pbAndjProperties.put(PROPERTY_KEYSIZE, DEFAULT_KEY_SIZE);
		pbAndjPasswordHash.initialize(pbAndjProperties);
		String pwHash = pbAndjPasswordHash.generate(DEFAULT_USER_PASSWORD.toCharArray());
		userForNewPerson.setPwHash(pwHash);
		userForNewPerson.setPerson(newPerson);
		SecurityRole userRole = em.createNamedQuery(ROLE_BY_NAME_QUERY, SecurityRole.class)
				.setParameter(PARAM1, USER_ROLE).getSingleResult();
		if (userForNewPerson.getRoles() == null)
			userForNewPerson.setRoles(new HashSet<SecurityRole>());
		userForNewPerson.getRoles().add(userRole);
		userRole.getUsers().add(userForNewPerson);
		em.persist(userForNewPerson);
	}

	@Transactional
	public Person setAddressFor(int id, Address newAddress) {
		Person pers = getPersonId(id);
		if (pers == null)
			return null;

		var contacts = pers.getContacts();
		var contact = contacts.stream().findFirst().get();
		contact.setAddress(newAddress);
		em.persist(contact);

		return pers;
	}

	@Transactional
	public Person setPhoneFor(int id, Phone phone) {
		Person pers = getPersonId(id);
		if (pers == null)
			return null;

		var contacts = pers.getContacts();
		var contact = new Contact();
		contact.setContactType("Home");
		contact.setPhone(phone);
		contact.setOwner(pers);
		contacts.add(contact);
		em.persist(contact);

		return pers;
	}

	/**
	 * to update a person
	 * 
	 * @param id                - id of entity to update
	 * @param personWithUpdates - entity with updated information
	 * @return Entity with updated information
	 */
	@Transactional
	public Person updatePersonById(int id, Person personWithUpdates) {
		Person personToBeUpdated = getPersonId(id);
		if (personToBeUpdated != null) {
			em.refresh(personToBeUpdated);
			em.merge(personWithUpdates);
			em.flush();
		}
		return personToBeUpdated;
	}

	/**
	 * to delete a person by id
	 * 
	 * @param id - person id to delete
	 */
	@Transactional
	public void deletePersonById(int id) {
		Person person = getPersonId(id);
		if (person != null) {
			em.refresh(person);
			TypedQuery<SecurityUser> findUser = em.createNamedQuery(USER_FOR_OWNING_PERSON_QUERY, SecurityUser.class)
					.setParameter(PARAM1, person.getId());
			SecurityUser sUser = findUser.getSingleResult();
			em.remove(sUser);
			em.remove(person);
		}
	}

}