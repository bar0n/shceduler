package com.bar0n.shceduler;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class ViewController {

    private UrlPathHelper urlPathHelper = new UrlPathHelper();;


    @RequestMapping({ "/scheduleLog", "/scheduleAdd", "/scheduleEdit/{id:[\\d|-]+}", "/schedule"})

    public String redirect(HttpServletResponse response, HttpServletRequest httpServletRequest) throws IOException {
        String originatingRequestUri = urlPathHelper.getOriginatingRequestUri(httpServletRequest);
        String s = "/#" + originatingRequestUri;
        response.sendRedirect(s);
        return null;
    }
}