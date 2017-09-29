<%@ page import="ru.javawebinar.basejava.model.*" %>
<%@ page import="ru.javawebinar.basejava.util.DateUtil" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
    <form method="post" action="resume" enctype="application/x-www-form-urlencoded">
        <input type="hidden" name="uuid" value="${resume.uuid}">
        <dl>
            <dt>Имя:</dt>
            <dd><input type="text" name="fullName" size=50 value="${resume.fullName}"></dd>
        </dl>

        <h3>Контакты:</h3>
        <c:forEach var="type" items="<%=ContactType.values()%>">
            <dl>
                <dt>${type.title}</dt>
                <dd><input type="text" name="${type.name()}" size=30 value="${resume.getContact(type)}"></dd>
            </dl>
        </c:forEach>

        <h3>Секции:</h3>
        <c:forEach var="SectionType" items="${resume.sections}">
            <jsp:useBean id="SectionType" type="java.util.Map.Entry<ru.javawebinar.basejava.model.SectionType, ru.javawebinar.basejava.model.Section>"/>
            <c:set var="type" value="${SectionType.key}"/>
            <c:set var="section" value="${SectionType.value}"/>
            <jsp:useBean id="section" type="ru.javawebinar.basejava.model.Section"/>
            <h3>${type.title}</h3>
            <c:choose>
                <c:when test="${type.name()=='OBJECTIVE' || type.name()=='PERSONAL' }">
                    <dd><input type="text" name="${type.name()}" size=60 value="<%= ((TextSection) section).getContent()%>"></dd>
                </c:when>
                <c:when test="${type.name()=='ACHIEVEMENT' || type.name()=='QUALIFICATIONS' }">
                    <textarea name="${type}" cols="70"><%=String.join("\n",((ListSection) section).getItems())%></textarea>
                </c:when>
                <c:when test="${type.name()=='EXPERIENCE' || type.name()=='EDUCATION' }">
                    <c:forEach items="<%=((OrganizationSection) section).getOrganizations()%>" var="organizations">
                        <h4>URL:</h4>
                            <dd><input name="${type}" type="text" size="70" value="${organizations.homePage.url}"><br/></dd>
                        <h4>Название:</h4>
                            <dd><input name="${type}" type="text" size="70" value="${organizations.homePage.name}"><br/></dd>
                        <c:forEach items="${organizations.positions}" var="positions">
                            <br/><br/><b>Должность:</b>
                            <b>${positions.title}<br/></b>
                            <b>Дата начала:</b>
                                <dd><input type="date" size="30" value="${positions.startDate}"></dd><br/>
                            <b>Дата окончания:</b>
                            <c:set var="date" value="<%=DateUtil.NOW%>"></c:set>
                            <c:if test="${date.equals(positions.endDate)}">
                                <dd><input type="date" size="30" value=""></dd>
                            </c:if>
                            <c:if test="${!date.equals(positions.endDate)}">
                                <dd><input type="date" size="30" value="${positions.endDate}"></dd>
                            </c:if>
                            <br/><b>Описание:</b><br/>
                            <textarea name="${type}" cols="70">${positions.description}</textarea>
                        </c:forEach>
                    </c:forEach>
                </c:when>

                </c:choose>

            </dl>
        </c:forEach>
        <hr>
        <button type="submit">Сохранить</button>
        <button onclick="window.history.back()">Отменить</button>
    </form>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>

