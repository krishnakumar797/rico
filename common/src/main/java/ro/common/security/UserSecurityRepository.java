package ro.common.security;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Repository;
import ro.common.springdata.CommonCrudRepository;

/**
 * User security repository for loading user details
 *
 * @author r.krishnakumar
 */
@Repository("securityRepository")
@DependsOn("hConfig")
@ConditionalOnMissingBean(name = "hConfig")
public interface UserSecurityRepository extends UserDAO, CommonCrudRepository<User, Long> {

    default User getUserByUsername(String userName) {
        return findByUserName(userName);
    }

    User findByUserName(String userName);
}
