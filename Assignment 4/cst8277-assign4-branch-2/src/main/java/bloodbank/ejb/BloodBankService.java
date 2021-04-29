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
import bloodbank.entity.BloodBank;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Singleton
public class BloodBankService implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LogManager.getLogger();

	@PersistenceContext(name = PU_NAME)
	protected EntityManager em;
	@Inject
	protected Pbkdf2PasswordHash pbAndjPasswordHash;

	public List<BloodBank> getAll() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<BloodBank> cq = cb.createQuery(BloodBank.class);
		Root<BloodBank> root = cq.from(BloodBank.class);
		cq.select(root);
		TypedQuery<BloodBank> qu = em.createQuery(cq);
		List<BloodBank> ret = qu.getResultList();
		return ret;
	}

	public BloodBank getBankId(int id) {
		BloodBank bank = em.find(BloodBank.class, id);
		if (bank == null)
			return null;
		return bank;
	}

	public BloodBank persistBank(BloodBank newBank) {
		em.persist(newBank);
		return newBank;

	}

	public void deleteBankById(int id) {
		BloodBank bank = getBankId(id);
		if (bank != null) {
			em.remove(bank);
		}
	}

}
