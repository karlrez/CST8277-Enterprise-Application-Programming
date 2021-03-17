/*****************************************************************
 * File: PersonPojo.java Course materials (21W) CST8277
 *
 * @author Shahriar (Shawn) Emami
 * @author (original) Mike Norman
 */
package databank.model;

import java.time.Instant;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * TODO 21 - What annotations should be used on these method.
 * https://www.logicbig.com/tutorials/java-ee-tutorial/jpa/entity-listeners.html<br>
 */
public class PersonPojoListener {

	private static final Logger LOG = LogManager.getLogger();

	//TODO 22 - called before persist to add the dates
	@PrePersist
	public void setCreatedOnDate( PersonPojo person) {
		LOG.debug( "Person @PrePersist before = {}", person);
		Instant now = Instant.now();
		person.setCreated( now);
		//might as well call setUpdatedDate as well
		person.setUpdated( now);
		LOG.debug( "Person @PrePersist after = {}", person);
	}

	//TODO 23 - called before update to update the date
	@PreUpdate
	public void setUpdatedDate( PersonPojo person) {
		LOG.debug( "Person @PreUpdate before = {}", person);
		person.setUpdated( Instant.now());
		LOG.debug( "Person @PreUpdate after = {}", person);
	}
}