package ru.netology.servlet;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.netology.controller.PostController;
import ru.netology.exception.NotFoundException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MainServlet extends HttpServlet {
    PostController controller;
    private static final String GET_METHOD = "GET";
    private static final String POST_METHOD = "POST";
    private static final String DELETE_METHOD = "DELETE";
    private static final String POST_PATH = "/api/posts";
    private static final String POST_PATH_D = "/api/posts/\\d+";
    private static final String SLASH = "/";

    @Override
    public void init() {
        System.out.println("Servlet initialized! Get context!");
        final var context = new AnnotationConfigApplicationContext("ru.netology");
        controller = context.getBean(PostController.class);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        // если деплоились в root context, то достаточно этого
        try {
            final var path = req.getRequestURI();
            final var method = req.getMethod();
            // primitive routing
            if (method.equals(GET_METHOD) && path.equals(POST_PATH)) {
                controller.all(resp);
                return;
            }
            if (method.equals(GET_METHOD) && path.matches(POST_PATH_D)) {
                // easy way
                final var id = Long.parseLong(path.substring(path.lastIndexOf(SLASH)) + 1);
                controller.getById(id, resp);
                return;
            }
            if (method.equals(POST_METHOD) && path.equals(POST_PATH)) {
                controller.save(req.getReader(), resp);
            }
            if (method.equals(DELETE_METHOD) && path.matches(POST_PATH_D)) {
                // easy way
                final var id = Long.parseLong(path.substring(path.lastIndexOf(SLASH)) + 1);
                controller.removeById(id, resp);
            }
            resp.setStatus(resp.SC_OK);
        }catch (NotFoundException e) {
            e.printStackTrace();
            resp.setStatus(resp.SC_NOT_FOUND);
        }catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
