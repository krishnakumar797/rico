/* Licensed under Apache-2.0 */
package ro.common.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import ro.common.utils.EntityDoc;

/**
 * User roles default class
 *
 * @author r.krishnakumar
 */
@Getter
@Setter
@Entity
public class UserRoles extends EntityDoc {

  @Column(name = "role_name")
  private String roleName;

  @JsonIgnore
  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;
}
