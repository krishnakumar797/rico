package ro.common.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Repository;
import ro.common.hibernate.GenericDAO;

/**
 * DAO implementation class for UserDAO
 *
 * @author r.krishnakumar
 */
@Repository("securityDAO")
@DependsOn("hConfig")
@ConditionalOnBean(name = "hConfig")
public class UserSecurityDAO implements UserDAO {

    @Autowired
    private GenericDAO genericDAO;

    @Override
    public User getUserByUsername(String username) {
        String hql = "from User where username = ?";
        return genericDAO.getEntityByStringParam(hql, username);
    }
}
