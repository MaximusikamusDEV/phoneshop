<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/WEB-INF/pages/header.jsp"/>
<html>
<head>
    <title>Error</title>
</head>
<body>
<div class="container-fluid vh-100 d-flex justify-content-center align-items-center">
    <h1 class="text-danger fw-bold">
        <c:choose>
            <c:when test="${not empty message}">
                <c:out value="Error 404: ${message}"/>
            </c:when>
            <c:otherwise>
                ERROR 404: NOT FOUND
            </c:otherwise>
        </c:choose></h1>
</div>
</body>
</html>

