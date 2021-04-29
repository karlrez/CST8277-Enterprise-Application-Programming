//@author Group 41: dan brown, daniel dwyer, karl rezansoff

package bloodbank.rest.resource;

import static bloodbank.utility.MyConstants.ADMIN_ROLE;
import static bloodbank.utility.MyConstants.DONATION_RESOURCE_NAME;
import static bloodbank.utility.MyConstants.RESOURCE_PATH_ID_ELEMENT;
import static bloodbank.utility.MyConstants.RESOURCE_PATH_ID_PATH;
import static bloodbank.utility.MyConstants.USER_ROLE;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.security.enterprise.SecurityContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import bloodbank.ejb.AddressService;
import bloodbank.ejb.BloodDonationService;
import bloodbank.entity.Address;
import bloodbank.entity.BloodDonation;

@Path(DONATION_RESOURCE_NAME)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class BloodDonationResource {
	private static final Logger LOG = LogManager.getLogger();

	@EJB
	protected BloodDonationService service;

	@Inject
	protected SecurityContext sc;

	@GET
	@RolesAllowed({ ADMIN_ROLE })
	public Response getDonations() {
		LOG.debug("retrieving all donations ...");
		List<BloodDonation> addresses = service.getAll();
		Response response = Response.ok(addresses).build();
		return response;
	}

	@GET
	@RolesAllowed({ ADMIN_ROLE, USER_ROLE })
	@Path(RESOURCE_PATH_ID_PATH)
	public Response getDonationById(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id) {
		LOG.debug("try to retrieve specific donation " + id);
		BloodDonation donation = service.getDonationId(id);
		if (donation == null)
			return Response.status(Status.NOT_FOUND).build();
		return Response.ok(donation).build();
	}

	@POST
	@RolesAllowed({ ADMIN_ROLE })
	public Response addDonation(BloodDonation newDonation) {
		Response response = null;
		service.persistDonation(newDonation);
		response = Response.ok(newDonation).build();
		return response;
	}

	@DELETE
	@RolesAllowed({ ADMIN_ROLE })
	@Path(RESOURCE_PATH_ID_PATH)
	public Response deleteDonation(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id) {
		Response response = null;
		service.deleteDonationById(id);
		response = Response.ok().build();
		return response;
	}
}
