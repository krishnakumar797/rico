package ro.common.security;

/**
 * Common UserDAO
 *
 * @author r.krishnakumar
 */
public interface UserDAO {

    User getUserByUsername(String username);
}
