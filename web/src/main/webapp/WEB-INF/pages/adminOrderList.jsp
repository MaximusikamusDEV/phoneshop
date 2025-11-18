<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="headerWithoutMiniCart.jsp"/>

<div style="margin-top: 20px;"></div>

<h3 class="mb-3">Orders</h3>

<div style="margin-top: 20px;"></div>

<c:choose>
    <c:when test="${not empty orderList && orderList.size() > 0}">
<table class="table table-bordered text-center align-middle">
    <thead>
    <tr class="table-secondary">
        <th scope="col">
            Order number
        </th>

        <th scope="col">
            Customer
        </th>

        <th scope="col">
            Phone
        </th>

        <th scope="col">
            Address
        </th>

        <th scope="col">
            Date
        </th>

        <th scope="col">
            Total price
        </th>

        <th scope="col" style="width: 1%; height: 1%; white-space: nowrap;">
            Status
        </th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="order" items="${orderList}" varStatus="i">
        <tr>
            <td>
                <a href="/phoneshop-web/admin/orders/${order.id}">
                        ${order.id}
                </a>
            </td>
            <td>
                    ${order.firstName} ${order.lastName}
            </td>

            <td>
                    ${order.contactPhoneNo}
            </td>

            <td>
                    ${order.deliveryAddress}
            </td>

            <td>
                    <fmt:parseDate value="${order.createdAt}" pattern="yyyy-MM-dd'T'HH:mm:ss" var="parsedDateTiem"/>
                    <fmt:formatDate value="${parsedDateTiem}" type="both" dateStyle="medium" timeStyle="medium"/>
            </td>

            <td>
                    ${order.totalPrice}
            </td>

            <td>
                    ${order.status}
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
    </c:when>

    <c:otherwise>
        <h1>Orders page is empty</h1>
    </c:otherwise>
</c:choose>