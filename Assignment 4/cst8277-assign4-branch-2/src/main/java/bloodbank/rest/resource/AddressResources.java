//@author Group 41: dan brown, daniel dwyer, karl rezansoff

package bloodbank.rest.resource;

import static bloodbank.utility.MyConstants.ADDRESS_RESOURCE_NAME;
import static bloodbank.utility.MyConstants.ADMIN_ROLE;
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
import bloodbank.entity.Address;

@Path(ADDRESS_RESOURCE_NAME)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AddressResources {

	private static final Logger LOG = LogManager.getLogger();

	@EJB
	protected AddressService service;

	@Inject
	protected SecurityContext sc;

	@GET
	@RolesAllowed({ ADMIN_ROLE })
	public Response getAddresses() {
		LOG.debug("retrieving all addresses ...");
		List<Address> addresses = service.getAllAddress();
		Response response = Response.ok(addresses).build();
		return response;
	}

	@GET
	@RolesAllowed({ ADMIN_ROLE, USER_ROLE })
	@Path(RESOURCE_PATH_ID_PATH)
	public Response getAddressById(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id) {
		LOG.debug("try to retrieve specific address " + id);
		Address address = service.getAddressId(id);
		if (address == null)
			return Response.status(Status.NOT_FOUND).build();
		return Response.ok(address).build();
	}

	@POST
	@RolesAllowed({ ADMIN_ROLE })
	public Response addAddress(Address newAddress) {
		Response response = null;
		service.persistAddress(newAddress);
		response = Response.ok(newAddress).build();
		return response;
	}

	@DELETE
	@RolesAllowed({ ADMIN_ROLE })
	@Path(RESOURCE_PATH_ID_PATH)
	public Response deleteAddress(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id) {
		Response response = null;
		service.deleteAddressById(id);
		response = Response.ok().build();
		return response;
	}
}