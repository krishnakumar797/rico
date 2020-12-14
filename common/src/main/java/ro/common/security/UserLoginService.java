package ro.common.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.common.utils.Utils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Service class for user login
 *
 * @author r.krishnakumar
 */
@Service
@Transactional
@DependsOn({"security"})
public class UserLoginService implements UserDetailsService {

    @Autowired
    private ConfigurableApplicationContext context;

    private UserDAO userDAO;

    @PostConstruct
    public void init() {
        if (Utils.persistenceType.contentEquals("springData")) {
            userDAO = (UserDAO) context.getBean("securityRepository");
        } else if (Utils.persistenceType.contentEquals("hibernate")) {
            userDAO = (UserDAO) context.getBean("securityDAO");
        }
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        try {

            User user = userDAO.getUserByUsername(username.trim());

            if (user != null) {
                List<GrantedAuthority> authorities = buildUserAuthority(user.getRoles());
                if (authorities != null && authorities.size() == 0) {
                    throw new UsernameNotFoundException("No role defined");
                }
                user.setAuthorities(authorities);
                return user;
            }
            throw new UsernameNotFoundException("Username not found");
        } catch (Exception e) {
            throw new UsernameNotFoundException("Username is required");
        }

    }

    /**
     * Setting required authorities for the user
     *
     * @param userRoles
     * @return
     */
    private List<GrantedAuthority> buildUserAuthority(List<UserRoles> userRoles) {
        Set<GrantedAuthority> setAuths = new HashSet<GrantedAuthority>();
        for (UserRoles userRole : userRoles) {
            setAuths.add(new SimpleGrantedAuthority(userRole.getRoleName()));
        }
        List<GrantedAuthority> Result = new ArrayList<GrantedAuthority>(setAuths);
        return Result;
    }
}