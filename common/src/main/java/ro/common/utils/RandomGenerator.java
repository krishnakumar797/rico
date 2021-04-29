package ro.common.utils;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.util.Properties;
import java.util.Random;

/** Random Generator for Primary Key */
public class RandomGenerator implements IdentifierGenerator, Configurable {

  @Override
  public Serializable generate(SharedSessionContractImplementor session, Object obj)
      throws HibernateException {
    return Math.abs(new Random().nextLong());
  }

  @Override
  public void configure(Type type, Properties properties, ServiceRegistry serviceRegistry)
      throws MappingException {
    // NoOPS
  }
}
