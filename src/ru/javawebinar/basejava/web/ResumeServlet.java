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
import java.util.*;

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
        String action=request.getParameter("action");
        System.out.println(action);
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

        switch (action)
        {
            case "add":
                List<Organization> listNewOrgExp=new ArrayList<>();
                List<Organization> listNewOrgEdu=new ArrayList<>();

                for (Map.Entry<SectionType, Section> e : r.getSections().entrySet()) {
                    SectionType t = e.getKey();
                    Section section = e.getValue();
                    switch (t)
                    {
                        case PERSONAL:
                        case OBJECTIVE:
                            String PerObj = ((TextSection) section).getContent();
                            r.addSection(t,new TextSection(PerObj));
                            break;
                        case ACHIEVEMENT:
                        case QUALIFICATIONS:
                            List<String> li = ((ListSection) section).getItems();
                            r.addSection(t,new ListSection(li));
                            break;
                        case EXPERIENCE:
                            listNewOrgExp = ((OrganizationSection) section).getOrganizations();
                            break;
                        case EDUCATION:
                            listNewOrgEdu = ((OrganizationSection) section).getOrganizations();
                            break;
                    }
                }

                for (SectionType type : SectionType.values()) {
                    String value = request.getParameter(type.name());
                    if (value != null && value.trim().length() != 0) {
                        switch (type) {
                            case EXPERIENCE:
                            case EDUCATION:
                                String name=request.getParameter(type.name()+"_name");
                                String url=request.getParameter(type.name() + "_urlNew");
                                String title = request.getParameter(type.name() + "_titleNew");
                                String startDate = request.getParameter(type.name() + "_startDateNew");
                                String endDate = request.getParameter(type.name() + "_endDateNew");
                                String description = request.getParameter(type.name() + "_descriptionNew");
                                if (type.equals(SectionType.EXPERIENCE) && startDate!=null) {
                                    listNewOrgExp.add(addNewSection(name, url, title, startDate, endDate, description));
                                    r.addSection(type,new OrganizationSection(listNewOrgExp));
                                }else
                                if (type.equals(SectionType.EDUCATION) && startDate!=null) {
                                    listNewOrgEdu.add(addNewSection(name, url, title, startDate, endDate, description));
                                    r.addSection(type,new OrganizationSection(listNewOrgEdu));
                                }
                        }
                    } else {
                        r.getSections().remove(type);
                    }
                }
                break;

            case "addPosition":
                List<Organization> listNewOrgExpP=new ArrayList<>();
                List<Organization> listNewOrgEduP=new ArrayList<>();

                for (SectionType type : SectionType.values()) {
                    String value = request.getParameter(type.name());
                    String[] values = request.getParameterValues(type.name());
                    if (value != null && value.trim().length() != 0) {
                        switch (type) {
                            case EXPERIENCE:
                            case EDUCATION:
                               ExperEducationEditPosition(values,request,type,listNewOrgExpP,listNewOrgEduP,r);
                    }
                    } else {
                        r.getSections().remove(type);
                    }
                }
                break;

            case "edit":
                List<Organization> listNewOrgExpEdit=new ArrayList<>();
                List<Organization> listNewOrgEduEdit=new ArrayList<>();
                for (SectionType type : SectionType.values()) {
                    String value = request.getParameter(type.name());
                    String[] values = request.getParameterValues(type.name());
                    if (value != null && value.trim().length() != 0) {
                        switch (type)
                        {
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
                                ExperEducationEditPosition(values,request,type,listNewOrgExpEdit,listNewOrgEduEdit,r);
                                break;
                        }
                    } else {
                        r.getSections().remove(type);
                    }
                }
                break;
        }
        storage.update(r);
        response.sendRedirect("resume");
    }


    private void ExperEducationEditPosition(String[] values, HttpServletRequest request, SectionType type, List<Organization> listNewOrgExpP, List<Organization> listNewOrgEduP, Resume r)
    {
        for (String org:values) {
            int c = 0;
            String[] title = request.getParameterValues(type.name() + "_title_"+org);
            String[] url = request.getParameterValues(type.name() + "_url_"+org);
            String[] startDate = request.getParameterValues(type.name() + "_startDate_"+org);
            String[] endDate = request.getParameterValues(type.name() + "_endDate_"+org);
            String[] description = request.getParameterValues(type.name() + "_description_"+org);
            if (type.equals(SectionType.EXPERIENCE) && startDate != null) {
                ArrayList<Organization.Position> list = addNewPositionm(title, startDate, endDate, description);
                listNewOrgExpP.add(new Organization(org, url[c], list.toArray(new Organization.Position[list.size()])));
                r.addSection(type, new OrganizationSection(listNewOrgExpP));
                c++;
            } else if (type.equals(SectionType.EDUCATION) && startDate != null) {
                ArrayList<Organization.Position> list = addNewPositionm(title, startDate, endDate, description);
                listNewOrgEduP.add(new Organization(org, url[c], list.toArray(new Organization.Position[list.size()])));
                r.addSection(type, new OrganizationSection(listNewOrgEduP));
                c++;
            }
        }
    }


    private ArrayList<Organization.Position> addNewPositionm(String[] titlem, String[] startDatem, String[] endDatem, String[] descriptionm) {
        ArrayList<Organization.Position>positions=new ArrayList<>();
        for (int i = 0; i <titlem.length ; i++) {
            if (titlem[i]==null) break;
            LocalDate startd = LocalDate.parse(startDatem[i]);
            LocalDate endd= DateParseEnd(endDatem[i]);
            positions.add(new Organization.Position(startd,endd,titlem[i],descriptionm[i]));
        }
        return positions;
    }

    private Organization addNewSection(String org , String url, String title, String startDate, String endDate, String description)
    {
        LocalDate startd = LocalDate.parse(startDate);
        LocalDate endd= DateParseEnd(endDate);
        return new Organization(org, url,new Organization.Position(startd,endd,title,description));
    }

    private LocalDate DateParseEnd(String endDate) {
        LocalDate endd;
        if (endDate.equals("")) {
            return endd = DateUtil.NOW;
        } else
            return endd = LocalDate.parse(endDate);
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
            case "addPosition":
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
            case "addPosition":
                request.getRequestDispatcher("/WEB-INF/jsp/addPosition.jsp").forward(request, response);
                break;
        }
    /*
        request.getRequestDispatcher(
                ("view".equals(action) ? "/WEB-INF/jsp/view.jsp" : "/WEB-INF/jsp/edit.jsp")
        ).forward(request, response);
        */
    }
}
