package edu.asu.securebanking.controller;

import edu.asu.securebanking.beans.AppUser;
import edu.asu.securebanking.beans.PageViewBean;
import edu.asu.securebanking.constants.AppConstants;
import edu.asu.securebanking.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

/**
 * @author Welcome Controller
 */
@Controller
public class WelcomeController {

    @Autowired
    private EmailService emailService;

    /**
     * @param model
     * @return view
     * <p>
     * Welcome hack
     */
    @RequestMapping(value = {"/index.html", ""}, method = RequestMethod.GET)
    public String welcome(final Model model,
                          HttpSession session) {

        // If loggedIn then redirect
        AppUser user = (AppUser) session.getAttribute(AppConstants.LOGGEDIN_USER);
        if (user != null) {
            String redirect = AppConstants.ROLE_URL_MAP.get(user.getUserType());
            return "redirect:/" + redirect + "/";
        }

        PageViewBean page = new PageViewBean();
        model.addAttribute("page", page);

        return "welcome";
    }
}