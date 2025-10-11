<%@ tag pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="page" required="true" type="java.lang.String" %>
<%@ attribute name="sortField" required="false" type="java.lang.String" %>
<%@ attribute name="sortOrder" required="false" type="java.lang.String" %>

<c:url value="productList">
    <c:param name="page" value="${page}"/>
    <c:if test="${not empty param.query}">
        <c:param name="query" value="${param.query}"/>
    </c:if>
    <c:if test="${not empty sortField}">
        <c:param name="sortField" value="${sortField}"/>
    </c:if>
    <c:if test="${empty sortField and not empty param.sortField}">
        <c:param name="sortField" value="${param.sortField}"/>
    </c:if>
    <c:if test="${not empty sortOrder}">
        <c:param name="sortOrder" value="${sortOrder}"/>
    </c:if>
    <c:if test="${empty sortOrder and not empty param.sortOrder}">
        <c:param name="sortOrder" value="${param.sortOrder}"/>
    </c:if>
</c:url>
