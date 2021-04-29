
/**
@author Group 41: dan brown, daniel dwyer, karl rezansoff
*/
package bloodbank.ejb;

import static bloodbank.utility.MyConstants.PU_NAME;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.security.enterprise.identitystore.Pbkdf2PasswordHash;
import javax.transaction.Transactional;
import bloodbank.entity.Address;

@Singleton
public class AddressService implements Serializable {
	private static final long serialVersionUID = 1L;

	@PersistenceContext(name = PU_NAME)
	protected EntityManager em;
	@Inject
	protected Pbkdf2PasswordHash pbAndjPasswordHash;

	public List<Address> getAllAddress() {
		List<Address> addresses = em.createNamedQuery(Address.ALL_ADDRESS_QUERY_NAME, Address.class).getResultList();
		addresses.forEach(address -> address.setContacts(null));
		return addresses;
	}

	public Address getAddressId(int id) {
		Address add = em.find(Address.class, id);
		return add;
	}

	@Transactional
	public Address persistAddress(Address newAddress) {
		em.persist(newAddress);
		return newAddress;
	}

	@Transactional
	public void deleteAddressById(int id) {
		Address person = getAddressId(id);
		if (person != null) {
			em.remove(person);
		}
	}
}
