package ro.common.security;

import lombok.Getter;
import lombok.Setter;
import ro.common.utils.EntityDoc;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

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

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
