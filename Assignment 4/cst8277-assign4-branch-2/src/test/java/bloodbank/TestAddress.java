//@author Group 41: dan brown, daniel dwyer, karl rezansoff

package bloodbank;

import static bloodbank.utility.MyConstants.APPLICATION_API_VERSION;
import static bloodbank.utility.MyConstants.DEFAULT_ADMIN_USER;
import static bloodbank.utility.MyConstants.DEFAULT_ADMIN_USER_PASSWORD;
import static bloodbank.utility.MyConstants.DEFAULT_USER_PASSWORD;
import static bloodbank.utility.MyConstants.DEFAULT_USER_PREFIX;
import static bloodbank.utility.MyConstants.PERSON_RESOURCE_NAME;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.collection.IsEmptyCollection.empty;

import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.logging.LoggingFeature;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import bloodbank.entity.Address;
import bloodbank.entity.Person;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class TestAddress {
	private static final Class<?> _thisClaz = MethodHandles.lookup().lookupClass();
	private static final Logger logger = LogManager.getLogger(_thisClaz);

	static final String APPLICATION_CONTEXT_ROOT = "REST-BloodBank-Skeleton";
	static final String HTTP_SCHEMA = "http";
	static final String HOST = "localhost";
	static final int PORT = 8080;
	private static final String ADDRESS_RESOURCE_NAME = "address";
	static URI uri;
	static HttpAuthenticationFeature adminAuth;
	static HttpAuthenticationFeature userAuth;

	@BeforeAll
	public static void oneTimeSetUp() throws Exception {
		logger.debug("oneTimeSetUp");
		uri = UriBuilder.fromUri(APPLICATION_CONTEXT_ROOT + APPLICATION_API_VERSION).scheme(HTTP_SCHEMA).host(HOST)
				.port(PORT).build();
		adminAuth = HttpAuthenticationFeature.basic(DEFAULT_ADMIN_USER, DEFAULT_ADMIN_USER_PASSWORD);
		userAuth = HttpAuthenticationFeature.basic(DEFAULT_USER_PREFIX, DEFAULT_USER_PASSWORD);
	}

	protected WebTarget webTarget;

	@BeforeEach
	public void setUp() {
		Client client = ClientBuilder
				.newClient(new ClientConfig().register(MyObjectMapperProvider.class).register(new LoggingFeature()));
		webTarget = client.target(uri);
	}

	@Test
	public void allAddress() throws JsonMappingException, JsonProcessingException {
		Response response = webTarget.register(adminAuth).path(ADDRESS_RESOURCE_NAME).request().get();
		assertThat(response.getStatus(), is(200));
		List<Address> emps = response.readEntity(new GenericType<List<Address>>() {
		});
		assertThat(emps, is(not(empty())));
		assertThat(response.getMediaType(), is(not(MediaType.APPLICATION_XML)));
	}

	@Test
	public void addressByID() throws JsonMappingException, JsonProcessingException {
		int id = 1;
		Response response = webTarget.register(adminAuth).path(ADDRESS_RESOURCE_NAME).path(Integer.toString(id))
				.request().get();
		assertThat(response.getStatus(), is(200));
		Address emps = response.readEntity(Address.class);
		assertThat(emps, notNullValue());
		assertThat(emps.getId(), is(id));
	}

	@Test
	public void deleteAndRemoveAddress() throws JsonMappingException, JsonProcessingException {
		Address add = new Address();
		String streetNum = "111";
		String city = "ottawa";
		String country = "canada";
		String province = "ont";
		String street = "bloppity";
		String zip = "111";
		add.setAddress(streetNum, street, city, province, country, zip);
		Response response = webTarget.register(adminAuth).path(ADDRESS_RESOURCE_NAME).request().post(Entity.json(add));

		assertThat(response.getStatus(), is(200));
		Address returnedAddress = response.readEntity(Address.class);
		assertThat(returnedAddress, notNullValue());
		assertThat(returnedAddress.getStreetNumber(), is(streetNum));
		assertThat(returnedAddress.getCity(), is(city));
		assertThat(returnedAddress.getCountry(), is(country));
		assertThat(returnedAddress.getProvince(), is(province));
		assertThat(returnedAddress.getStreet(), is(street));
		assertThat(returnedAddress.getZipcode(), is(zip));

		response = webTarget.register(adminAuth).path(ADDRESS_RESOURCE_NAME)
				.path(Integer.toString(returnedAddress.getId())).request().delete();

		assertThat(response.getStatus(), is(200));
	}
}
