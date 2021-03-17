/******************************************************
 * File: PersonDaoImpl.java Course materials (21W) CST8277
 *
 * @author Shariar (Shawn) Emami
 * @author (original) Mike Norman
 */
package databank.dao;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.ExternalContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletContext;
import javax.sql.DataSource;

import databank.model.PersonPojo;

/**
 * Description: Implements the C-R-U-D API for the database
 */
//TODO don't forget this object is a managed bean with a application scope
@Named
@ApplicationScoped
public class PersonDaoImpl implements PersonDao, Serializable {
	/** explicitly set serialVersionUID */
	private static final long serialVersionUID = 1L;

	private static final String DATABANK_DS_JNDI = "java:app/jdbc/databank";
	private static final String READ_ALL = "select * from person";
	private static final String READ_PERSON_BY_ID = "select * from person where id = ?";
	private static final String INSERT_PERSON = "insert into person (first_name,last_name,email,phone,created) values(?,?,?,?,?)";
	private static final String UPDATE_PERSON_ALL_FIELDS = "update person set first_name = ?, last_name = ?, email = ?, phone = ? where id = ?";
	private static final String DELETE_PERSON_BY_ID = "delete from person where id = ?";

	@Inject
	protected ExternalContext externalContext;

	private void logMsg( String msg) {
		( (ServletContext) externalContext.getContext()).log( msg);
	}

	@Resource( lookup = DATABANK_DS_JNDI)
	protected DataSource databankDS;

	protected Connection conn;
	protected PreparedStatement readAllPstmt;
	protected PreparedStatement readByIdPstmt;
	protected PreparedStatement createPstmt;
	protected PreparedStatement updatePstmt;
	protected PreparedStatement deleteByIdPstmt;

	@PostConstruct
	protected void buildConnectionAndStatements() {
		try {
			logMsg( "building connection and stmts");
			conn = databankDS.getConnection();
			readAllPstmt = conn.prepareStatement( READ_ALL);
			createPstmt = conn.prepareStatement( INSERT_PERSON, RETURN_GENERATED_KEYS);
			//TODO initialize other PreparedStatements
			readByIdPstmt=conn.prepareStatement(READ_PERSON_BY_ID);
			updatePstmt=conn.prepareStatement(UPDATE_PERSON_ALL_FIELDS);
			deleteByIdPstmt=conn.prepareStatement(DELETE_PERSON_BY_ID);
		} catch ( Exception e) {
			logMsg( "something went wrong getting connection from database: " + e.getLocalizedMessage());
		}
	}

	@PreDestroy
	protected void closeConnectionAndStatements() {
		try {
			logMsg( "closing stmts and connection");
			readAllPstmt.close();
			createPstmt.close();
			conn.close();
			//TODO close other PreparedStatements
			readByIdPstmt.close();
			updatePstmt.close();
			deleteByIdPstmt.close();
		} catch ( Exception e) {
			logMsg( "something went wrong closing stmts or connection: " + e.getLocalizedMessage());
		}
	}

	@Override
	public List< PersonPojo> readAllPeople() {
		logMsg( "reading all People");
		List< PersonPojo> people = new ArrayList<>();
		try ( ResultSet rs = readAllPstmt.executeQuery();) {

			while ( rs.next()) {
				PersonPojo newPerson = new PersonPojo();
				newPerson.setId( rs.getInt( "id"));
				newPerson.setFirstName( rs.getString( "first_name"));
				//TODO complete the person initialization
				newPerson.setLastName( rs.getString( "last_name"));
				newPerson.setEmail(rs.getString("email"));
				newPerson.setPhoneNumber(rs.getString("phone"));
				newPerson.setCreated(rs.getLong("Created"));
				people.add( newPerson);
			}
		} catch ( SQLException e) {
			logMsg( "something went wrong accessing database: " + e.getLocalizedMessage());
		}
		return people;
	}

	@Override
	public PersonPojo createPerson( PersonPojo person) {
		logMsg( "creating an person");
		//TODO complete
		try {
			this.createPstmt.setString(1, person.getFirstName());
			this.createPstmt.setString(2, person.getLastName());
			this.createPstmt.setString(3, person.getEmail());
			this.createPstmt.setString(4, person.getPhoneNumber());
			this.createPstmt.setLong(5, person.getCreatedEpochMilli());
			int personId = createPstmt.executeUpdate();
			person.setId(personId);
		} catch (SQLException e) {
			logMsg("sql exception in createPerson() : "+e.getMessage());
			e.printStackTrace();
			return null;
		}
		logMsg("person created!");
		return person;
	}

	@Override
	public PersonPojo readPersonById( int personId) {
		logMsg( "read a specific person");
		//TODO complete
		PersonPojo personPojo = new PersonPojo();
		
		try {
			this.readByIdPstmt.setInt(1, personId);
			ResultSet resultSet = this.readByIdPstmt.executeQuery();
			while(resultSet.next()) {
				personPojo.setId(resultSet.getInt( "id"));
				personPojo.setFirstName(resultSet.getString( "first_name"));
				personPojo.setLastName(resultSet.getString( "last_name"));
				personPojo.setEmail(resultSet.getString( "email"));
				personPojo.setPhoneNumber(resultSet.getString("phone"));
				personPojo.setCreated(resultSet.getLong("created"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logMsg("sql exception in readPersonById() : "+e.getMessage());
			e.printStackTrace();
			return null;
		}
		
		return personPojo;
	}

	@Override
	public void updatePerson( PersonPojo person) {
		logMsg( "updating a specific person");
		//TODO complete
		try {
			this.updatePstmt.setString(1, person.getFirstName());
			this.updatePstmt.setString(2, person.getLastName());
			this.updatePstmt.setString(3, person.getEmail());
			this.updatePstmt.setString(4, person.getPhoneNumber());
			this.updatePstmt.setInt(5, person.getId());
			this.updatePstmt.execute();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logMsg("sql exception in updatePerson() : "+e.getMessage());
			e.printStackTrace();
		}
		logMsg("Person updated successfully!");
	}

	@Override
	public void deletePersonById( int personId) {
		logMsg( "deleting a specific person");
		//TODO complete
		try {
			this.deleteByIdPstmt.setInt(1, personId);
			this.deleteByIdPstmt.execute();
		} catch (SQLException e) {
			logMsg("sql exception in deletePersonById() : "+e.getMessage());
			e.printStackTrace();
		}
		logMsg("Delete successful!");
	}

}