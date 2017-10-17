package ru.javawebinar.basejava.web;

import ru.javawebinar.basejava.Config;
import ru.javawebinar.basejava.model.*;
import ru.javawebinar.basejava.storage.Storage;
import ru.javawebinar.basejava.util.DateUtil;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.HashMap;
import java.util.Map;

public class ResumeServlet extends HttpServlet {

    private Storage storage; // = Config.get().getStorage();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        storage = Config.get().getStorage();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String uuid = request.getParameter("uuid");
        String fullName = request.getParameter("fullName");
        Resume r = storage.get(uuid);
        r.setFullName(fullName);
        for (ContactType type : ContactType.values()) {
            String value = request.getParameter(type.name());
            if (value != null && value.trim().length() != 0) {
                r.addContact(type, value);
            } else {
                r.getContacts().remove(type);
            }
        }
        for (SectionType type : SectionType.values()) {
            String value = request.getParameter(type.name());
            String[] values = request.getParameterValues(type.name());
            if (value != null && value.trim().length() != 0) {
                switch (type) {
                    case PERSONAL:
                    case OBJECTIVE:
                        r.addSection(type, new TextSection(value));
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        r.addSection(type, new ListSection(value.split("\n")));
                        break;
                    case EXPERIENCE:
                    case EDUCATION:
                        for (String org : values
                                ) {
                            String url = request.getParameter(type.name() + "_url");
                            String title = request.getParameter(type.name() + "_title");
                            String startDate = request.getParameter(type.name() + "_startDate");
                            String endDate = request.getParameter(type.name() + "_endDate");
                            String description = request.getParameter(type.name() + "_description");

                                /*
                                DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                                        .appendPattern("yyyy/MM")
                                        .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
                                        .toFormatter();
                                LocalDate startd = LocalDate.parse(startDate, formatter);
*/
                            LocalDate startd = LocalDate.parse(startDate);
                            LocalDate endd;
                            if (endDate.equals("")) {
                                endd = DateUtil.NOW;
                            } else
                                endd = LocalDate.parse(endDate);
                            r.addSection(type, new OrganizationSection(
                                    new Organization(org, url,
                                            new Organization.Position(startd, endd, title, description))));
                        }
                        break;
                }
            } else {
                r.getSections().remove(type);
            }
        }
        for (SectionType type1 : SectionType.values()) {
            String value1 = request.getParameter(type1.name());
            String[] values1 = request.getParameterValues(type1.name());
            if (value1 != null && value1.trim().length() != 0) {
                if (type1.getTitle().equals("EXPERIENCE")||type1.getTitle().equals("EDUCATION"))
                {

                        for (String org : values1
                                ) {
                            String url = request.getParameter(type1.name() + "_urlNew");
                            String title = request.getParameter(type1.name() + "_titleNew");
                            String startDate = request.getParameter(type1.name() + "_startDateNew");
                            String endDate = request.getParameter(type1.name() + "_endDateNew");
                            String description = request.getParameter(type1.name() + "_descriptionNew");

                            LocalDate startd = LocalDate.parse(startDate);
                            LocalDate endd;
                            if (endDate.equals("")) {
                                endd = DateUtil.NOW;
                            } else
                                endd = LocalDate.parse(endDate);
                            r.addSection(type1, new OrganizationSection(
                                    new Organization(org, url,
                                            new Organization.Position(startd, endd, title, description))));
                        }
                        break;
                }


            } else {
                r.getSections().remove(type1);
            }
        }

        storage.update(r);
        response.sendRedirect("resume");
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        String uuid = request.getParameter("uuid");
        String action = request.getParameter("action");
        if (action == null) {
            request.setAttribute("resumes", storage.getAllSorted());
            request.getRequestDispatcher("/WEB-INF/jsp/list.jsp").forward(request, response);
            return;
        }
        Resume r;
        switch (action) {
            case "delete":
                storage.delete(uuid);
                response.sendRedirect("resume");
                return;
            case "view":
            case "edit":
            case "add":
                r = storage.get(uuid);
                break;
            default:
                throw new IllegalArgumentException("Action " + action + " is illegal");
        }
        request.setAttribute("resume", r);
        switch (action) {
            case "view":
                request.getRequestDispatcher("/WEB-INF/jsp/view.jsp").forward(request, response);
                break;
            case "edit":
                request.getRequestDispatcher("/WEB-INF/jsp/edit.jsp").forward(request, response);
                break;
            case "add":
                request.getRequestDispatcher("/WEB-INF/jsp/add.jsp").forward(request, response);
                break;
        }
    /*
        request.getRequestDispatcher(
                ("view".equals(action) ? "/WEB-INF/jsp/view.jsp" : "/WEB-INF/jsp/edit.jsp")
        ).forward(request, response);
        */
    }
}
