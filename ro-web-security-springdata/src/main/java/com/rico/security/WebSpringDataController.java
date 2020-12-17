/* Licensed under Apache-2.0 */
package com.rico.security;

import com.rico.security.domain.UserDTO;
import com.rico.security.services.UserService;
import com.rico.security.utils.SecurityConstants;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import ro.common.exception.GenericServiceException;
import ro.common.rest.CommonDTOConverter;
import ro.common.security.User;
import ro.common.security.UserLoginService;
import ro.common.security.UserRoles;

/** Testing Web controller */
@Controller
@Validated
@Log4j2
public class WebSpringDataController {

  @Autowired private CommonDTOConverter dtoConverter;

  @Autowired private UserLoginService loginService;

  @Autowired private UserService userService;

  @GetMapping(value = {"/index", "/"})
  public String homePage(HttpServletRequest request) {
    return "thyme/index";
  }

  @GetMapping(value = {"/login"})
  public String login(HttpServletRequest request) {
    return "jsp/login/login";
  }

  @GetMapping(value = {"/register"})
  public ModelAndView registerForm() {
    return new ModelAndView("jsp/register/register", "user", new UserDTO());
  }

  @GetMapping(value = {"/registerResult"})
  public String registerResult(Model model) {
    return "thyme/registerResult";
  }

  @GetMapping(value = {"/welcome"})
  public String welcome(HttpSession session, Model model) {
    try {
      Optional<User> user = userService.getUserById(((Long) session.getAttribute("userid")));
      model.addAttribute("name", user.isPresent() ? user.get().getUsername() : "");
    } catch (GenericServiceException e) {
      log.error("Error at welcome page ", e);
    }
    return "thyme/welcome";
  }

  /**
   * POST Method to register user with Admin role
   *
   * @param userDTO
   * @return
   */
  @PostMapping(value = "/register")
  public RedirectView register(
      @Valid @ModelAttribute("user") UserDTO userDTO,
      BindingResult result,
      RedirectAttributes redir) {
    try {
      User user = dtoConverter.convertToEntity(userDTO, User.class);
      UserRoles ur = new UserRoles();
      ur.setRoleName(SecurityConstants.ADMIN_ROLE);
      user.addRole(ur);
      loginService.register(user);
      redir.addFlashAttribute("result", "success");
    } catch (GenericServiceException e) {
      redir.addFlashAttribute("result", "failed");
      redir.addFlashAttribute("message", e.getMessage());
      log.error("Register error ", e);
    }
    RedirectView redirectView = new RedirectView("/registerResult", true);
    return redirectView;
  }
}
