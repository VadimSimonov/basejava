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
        ArrayList<String> arrayListEXPERIENCE = new ArrayList<>();
        ArrayList<String> arrayListEDUCATION = new ArrayList<>();

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
                        int index=-1;
                        String startDateNew = null;
                        String endDateNew=null;
                        String urlNew = null;
                        String titleNew= null;
                        String descriptionNew= null;

                        String title=null;
                        String startDate=null;
                        String endDate=null;
                        String url=null;
                        String description=null;

                        for (String org : values) {
                            List<Organization> listNewOrg=new ArrayList<>();
                            boolean flag=false;

                            for (Map.Entry<SectionType, Section> e : r.getSections().entrySet()) {
                                SectionType t = e.getKey();
                                Section section = e.getValue();

                                if (t.equals(type) && t.equals(SectionType.EXPERIENCE))
                                {
                                    url= request.getParameter(type.name() + "_url");
                                    title = request.getParameter(type.name() + "_title");
                                    startDate = request.getParameter(type.name() + "_startDate");
                                    endDate = request.getParameter(type.name() + "_endDate");
                                    description = request.getParameter(type.name() + "_description");
                                        List<Organization> liste = ((OrganizationSection) section).getOrganizations();
                                        for (Organization f : liste) {
                                            List<Organization.Position> list2 = f.getPositions();
                                            for (Organization.Position p : list2) {
                                                index = arrayListEXPERIENCE.indexOf(title + startDate);
                                                LocalDate date = p.getStartDate();
                                                LocalDate datenew = LocalDate.parse(startDate);
                                                if (p.getTitle().equals(title) && date.isEqual(datenew) && index == -1) {
                                                    flag = true;
                                                    arrayListEXPERIENCE.add(title + startDate);
                                                }
                                            }
                                        }
                                ///    LocalDate startd=DateParseStart(startDate);
                                //    LocalDate endd= DateParseEnd(endDate);

                                //    listNewOrg.add(new Organization(org, url,new Organization.Position(startd,endd,title,description)));
                                //    r.addSection(type,new OrganizationSection(listNewOrg));
                                }

                                else if (t.equals(type) && t.equals(SectionType.EDUCATION))
                                {
                                    url= request.getParameter(type.name() + "_url");
                                    title = request.getParameter(type.name() + "_title");
                                    startDate = request.getParameter(type.name() + "_startDate");
                                    endDate = request.getParameter(type.name() + "_endDate");
                                    description = request.getParameter(type.name() + "_description");
                                    List<Organization> list = ((OrganizationSection) section).getOrganizations();
                                    for (Organization f : list) {
                                        List<Organization.Position> list2 = f.getPositions();
                                        for (Organization.Position p : list2) {
                                            index = arrayListEDUCATION.indexOf(title + startDate);
                                            LocalDate date = p.getStartDate();
                                            LocalDate datenew = LocalDate.parse(startDate);
                                            if (p.getTitle().equals(title) && date.isEqual(datenew) && index == -1) {
                                                flag = true;
                                                arrayListEDUCATION.add(title + startDate);
                                            }
                                        }
                                    }
                             //       LocalDate startd=DateParseStart(startDate);
                             //       LocalDate endd= DateParseEnd(endDate);

                              //      listNewOrg.add(new Organization(org, url,new Organization.Position(startd,endd,title,description)));
                             //       r.addSection(type,new OrganizationSection(listNewOrg));
                                }
                                }
                        if (flag)
                            {
                                url= request.getParameter(type.name() + "_url");
                                startDate = request.getParameter(type.name() + "_startDate");
                                endDate = request.getParameter(type.name() + "_endDate");
                                description = request.getParameter(type.name() + "_description");

                                LocalDate startd = LocalDate.parse(startDate);
                                LocalDate endd= DateParseEnd(endDate);

                                listNewOrg.add(new Organization(org, url,new Organization.Position(startd,endd,title,description)));
                                r.addSection(type,new OrganizationSection(listNewOrg));


                            }else if (!flag)
                            {
                                url=request.getParameter(type.name() + "_urlNew");
                                title = request.getParameter(type.name() + "_titleNew");
                                startDate = request.getParameter(type.name() + "_startDateNew");
                                endDate = request.getParameter(type.name() + "_endDateNew");
                                description = request.getParameter(type.name() + "_descriptionNew");


                                LocalDate startd=DateParseStart(startDate);
                                LocalDate endd= DateParseEnd(endDate);

                                /*
                                r.addSection(type, new OrganizationSection(
                                        new Organization(org, url,
                                                new Organization.Position(startd, endd, title, description))));
                                */
                                listNewOrg.add(new Organization(org, url,new Organization.Position(startd,endd,title,description)));
                                r.addSection(type,new OrganizationSection(listNewOrg));
                                //listNewPosition.add(new Organization.Position(startd,endd,title,description));
                            }
                            }
                        }

               // Organization listNewOrgEDUCATION1 = null;
              //  r.addSection(type, new OrganizationSection(listNewOrgEDUCATION1));
                /*
                for (int i = 0; i <listNewOrg.size() ; i++) {
                    listNewOrg.get(i).getPositions();
                }
                */

              //  for (Organization e:listNewOrg) {
               //     List<Organization.Position> positions = e.getPositions();
                   // OrganizationSection[] Org = positions.toArray(new OrganizationSection[positions.size()]);
                  //  for (Organization.Position p : positions) {
                    /*
                        r.addSection(type,new OrganizationSection(
                                new Organization(e.getHomePage().getName(),e.getHomePage().getUrl(),
                                        method(positions))));
                        */
/*
                r.addSection(type,new OrganizationSection(
                                     new Organization(e.getHomePage().getName(),e.getHomePage().getUrl(),
                                             method(method2(listNewOrg)))));
                */
                        /*
                    ArrayList<String>arrayList=new ArrayList<>();
                    arrayList.add("1");
                    OrganizationSection[] d = arrayList.toArray(new OrganizationSection[arrayList.size()]);
                    OrganizationSection[] Org=listNewOrg.toArray(new OrganizationSection[listNewOrg.size()]);
                    r.addSection(type,new OrganizationSection(new Organization()));
                    */
                  //  }
             //   }


            } else {
                r.getSections().remove(type);
            }
        }

        storage.update(r);
        response.sendRedirect("resume");
    }

    private LocalDate DateParseStart(String startDate) {
        return LocalDate.parse(startDate);
    }

    private LocalDate DateParseEnd(String endDate) {
        LocalDate endd;
        if (endDate.equals("")) {
            return endd = DateUtil.NOW;
        } else
            return endd = LocalDate.parse(endDate);
    }

    private List<Organization.Position> method2(List<Organization> listNewOrgEDUCATION)
    {

        List<Organization.Position> positions = new ArrayList<>();
        for (Organization e:listNewOrgEDUCATION) {
            positions = e.getPositions();
        }
        return positions;

    }

    private Organization.Position method(List<Organization.Position> positions) {
        for (Organization.Position p : positions) {
            return new Organization.Position(p.getStartDate(),p.getEndDate(),p.getTitle(),p.getDescription());
        }
        return null;
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
