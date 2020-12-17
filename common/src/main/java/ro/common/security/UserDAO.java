/* Licensed under Apache-2.0 */
package ro.common.security;

/**
 * Common UserDAO
 *
 * @author r.krishnakumar
 */
public interface UserDAO {

  User getUserByUsername(String username);

  User registerUser(User user);
}
