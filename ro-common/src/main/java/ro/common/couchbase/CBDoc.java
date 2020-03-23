package ro.common.couchbase;

import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.id.GeneratedValue;
import org.springframework.data.couchbase.core.mapping.id.GenerationStrategy;

import ro.common.utils.Doc;

/**
 * Abstract class for couchbase documents
 * 
 * @author r.krishnakumar
 *
 */
public abstract class CBDoc implements Doc {

	@Id
	@GeneratedValue(strategy = GenerationStrategy.UNIQUE)
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
