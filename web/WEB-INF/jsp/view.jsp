<%@ page import="ru.javawebinar.basejava.model.TextSection" %>
<%@ page import="ru.javawebinar.basejava.model.ListSection" %>
<%@ page import="ru.javawebinar.basejava.util.DateUtil" %>
<%@ page import="java.util.List" %>
<%@ page import="ru.javawebinar.basejava.model.OrganizationSection" %>
<%@ page import="java.time.Month" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
    <h2>${resume.fullName}&nbsp;<a href="resume?uuid=${resume.uuid}&action=edit"><img src="img/pencil.png"></a></h2>
    <p>
        <c:forEach var="contactEntry" items="${resume.contacts}">
            <jsp:useBean id="contactEntry" type="java.util.Map.Entry<ru.javawebinar.basejava.model.ContactType, java.lang.String>"/>
                <%=contactEntry.getKey().toHtml(contactEntry.getValue())%><br/>
        </c:forEach>
    <hr>

    <c:forEach var="SectionType" items="${resume.sections}">
        <jsp:useBean id="SectionType" type="java.util.Map.Entry<ru.javawebinar.basejava.model.SectionType, ru.javawebinar.basejava.model.Section>"/>
        <c:set var="type" value="${SectionType.key}"/>
        <c:set var="section" value="${SectionType.value}"/>
        <jsp:useBean id="section" type="ru.javawebinar.basejava.model.Section"/>
        <h3>${type.title}</h3>
            <c:choose>
                <c:when test="${type.name()=='OBJECTIVE' || type.name()=='PERSONAL' }">
                    <%= ((TextSection) section).getContent()%>
                </c:when>
                <c:when test="${type.name()=='ACHIEVEMENT' || type.name()=='QUALIFICATIONS' }">
                    <c:forEach items="<%=((ListSection) section).getItems()%>" var="organizations">
                        ${organizations}<br/>
                    </c:forEach>
                </c:when>
                <c:when test="${type.name()=='EXPERIENCE' || type.name()=='EDUCATION' }">
                    <c:forEach items="<%=((OrganizationSection) section).getOrganizations()%>" var="organizations">
                    <a href="${organizations.homePage.url}">${organizations.homePage.name}</a><br/>
                        <c:forEach items="${organizations.positions}" var="positions">
                            ${positions.startDate}
                            <c:set var="date" value="<%=DateUtil.NOW%>"></c:set>
                            <c:if test="${date.equals(positions.endDate)}">
                            <c:out value="По настоящее время"></c:out>
                            </c:if>
                            <c:if test="${!date.equals(positions.endDate)}">
                                <c:out value="${positions.endDate}"></c:out>
                            </c:if>

                            <b>${positions.title}<br/></b>
                            ${positions.description}<br/>
                        </c:forEach>
                    </c:forEach>
                </c:when>
            </c:choose>
    </c:forEach>
    <p>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>

