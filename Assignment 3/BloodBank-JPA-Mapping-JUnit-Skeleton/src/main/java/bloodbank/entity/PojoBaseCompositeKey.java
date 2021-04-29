/***************************************************************************
 * File: PojoBase.java Course materials (21F) CST 8277
 * 
 * @author Shariar (Shawn) Emami
 * @date Mar 9, 2021
 * @author Mike Norman
 * @date 2020 04
 */
package bloodbank.entity;

import java.io.Serializable;
import java.time.Instant;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.persistence.Version;

/**
 * Abstract class that is base of (class) hierarchy for all @Entity classes
 * 
 * @param <ID> - type of composit key used
 */
//TODO PC01 - add annotation to define this class as superclass of all entities. Week 9 slides.
@MappedSuperclass
//TODO PC02 - add annotation to place all JPA annotations on fields.
@Access(AccessType.FIELD)
//TODO PC03 - add annotation to for listener.
@EntityListeners(PojoCompositeListener.class)
public abstract class PojoBaseCompositeKey< ID extends Serializable> implements Serializable {
	private static final long serialVersionUID = 1L;

	// TODO PC04 - add missing annotations.
	@Version
	protected int version;

	// TODO PC05 - add missing annotations.
	@Column(name = "created")
	protected long epochCreated;

	// TODO PC06 - add missing annotations.
	@Column(name = "updated")
	protected long epochUpdated;

	// TODO PC07 - add missing annotations (hint, is this column on DB). // nope
	@Transient
	protected Instant created;

	// TODO PC08 - add missing annotations (hint, is this column on DB).
	@Transient
	protected Instant updated;

	public abstract ID getId();

	public abstract void setId( ID id);

	public int getVersion() {
		return version;
	}

	public void setVersion( int version) {
		this.version = version;
	}

	public Instant getCreated() {
		if ( created == null)
			setCreatedEpochMilli( epochCreated);
		return created;
	}

	public long getCreatedEpochMilli() {
		return created.toEpochMilli();
	}

	public void setCreated( Instant created) {
		setCreatedEpochMilli( created.toEpochMilli());
	}

	public void setCreatedEpochMilli( long created) {
		this.epochCreated = created;
		this.created = Instant.ofEpochMilli( created);
	}

	public void setUpdated( Instant updated) {
		setUpdatedEpochMilli( updated.toEpochMilli());
	}

	public void setUpdatedEpochMilli( long updated) {
		this.epochUpdated = updated;
		this.updated = Instant.ofEpochMilli( updated);
	}

	public Instant getUpdated() {
		if ( updated == null)
			setUpdatedEpochMilli( epochUpdated);
		return updated;
	}

	public long getUpdatedEpochMilli() {
		return updated.toEpochMilli();
	}

	/**
	 * @see <a href= "https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/">
	 *      How to implement equals and hashCode using the JPA entity identifier (Primary Key) </a>
	 * @see <a href=
	 *      "https://vladmihalcea.com/the-best-way-to-implement-equals-hashcode-and-tostring-with-jpa-and-hibernate/">
	 *      The best way to implement equals, hashCode, and toString with JPA and Hibernate </a>
	 */
	@Override
	public int hashCode() {
		return getClass().hashCode();
	}

	@Override
	public boolean equals( Object obj) {
		if ( this == obj) {
			return true;
		}
		if ( obj == null) {
			return false;
		}
		if ( !( obj instanceof PojoBaseCompositeKey)) {
			return false;
		}
		@SuppressWarnings( "rawtypes")
		PojoBaseCompositeKey other = (PojoBaseCompositeKey) obj;
		if ( getId() != null && !getId().equals( other.getId())) {
			return false;
		}
		return true;
	}
}