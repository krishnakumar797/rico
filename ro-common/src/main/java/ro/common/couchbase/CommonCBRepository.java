package ro.common.couchbase;

import java.util.Optional;

import org.springframework.context.annotation.DependsOn;
import org.springframework.data.repository.Repository;

import ro.common.utils.Doc;

/**
 * Genric Commmon DAO spring data repository
 *
 * @author r.krishnakumar
 * 
 * @param <T>
 * @param <ID>
 */
@DependsOn("CouchBaseConfig")
public interface CommonCBRepository<T extends Doc, ID> extends Repository<T, ID> {

	<S extends T> S save(S entity);

	Optional<T> findById(ID primaryKey);

	Iterable<T> findAll();

	long count();

	void delete(T entity);

	boolean existsById(ID primaryKey);
}
