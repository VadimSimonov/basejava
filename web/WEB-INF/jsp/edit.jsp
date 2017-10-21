<%@ page import="ru.javawebinar.basejava.model.*" %>
<%@ page import="ru.javawebinar.basejava.util.DateUtil" %>
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
    <form method="post" action="resume?action=edit" enctype="application/x-www-form-urlencoded">
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
                    <c:choose>
                        <c:when test="${type.name()=='EXPERIENCE'}">
                            <a href="resume?uuid=${resume.uuid}&action=add&form=EXPERIENCE"><img src="img/pencil.png"></a>
                        </c:when>
                        <c:when test="${type.name()=='EDUCATION'}">
                            <a href="resume?uuid=${resume.uuid}&action=add&form=EDUCATION"><img src="img/pencil.png"></a>
                        </c:when>
                    </c:choose>

                    <c:forEach items="<%=((OrganizationSection) section).getOrganizations()%>" var="organizations" varStatus="loop">
                        <h4>Название:</h4>
                        ${organizations.setId()}
                        <c:set var="index" value="${loop.index+100}" />
                        <c:set var="index" value="${index + 1}"/>

                        <dd><input name="${type}" type="text" size="70" value="${organizations.homePage.name}"><br/></dd>
                        <h4>URL:</h4>
                        <dd><input name="${type}_url" type="text" size="70" value="${organizations.homePage.url}"><br/></dd>
                        <c:forEach items="${organizations.positions}" var="positions" varStatus="isindex">

                            <c:set var="ind" value="${isindex.index+100}" />
                            <c:set var="ind" value="${ind + 1}" />

                            <br/><br/><b>Должность:</b>
                            <dd><input name="${type}_title_${ind}" type="text" size="70" value="${positions.title}"><br/></dd>
                            <br/><b>Дата начала:</b>
                            <fmt:parseDate value="${positions.startDate}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                            <fmt:formatDate value="${parsedDate}" var="stdDatum" type="date" pattern="yyyy/MM" />
                                <dd><input name="${type}_startDate" type="date" size="30" value="${positions.startDate}"></dd><br/>
                            <b>Дата окончания:</b>
                            <c:set var="date" value="<%=DateUtil.NOW%>">
                            </c:set>
                            <c:if test="${date.equals(positions.endDate)}">
                                <dd><input name="${type}_endDate" type="date" size="30" value=""></dd>
                            </c:if>
                            <c:if test="${!date.equals(positions.endDate)}">
                                <fmt:parseDate value="${positions.endDate}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
                                <fmt:formatDate value="${parsedDate}" var="stdDatum" type="date" pattern="yyyy/MM" />
                                <dd><input name="${type}_endDate" type="date" size="30" value="${positions.endDate}"></dd>
                            </c:if>
                            <br/><b>Описание:</b><br/>
                            <textarea name="${type}_description" cols="70">${positions.description}</textarea>
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

