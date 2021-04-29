//@author Group 41: dan brown, daniel dwyer, karl rezansoff

package bloodbank.ejb;

import static bloodbank.utility.MyConstants.PU_NAME;

import java.io.Serializable;
import java.util.List;

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

import bloodbank.entity.Phone;

@Singleton
public class PhoneService implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LogManager.getLogger();

	@PersistenceContext(name = PU_NAME)
	protected EntityManager em;
	@Inject
	protected Pbkdf2PasswordHash pbAndjPasswordHash;

	public List<Phone> getAllPhone() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Phone> cq = cb.createQuery(Phone.class);
		Root<Phone> root = cq.from(Phone.class);
		cq.select(root);
		TypedQuery<Phone> q = em.createQuery(cq);
		List<Phone> ret = q.getResultList();
		return ret;
	}

	public Phone getPhoneId(int id) {
		Phone phone = em.find(Phone.class, id);
		return phone;
	}

	@Transactional
	public Phone persistPhone(Phone phone) {
		em.persist(phone);
		return phone;
	}

	@Transactional
	public void deletePhoneById(int id) {
		Phone phone = getPhoneId(id);
		if (phone != null) {
			em.remove(phone);
		}
	}
}
