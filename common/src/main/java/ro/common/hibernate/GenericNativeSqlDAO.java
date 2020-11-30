package ro.common.hibernate;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.util.StringUtils;

import ro.common.utils.Utils;

/**
 * Generic Native SQL DAO for hibernate
 *
 * @author r.krishnakumar
 */
@NoRepositoryBean
@SuppressWarnings({"unchecked", "deprecation", "rawtypes"})
public abstract class GenericNativeSqlDAO {

  private SessionFactory sessionFactory;

  public GenericNativeSqlDAO(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  // Methods to get the Update records using native sql query
  public int updateRecords(String sqlQry, Integer param0, Long param1) {
    sqlQry = replace(sqlQry, 1);
    Session session = sessionFactory.getCurrentSession();
    NativeQuery query = session.createNativeQuery(sqlQry);
    query.setParameter(0, param0);
    query.setParameter(1, param1);
    return query.executeUpdate();
  }

  public int updateRecords(String sqlQry, String param0, Long param1) {
    sqlQry = replace(sqlQry, 1);
    Session session = sessionFactory.getCurrentSession();
    NativeQuery query = session.createNativeQuery(sqlQry);
    query.setParameter(0, param0);
    query.setParameter(1, param1);
    return query.executeUpdate();
  }

  public int updateRecords(String sqlQry, String param0, String param1) {
    sqlQry = replace(sqlQry, 1);
    Session session = sessionFactory.getCurrentSession();
    NativeQuery query = session.createNativeQuery(sqlQry);
    query.setParameter(0, param0);
    query.setParameter(1, param1);
    return query.executeUpdate();
  }

  public int updateRecords(String sqlQry, String param0, String param1, String param2) {
    sqlQry = replace(sqlQry, 2);
    Session session = sessionFactory.getCurrentSession();
    NativeQuery query = session.createNativeQuery(sqlQry);
    query.setParameter(0, param0);
    query.setParameter(1, param1);
    query.setParameter(2, param2);
    return query.executeUpdate();
  }
  // Methods to get the Unique records using native sql query
  public <T> T getUniqueRecord(Class<T> c, String sqlQry) {
    sqlQry = replaceAlias(sqlQry);
    Session session = sessionFactory.getCurrentSession();
    NativeQuery query = session.createNativeQuery(sqlQry);
    return (T) query.setResultTransformer(Transformers.aliasToBean(c)).uniqueResult();
  }

  public <T> T getUniqueRecord(Class<T> c, String sqlQry, int param0) {
    sqlQry = replace(sqlQry, 0);
    sqlQry = replaceAlias(sqlQry);
    Session session = sessionFactory.getCurrentSession();
    NativeQuery query = session.createNativeQuery(sqlQry);
    query.setParameter(0, param0);
    return (T) query.setResultTransformer(Transformers.aliasToBean(c)).uniqueResult();
  }

  public <T> T getUniqueRecord(Class<T> c, String sqlQry, String param0) {
    sqlQry = replace(sqlQry, 0);
    sqlQry = replaceAlias(sqlQry);
    Session session = sessionFactory.getCurrentSession();
    NativeQuery query = session.createNativeQuery(sqlQry);
    query.setParameter(0, param0);
    return (T) query.setResultTransformer(Transformers.aliasToBean(c)).uniqueResult();
  }

  public <T> T getUniqueRecord(Class<T> c, String sqlQry, int param0, int param1) {
    sqlQry = replace(sqlQry, 1);
    sqlQry = replaceAlias(sqlQry);
    Session session = sessionFactory.getCurrentSession();
    NativeQuery query = session.createNativeQuery(sqlQry);
    query.setParameter(0, param0);
    query.setParameter(1, param1);
    return (T) query.setResultTransformer(Transformers.aliasToBean(c)).uniqueResult();
  }

  // Methods to get the records using native sql queries
  public <T> List<T> getRecords(Class<T> c, String sqlQry, int param0) {
    sqlQry = replace(sqlQry, 0);
    sqlQry = replaceAlias(sqlQry);
    Session session = sessionFactory.getCurrentSession();
    NativeQuery query = session.createNativeQuery(sqlQry);
    query.setParameter(0, param0);
    return query.setResultTransformer(Transformers.aliasToBean(c)).list();
  }

  public <T> List<T> getRecords(Class<T> c, String sqlQry, String param0) {
    sqlQry = replace(sqlQry, 0);
    sqlQry = replaceAlias(sqlQry);
    Session session = sessionFactory.getCurrentSession();
    NativeQuery query = session.createNativeQuery(sqlQry);
    query.setParameter(0, param0);
    return query.setResultTransformer(Transformers.aliasToBean(c)).list();
  }

  public List getRecords(String sqlQry, int param0) {
    sqlQry = replace(sqlQry, 0);
    Session session = sessionFactory.getCurrentSession();
    NativeQuery query = session.createNativeQuery(sqlQry);
    query.setParameter(0, param0);
    return query.list();
  }

  public List getRecords(String sqlQry) {
    Session session = sessionFactory.getCurrentSession();
    return session.createNativeQuery(sqlQry).list();
  }

  public <T> List<T> getRecords(Class<T> c, String sqlQry, int param0, int param1) {
    sqlQry = replace(sqlQry, 1);
    sqlQry = replaceAlias(sqlQry);
    Session session = sessionFactory.getCurrentSession();
    NativeQuery query = session.createNativeQuery(sqlQry);
    query.setParameter(0, param0);
    query.setParameter(1, param1);
    return query.setResultTransformer(Transformers.aliasToBean(c)).list();
  }

  public <T> List<T> getRecords(Class<T> c, String sqlQry, int param0, String param1) {
    sqlQry = replace(sqlQry, 1);
    sqlQry = replaceAlias(sqlQry);
    Session session = sessionFactory.getCurrentSession();
    NativeQuery query = session.createNativeQuery(sqlQry);
    query.setParameter(0, param0);
    query.setParameter(1, param1);
    return query.setResultTransformer(Transformers.aliasToBean(c)).list();
  }

  public <T> List<T> getRecords(Class<T> c, String sqlQry) {
    sqlQry = replaceAlias(sqlQry);
    Session session = sessionFactory.getCurrentSession();
    NativeQuery query = session.createNativeQuery(sqlQry);
    return query.setResultTransformer(Transformers.aliasToBean(c)).list();
  }

  public <T> List<T> getRecords(Class<T> c, String sqlQry, Date param0, String param1) {
    sqlQry = replace(sqlQry, 1);
    sqlQry = replaceAlias(sqlQry);
    Session session = sessionFactory.getCurrentSession();
    NativeQuery query = session.createNativeQuery(sqlQry);
    query.setParameter(0, param0);
    query.setParameter(1, param1);
    return query.setResultTransformer(Transformers.aliasToBean(c)).list();
  }

  public <T> List<T> getRecords(Class<T> c, String sqlQry, String param0, String param1) {
    sqlQry = replace(sqlQry, 1);
    sqlQry = replaceAlias(sqlQry);
    Session session = sessionFactory.getCurrentSession();
    NativeQuery query = session.createNativeQuery(sqlQry);
    query.setParameter(0, param0);
    query.setParameter(1, param1);
    return query.setResultTransformer(Transformers.aliasToBean(c)).list();
  }

  public <T> List<T> getRecords(Class<T> c, String sqlQry, int param0, int param1, int param2) {
    sqlQry = replace(sqlQry, 2);
    sqlQry = replaceAlias(sqlQry);
    Session session = sessionFactory.getCurrentSession();
    NativeQuery query = session.createNativeQuery(sqlQry);
    query.setParameter(0, param0);
    query.setParameter(1, param1);
    query.setParameter(2, param2);
    return query.setResultTransformer(Transformers.aliasToBean(c)).list();
  }

  public <T> List<T> getRecords(Class<T> c, String sqlQry, int param0, int param1, String param2) {
    sqlQry = replace(sqlQry, 2);
    sqlQry = replaceAlias(sqlQry);
    Session session = sessionFactory.getCurrentSession();
    NativeQuery query = session.createNativeQuery(sqlQry);
    query.setParameter(0, param0);
    query.setParameter(1, param1);
    query.setParameter(2, param2);
    return query.setResultTransformer(Transformers.aliasToBean(c)).list();
  }

  // Methods to get the records using native sql queries with limits
  public <T> List<T> getRecordsWithLimit(
      Class<T> c,
      String sqlQry,
      int lastCount,
      int limit,
      List<Integer> param0,
      LocalDateTime param1,
      LocalDateTime param2) {
    sqlQry = replace(sqlQry, 2);
    sqlQry = replaceAlias(sqlQry);
    sqlQry = addLimitQuery(sqlQry);
    Session session = sessionFactory.getCurrentSession();
    NativeQuery query = session.createNativeQuery(sqlQry);
    query.setParameter(0, param0);
    query.setParameter(1, param1);
    query.setParameter(2, param2);
    query.setParameter("lastCount", lastCount);
    query.setParameter("limit", limit);
    return query.setResultTransformer(Transformers.aliasToBean(c)).list();
  }

  public <T> List<T> getRecordsWithLimit(Class<T> c, int lastCount, int limit, String sqlQry) {
    sqlQry = replaceAlias(sqlQry);
    sqlQry = addLimitQuery(sqlQry);
    Session session = sessionFactory.getCurrentSession();
    NativeQuery query = session.createNativeQuery(sqlQry);
    query.setParameter("lastCount", lastCount);
    query.setParameter("limit", limit);
    return query.setResultTransformer(Transformers.aliasToBean(c)).list();
  }

  public <T> List<T> getRecordsWithLimit(
      Class<T> c, int lastCount, int limit, String sqlQry, String param0) {
    sqlQry = replace(sqlQry, 0);
    sqlQry = replaceAlias(sqlQry);
    sqlQry = addLimitQuery(sqlQry);
    Session session = sessionFactory.getCurrentSession();
    NativeQuery query = session.createNativeQuery(sqlQry);
    query.setParameter("lastCount", lastCount);
    query.setParameter("limit", limit);
    query.setParameter(0, param0);
    return query.setResultTransformer(Transformers.aliasToBean(c)).list();
  }

  public <T> List<T> getRecordsWithLimit(
      Class<T> c, int lastCount, int limit, String sqlQry, String param0, String param1) {
    sqlQry = replace(sqlQry, 1);
    sqlQry = replaceAlias(sqlQry);
    sqlQry = addLimitQuery(sqlQry);
    Session session = sessionFactory.getCurrentSession();
    NativeQuery query = session.createNativeQuery(sqlQry);
    query.setParameter("lastCount", lastCount);
    query.setParameter("limit", limit);
    query.setParameter(0, param0);
    query.setParameter(1, param1);
    return query.setResultTransformer(Transformers.aliasToBean(c)).list();
  }

  public <T> List<T> getRecordsWithLimit(
      Class<T> c, int lastCount, int limit, String sqlQry, int param0, int param1) {
    sqlQry = replace(sqlQry, 1);
    sqlQry = replaceAlias(sqlQry);
    sqlQry = addLimitQuery(sqlQry);
    Session session = sessionFactory.getCurrentSession();
    NativeQuery query = session.createNativeQuery(sqlQry);
    query.setParameter("lastCount", lastCount);
    query.setParameter("limit", limit);
    query.setParameter(0, param0);
    query.setParameter(1, param1);
    return query.setResultTransformer(Transformers.aliasToBean(c)).list();
  }

  public <T> List<T> getRecordsWithLimit(
      Class<T> c, int lastCount, int limit, String sqlQry, String[] param0) {
    sqlQry = replace(sqlQry, 0);
    sqlQry = replaceAlias(sqlQry);
    sqlQry = addLimitQuery(sqlQry);
    Session session = sessionFactory.getCurrentSession();
    NativeQuery query = session.createNativeQuery(sqlQry);
    query.setParameter("lastCount", lastCount);
    query.setParameter("limit", limit);
    query.setParameterList(0, param0);
    return query.setResultTransformer(Transformers.aliasToBean(c)).list();
  }

  public <T> List<T> getRecordsWithLimit(
      Class<T> c, int lastCount, int limit, String sqlQry, int param0, String param1) {
    sqlQry = replace(sqlQry, 1);
    sqlQry = replaceAlias(sqlQry);
    sqlQry = addLimitQuery(sqlQry);
    Session session = sessionFactory.getCurrentSession();
    NativeQuery query = session.createNativeQuery(sqlQry);
    query.setParameter("lastCount", lastCount);
    query.setParameter("limit", limit);
    query.setParameter(0, param0);
    query.setParameter(1, param1);
    return query.setResultTransformer(Transformers.aliasToBean(c)).list();
  }

  public <T> List<T> getRecordsWithLimit(
      Class<T> c,
      int lastCount,
      int limit,
      String sqlQry,
      String param0,
      String param1,
      String param2) {

    sqlQry = replace(sqlQry, 2);
    sqlQry = replaceAlias(sqlQry);
    sqlQry = addLimitQuery(sqlQry);
    Session session = sessionFactory.getCurrentSession();
    NativeQuery query = session.createNativeQuery(sqlQry);
    query.setParameter("lastCount", lastCount);
    query.setParameter("limit", limit);
    query.setParameter(0, param0);
    query.setParameter(1, param1);
    query.setParameter(2, param2);
    return query.setResultTransformer(Transformers.aliasToBean(c)).list();
  }

  public <T> List<T> getRecordsWithLimit(
      Class<T> c, int lastCount, int limit, String sqlQry, int param0, int param1, int param2) {
    sqlQry = replace(sqlQry, 2);
    sqlQry = replaceAlias(sqlQry);
    sqlQry = addLimitQuery(sqlQry);
    Session session = sessionFactory.getCurrentSession();
    NativeQuery query = session.createNativeQuery(sqlQry);
    query.setParameter("lastCount", lastCount);
    query.setParameter("limit", limit);
    query.setParameter(0, param0);
    query.setParameter(1, param1);
    query.setParameter(2, param2);
    return query.setResultTransformer(Transformers.aliasToBean(c)).list();
  }

  public <T> List<T> getRecordsWithLimit(
      Class<T> c, int lastCount, int limit, String sqlQry, int param0, int param1, String param2) {
    sqlQry = replace(sqlQry, 2);
    sqlQry = replaceAlias(sqlQry);
    sqlQry = addLimitQuery(sqlQry);
    Session session = sessionFactory.getCurrentSession();
    NativeQuery query = session.createNativeQuery(sqlQry);
    query.setParameter("lastCount", lastCount);
    query.setParameter("limit", limit);
    query.setParameter(0, param0);
    query.setParameter(1, param1);
    query.setParameter(2, param2);
    return query.setResultTransformer(Transformers.aliasToBean(c)).list();
  }

  private String addLimitQuery(String sqlQry) {
    if (Utils.dataBaseType.contentEquals("mysql")) {
      sqlQry += " limit :lastCount, :limit";
    }
    if (Utils.dataBaseType.contentEquals("postgres")) {
      sqlQry += " limit :lastCount offset :limit";
    }
    return sqlQry;
  }

  /**
   * Utility method to replace alias names with quotes to make the query compatible with Postgres
   * and resultsetmapper for Hibernate nativesql. Suggested to use postgres sql query with quotes
   * while using alias names with a native query and avoid using 'as' identifier
   *
   * @param query
   * @return
   */
  private String replaceAlias(String query) {
    if (!Utils.dataBaseType.contentEquals("postgres")) {
      return query;
    }
    int count = StringUtils.countOccurrencesOf(query, " as ");
    if (count <= 0) {
      return query;
    }
    query = query.replaceAll("\\s*,\\s*", ",");
    while (count >= 0) {
      query = query.replaceFirst(" as ", "@@");
      int startIndex = query.indexOf("@@") + 2;
      int endIndex = query.indexOf(",", startIndex);
      if (endIndex == -1) {
        endIndex = query.indexOf(" ", startIndex);
      }
      String paramToken = query.substring(startIndex, endIndex);
      String repalceToken = "\"" + paramToken + "\"";
      query = query.replaceFirst("@@" + paramToken, " " + repalceToken);
      count--;
    }
    return query;
  }

  /**
   * Replacing ? with index parameter num
   *
   * @param query
   * @param count
   * @return
   */
  protected String replace(String query, int count) {
    int initialVal = count;
    StringBuffer buffer = new StringBuffer();
    while (count >= 0) {
      int val = initialVal - count;
      query = query.replaceFirst("\\?", "?" + val);
      int index = query.indexOf("?" + val) + 2;
      buffer.append(query.substring(0, index));
      query = query.substring(index, query.length());
      count--;
    }
    return buffer.toString();
  }
}
