//@author Group 41: dan brown, daniel dwyer, karl rezansoff

package bloodbank.ejb;

import static bloodbank.utility.MyConstants.PU_NAME;

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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import bloodbank.entity.BloodDonation;
import bloodbank.entity.Phone;

@Singleton
public class BloodDonationService {
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LogManager.getLogger();

	@PersistenceContext(name = PU_NAME)
	protected EntityManager em;
	@Inject
	protected Pbkdf2PasswordHash pbAndjPasswordHash;

	public List<BloodDonation> getAll() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<BloodDonation> cq = cb.createQuery(BloodDonation.class);
		Root<BloodDonation> root = cq.from(BloodDonation.class);
		cq.select(root);
		TypedQuery<BloodDonation> q = em.createQuery(cq);
		List<BloodDonation> ret = q.getResultList();
		return ret;
	}

	public BloodDonation getDonationId(int id) {
		BloodDonation donation = em.find(BloodDonation.class, id);
		return donation;
	}

	public BloodDonation persistDonation(BloodDonation newDonation) {
		em.persist(newDonation);
		return newDonation;

	}

	public void deleteDonationById(int id) {
		BloodDonation donation = getDonationId(id);
		if (donation != null) {
			em.remove(donation);
		}
	}

}
