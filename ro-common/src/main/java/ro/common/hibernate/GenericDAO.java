package ro.common.hibernate;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * Generic Common DAO for hibernate
 *
 * @author r.krishnakumar
 */
@SuppressWarnings({"unchecked"})
public class GenericDAO extends GenericHqlDAO {

  private SessionFactory sessionFactory;

  public GenericDAO(SessionFactory sessionFactory) {
    super(sessionFactory);
    this.sessionFactory = sessionFactory;
  }

  /**
   * Generic Method to save an entity
   *
   * @param <T>
   * @param obj
   * @return
   */
  public <T> T saveEntity(T obj) {
    if (obj == null) return null;
    Session session = sessionFactory.getCurrentSession();
    session.save(obj);
    return obj;
  }

  /**
   * Generic Method to save or update an entity
   *
   * @param <T>
   * @param obj
   * @return
   */
  public <T> T saveOrUpdateEntity(T obj) {
    if (obj == null) return null;
    Session session = sessionFactory.getCurrentSession();
    session.saveOrUpdate(obj);
    return obj;
  }

  /**
   * Generic Method to save a collection of entity
   *
   * @param <T>
   * @param obj
   * @return
   */
  public <T> boolean saveEntites(List<T> collection) {
    if (collection == null || collection.size() == 0) return false;
    Session session = sessionFactory.getCurrentSession();
    for (T t : collection) {
      session.save(t);
    }
    return true;
  }

  /**
   * Generic Method to update an entity
   *
   * @param <T>
   * @param entity
   * @return
   */
  public <T> boolean updateEntity(T entity) {
    if (entity == null) return false;
    if (entity instanceof List) {
      List<T> collection = (List<T>) entity;
      return updateEntites(collection);
    }
    Session session = sessionFactory.getCurrentSession();
    session.update(entity);
    return true;
  }

  /**
   * Generic Method to update a collection of entity
   *
   * @param <T>
   * @param collection
   * @return
   */
  public <T> boolean updateEntites(List<T> collection) {
    if (collection == null || collection.size() == 0) return false;
    Session session = sessionFactory.getCurrentSession();
    for (T t : collection) {
      session.update(t);
    }
    return true;
  }

  /**
   * Generic Method to update and return an entity
   *
   * @param <T>
   * @param entity
   * @return
   */
  public <T> T updateAndReturnEntity(T entity) {
    if (entity == null) return null;
    Session session = sessionFactory.getCurrentSession();
    session.update(entity);
    return entity;
  }

  /**
   * Generic Method to delete an entity
   *
   * @param <T>
   * @param entity
   * @return
   */
  public <T> boolean deleteEntity(T entity) {
    if (entity == null) return false;
    Session session = sessionFactory.getCurrentSession();
    session.delete(entity);
    return true;
  }

  /**
   * Generic Method to delete a collection of entities
   *
   * @param <T>
   * @param collection
   * @return
   */
  public <T> boolean deleteEntites(List<T> collection) {
    if (collection == null || collection.size() == 0) return false;
    Session session = sessionFactory.getCurrentSession();
    for (T t : collection) {
      session.delete(t);
    }
    return true;
  }

  /**
   * Generic Method to return an entity by id
   *
   * @param <T>
   * @param c
   * @param entityId
   * @return
   */
  public <T> T getEntity(Class<T> c, Long entityId) {
    return (T) sessionFactory.getCurrentSession().get(c, entityId);
  }
}
