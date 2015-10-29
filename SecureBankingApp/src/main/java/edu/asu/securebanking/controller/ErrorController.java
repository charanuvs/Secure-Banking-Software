package edu.asu.securebanking.controller;

import edu.asu.securebanking.beans.PageViewBean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Vikranth on 10/28/2015.
 */
@Controller
public class ErrorController {

    /**
     * Access denied error page
     *
     * @return view
     */
    @RequestMapping(value = "/error/access", method = RequestMethod.GET)
    public String error403(Model model) {
        PageViewBean page = new PageViewBean();

        model.addAttribute("page", page);

        page.setValid(false);
        page.setMessage("Access denied");
        return "message";
    }

    /**
     * @return view
     */
    @RequestMapping(value = "/error/", method = RequestMethod.GET)
    public String internalError(Model model) {
        PageViewBean page = new PageViewBean();

        model.addAttribute("page", page);

        page.setValid(false);
        page.setMessage("Something is wrong. Please try again later.");

        return "message";
    }

    /**
     * @return view
     */
    @RequestMapping(value = "/error/404", method = RequestMethod.GET)
    public String error404(Model model) {
        PageViewBean page = new PageViewBean();

        model.addAttribute("page", page);

        page.setValid(false);
        page.setMessage("404 Error. You probably want to try a different URL");

        return "message";
    }
}
