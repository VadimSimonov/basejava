package ru.javawebinar.basejava.web;

import ru.javawebinar.basejava.Config;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.storage.Storage;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ResumeServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
//        response.setHeader("Content-Type", "text/html; charset=UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        String uuid = request.getParameter("uuid");
      //  response.getWriter().write(uuid == null ? "Hello Resumes!" : "Hello " + uuid + '!');

        Storage storage  = Config.get().getStorage();
        List<Resume> list=storage.getAllSorted();

        response.getWriter().write("<table border=\"1\">");
        response.getWriter().write("<tr>");
        response.getWriter().write("<th>"+"UUID"+"</th>");
        response.getWriter().write("<th>"+"Full name"+"</th>");
        response.getWriter().write("</tr>");
        for (Resume a:list
             ) {
            response.getWriter().write("<tr>");
            response.getWriter().write("<td>"+a.getUuid()+"</td>");
            response.getWriter().write("<td>"+a.getFullName()+"</td>");
            response.getWriter().write("</tr>");
        }

        response.getWriter().write("</table>");

        }

}
