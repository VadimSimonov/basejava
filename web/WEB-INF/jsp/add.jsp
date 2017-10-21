<%@ page import="ru.javawebinar.basejava.storage.Storage" %>
<%@ page import="ru.javawebinar.basejava.model.*" %>
<%@ page import="ru.javawebinar.basejava.util.DateUtil" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <jsp:useBean id="resume" type="ru.javawebinar.basejava.model.Resume" scope="request"/>
    <title>Резюме ${resume.fullName}</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<section>
    <form method="post" action="resume?action=add" enctype="application/x-www-form-urlencoded">
        <input type="hidden" name="uuid" value="${resume.uuid}">
        <input type="hidden" name="fullName" value="${resume.fullName}">
        <c:set var="contacts" value="<%=resume.getContacts()%>"> </c:set>
        <%--load contact --%>
        <c:forEach var="type" items="<%=ContactType.values()%>">
            <input type="hidden" name="${type.title}">
            <input type="hidden" name="${type.name()}" value="${resume.getContact(type)}">
        </c:forEach>
        <%--load contact --%>

        <%--load sections --%>
        <c:forEach var="SectionType" items="${resume.sections}">
            <jsp:useBean id="SectionType" type="java.util.Map.Entry<ru.javawebinar.basejava.model.SectionType, ru.javawebinar.basejava.model.Section>"/>
            <c:set var="type" value="${SectionType.key}"/>
            <c:set var="section" value="${SectionType.value}"/>
            <jsp:useBean id="section" type="ru.javawebinar.basejava.model.Section"/>
            <input type="hidden" name="${type.title}">
            <c:choose>
                <c:when test="${type.name()=='OBJECTIVE' || type.name()=='PERSONAL' }">
                    <input type="hidden" name="${type.name()}" value="<%= ((TextSection) section).getContent()%>">
                </c:when>
                <c:when test="${type.name()=='ACHIEVEMENT' || type.name()=='QUALIFICATIONS' }">
                    <input type="hidden" name="${type}" value="<%=String.join("\n",((ListSection) section).getItems())%>">
                </c:when>
                <c:when test="${type.name()=='EXPERIENCE' || type.name()=='EDUCATION' }">
                    <c:forEach items="<%=((OrganizationSection) section).getOrganizations()%>" var="organizations">
                        <input type="hidden" name="${type}" value="${organizations.homePage.name}">
                        <input type="hidden" name="${type}_url" value="${organizations.homePage.url}">
                        <c:forEach items="${organizations.positions}" var="positions">
                           <input type="hidden" name="${type}_title" value="${positions.title}">
                            <fmt:parseDate value="${positions.startDate}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                            <fmt:formatDate value="${parsedDate}" var="stdDatum" type="date" pattern="yyyy/MM" />
                            <input type="hidden" name="${type}_startDate" value="${positions.startDate}">

                                <fmt:parseDate value="${positions.endDate}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                                <fmt:formatDate value="${parsedDate}" var="stdDatum" type="date" pattern="yyyy/MM" />
                                <input type="hidden" name="${type}_endDate" value="${positions.endDate}">

                            <input type="hidden" name="${type}_description" value="${positions.description}">
                        </c:forEach>
                    </c:forEach>
                </c:when>
            </c:choose>
            </dl>
        </c:forEach>

        <%--load sections --%>
        <dl>
            <dt>Имя:</dt>
            <dd>${resume.fullName}</dd>
        </dl>
        <c:set var="type" value='<%=request.getParameter("form")%>'></c:set>
        <c:choose>
            <c:when test="${type=='EXPERIENCE'}">
                <h3>Опыт работы</h3>
            </c:when>
            <c:when test="${type=='EDUCATION'}">
                <h3>Образование</h3>
            </c:when>
        </c:choose>
        <b>Название:</b>
        <%--
        <dd><input name="${type}" type="text" size="50" value=""><br/></dd><br/>
        <b>URL:</b>
        <dd><input name="${type}_urlNew" type="text" size="50" value=""><br/></dd>
        <br/><br/><b>Должность:</b>
        <dd><input name="${type}_titleNew" type="text" size="50" value=""><br/></dd>
        <br/><b>Дата начала:</b>
        <dd><input name="${type}_startDateNew" type="date" size="50" value=""></dd><br/>
        <b>Дата окончания:</b>
        <dd><input name="${type}_endDateNew" type="date" size="50" value=""></dd>
        <br/><b>Описание:</b><br/>
        <textarea name="${type}_descriptionNew" cols="70"></textarea>
        </dl>
        --%>
        <dd><input name="${type}_name" type="text" size="50" value=""><br/></dd><br/>
        <b>URL:</b>
        <dd><input name="${type}_urlNew" type="text" size="50" value=""><br/></dd>
        <br/><br/><b>Должность:</b>
        <dd><input name="${type}_titleNew" type="text" size="50" value=""><br/></dd>
        <br/><b>Дата начала:</b>
        <dd><input name="${type}_startDateNew" type="date" size="50" value=""></dd><br/>
        <b>Дата окончания:</b>
        <dd><input name="${type}_endDateNew" type="date" size="50" value=""></dd>
        <br/><b>Описание:</b><br/>
        <textarea name="${type}_descriptionNew" cols="70"></textarea>


        <hr>
        <button type="submit">Сохранить</button>
        <button onclick="window.history.back()">Отменить</button>
    </form>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
