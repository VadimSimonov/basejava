<%@ page import="ru.javawebinar.basejava.storage.Storage" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <jsp:useBean id="resume" type="ru.javawebinar.basejava.model.Resume" scope="request"/>
    <jsp:useBean id="storage" type="ru.javawebinar.basejava.storage.Storage" scope="request"/>
    <title>Резюме ${resume.fullName}</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<section>
    <form method="post" action="resume" enctype="application/x-www-form-urlencoded">
        <input type="hidden" name="uuid" value="${resume.uuid}">
        <input type="hidden" name="fullName" value="${resume.fullName}">
        <c:set
        <%=
        storage.get(resume.getFullName()).getContacts()
        %>
        <input type="hidden" name="fullName" value="${storage.get(resume.fullName)}">
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
        <dd><input name="${type}" type="text" size="50" value=""><br/></dd><br/>
        <b>URL:</b>
        <dd><input name="${type}_url" type="text" size="50" value=""><br/></dd>
        <br/><br/><b>Должность:</b>
        <dd><input name="${type}_title" type="text" size="50" value=""><br/></dd>
        <br/><b>Дата начала:</b>
        <dd><input name="${type}_startDate" type="date" size="50" value=""></dd><br/>
        <b>Дата окончания:</b>
        <dd><input name="${type}_endDate" type="date" size="50" value=""></dd>
        <br/><b>Описание:</b><br/>
        <textarea name="${type}_description" cols="70"></textarea>
        </dl>
        <hr>
        <button type="submit">Сохранить</button>
        <button onclick="window.history.back()">Отменить</button>
    </form>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
