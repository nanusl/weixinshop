package com.github.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @author nanusl
 * @version V1.0
 * @Description
 * @date 2021-01-05 13:38
 */
public abstract class BaseController {

    @Value("${server.servlet.context-path:/}")
    private String contextPath;
    @Autowired
    HttpServletRequest request;
    @Autowired
    ApplicationContext applicationContext;
    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;

    /**
     * Get model view
     **/
    protected ModelAndView getJsonModelView() {
        return new ModelAndView(new MappingJackson2JsonView());
    }

    protected void renderHtml(HttpServletResponse response, String view) throws IOException {
        renderHtml(response, view, null);
    }

    protected void renderHtml(HttpServletResponse response, String view, Map<String, Object> variables) throws IOException {
        WebContext ctx = new WebContext(request, response, request.getServletContext(), request.getLocale(), variables);

        response.setCharacterEncoding("UTF-8");
        response.setDateHeader("Expires", 0);
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/html;charset=UTF-8");

        thymeleafViewResolver.getTemplateEngine().process(view, ctx, response.getWriter());
    }

}
