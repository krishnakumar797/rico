/* Licensed under Apache-2.0 */
package ro.common.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ro.common.utils.EntityDoc;

/**
 * User default class
 *
 * @author r.krishnakumar
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
public class User extends EntityDoc implements UserDetails {

  @Column(name = "user_name", unique = true)
  private String userName;

  @Column(name = "password")
  private String password;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private List<UserRoles> roles = new ArrayList<>();

  @Transient private List<GrantedAuthority> authorities;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public String getUsername() {
    return userName;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  /**
   * Method to add role for the user
   *
   * @param role
   */
  public void addRole(UserRoles role) {
    if (role != null) {
      role.setUser(this);
      this.roles.add(role);
    }
  }

  /**
   * Method to remove role from the user
   *
   * @param role
   */
  public void removeRole(UserRoles role) {
    if (role != null) {
      role.setUser(null);
    }
    this.roles.remove(role);
  }
}
