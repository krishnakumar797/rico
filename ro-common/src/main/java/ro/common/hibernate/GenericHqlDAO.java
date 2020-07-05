package ro.common.hibernate;

import java.time.LocalDate;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Generic HQL DAO for hibernate
 *
 * @author r.krishnakumar
 */
@NoRepositoryBean
@SuppressWarnings({"unchecked", "rawtypes"})
public abstract class GenericHqlDAO extends GenericNativeSqlDAO {

  private SessionFactory sessionFactory;

  public GenericHqlDAO(SessionFactory sessionFactory) {
    super(sessionFactory);
    this.sessionFactory = sessionFactory;
  }

  // Methods to return unique results
  public <T> T getEntity(String hqlQuery) {
    Session session = sessionFactory.getCurrentSession();
    Query query = session.createQuery(hqlQuery);
    return (T) query.uniqueResult();
  }

  public <T> T getEntityByIntParam(String hqlQuery, int param0) {
    hqlQuery = replace(hqlQuery, 0);
    Session session = sessionFactory.getCurrentSession();
    Query query = session.createQuery(hqlQuery);
    query.setParameter(0, param0);
    return (T) query.uniqueResult();
  }

  public <T> T getEntityByStringParam(String hqlQuery, String param0) {
    hqlQuery = replace(hqlQuery, 0);
    Session session = sessionFactory.getCurrentSession();
    Query query = session.createQuery(hqlQuery);
    query.setParameter(0, param0);
    return (T) query.uniqueResult();
  }

  public <T> T getEntityByTwoIntParam(String hqlQuery, int param0, int param1) {
    hqlQuery = replace(hqlQuery, 1);
    Session session = sessionFactory.getCurrentSession();
    Query query = session.createQuery(hqlQuery);
    query.setParameter(0, param0);
    query.setParameter(1, param1);
    return (T) query.uniqueResult();
  }

  public <T> T getEntityByIntAndStringParam(String hqlQuery, int param0, String param1) {
    hqlQuery = replace(hqlQuery, 1);
    Session session = sessionFactory.getCurrentSession();
    Query query = session.createQuery(hqlQuery);
    query.setParameter(0, param0);
    query.setParameter(1, param1);
    return (T) query.uniqueResult();
  }

  // Methods to return list results
  public <T> List<T> getEntitiesNoParam(String hqlQuery) {
    Session session = sessionFactory.getCurrentSession();
    Query query = session.createQuery(hqlQuery);
    return query.list();
  }

  public <T> List<T> getEntitiesByIntParam(String hqlQuery, int param0) {
    hqlQuery = replace(hqlQuery, 0);
    Session session = sessionFactory.getCurrentSession();
    Query query = session.createQuery(hqlQuery);
    query.setParameter(0, param0);
    return query.list();
  }

  public <T> List<T> getEntitiesByStringParam(String hqlQuery, String param0) {
    hqlQuery = replace(hqlQuery, 0);
    Session session = sessionFactory.getCurrentSession();
    Query query = session.createQuery(hqlQuery);
    query.setParameter(0, param0);
    return query.list();
  }

  public <T> List<T> getEntitiesByIntAndStringParam(String hqlQuery, int param0, String param1) {
    hqlQuery = replace(hqlQuery, 1);
    Session session = sessionFactory.getCurrentSession();
    Query query = session.createQuery(hqlQuery);
    query.setParameter(0, param0);
    query.setParameter(1, param1);
    return query.list();
  }

  public <T> List<T> getEntitiesByDateParam(String hqlQuery, LocalDate param0) {
    hqlQuery = replace(hqlQuery, 0);
    Session session = sessionFactory.getCurrentSession();
    Query query = session.createQuery(hqlQuery);
    query.setParameter(0, param0);
    return query.list();
  }

  // Method to return list results with limit
  public <T> List<T> getEntitiesByNoParamWithLimit(String hqlQuery, int lastCount, int limit) {
    Session session = sessionFactory.getCurrentSession();
    Query query = session.createQuery(hqlQuery);
    query.setFirstResult(lastCount);
    query.setMaxResults(limit);
    return query.list();
  }

  public <T> List<T> getEntitiesByStringParamWithLimit(
      String hqlQuery, int lastCount, int limit, String param0) {
    hqlQuery = replace(hqlQuery, 0);
    Session session = sessionFactory.getCurrentSession();
    Query query = session.createQuery(hqlQuery);
    query.setParameter(0, param0);
    query.setFirstResult(lastCount);
    query.setMaxResults(limit);
    return query.list();
  }

  public <T> List<T> getEntitiesByIntAndStringParamWithLimit(
      String hqlQuery, int lastCount, int limit, int param0, String param1) {
    hqlQuery = replace(hqlQuery, 1);
    Session session = sessionFactory.getCurrentSession();
    Query query = session.createQuery(hqlQuery);
    query.setParameter(0, param0);
    query.setParameter(1, param1);
    query.setFirstResult(lastCount);
    query.setMaxResults(limit);
    return query.list();
  }
}
