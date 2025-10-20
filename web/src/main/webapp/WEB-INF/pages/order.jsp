<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<jsp:include page="header.jsp"/>

<div style="margin-top: 20px;"></div>

<p class="fw-bold text-primary fs-3 d-flex align-items-center">
    Cart
</p>


<div style="display: flex; justify-content: space-between; align-items: center;">
    <a href="/phoneshop-web/cart" class="btn btn-primary">
        <i class="bi bi-arrow-left"></i> Back to cart
    </a>
</div>

<spring:hasBindErrors name="orderForm">
    <c:if test="${not empty errors.globalErrors}">
        <div class="alert alert-danger">
            <c:forEach var="error" items="${errors.globalErrors}">
                <div>${error.defaultMessage}</div>
            </c:forEach>
    </div>
    </c:if>
</spring:hasBindErrors>

<div style="margin-top: 20px;"></div>

<c:choose>
    <c:when test="${not empty order.orderItems && order.orderItems.size() > 0}">
        <table class="table table-bordered text-center align-middle">
            <thead>
            <tr class="table-secondary">
                <th scope="col">
                    <div class="d-flex justify-content-between align-items-center">
                        <span>Brand</span>
                    </div>
                </th>

                <th scope="col">
                    <div class="d-flex justify-content-between align-items-center">
                        <span>Model</span>
                    </div>
                </th>

                <th scope="col">
                    Color
                </th>

                <th scope="col">
                    <div class="d-flex justify-content-between align-items-center">
                        <span>Display size</span>
                    </div>
                </th>

                <th scope="col">
                    <div class="d-flex justify-content-between align-items-center">
                        <span>Quantity</span>
                    </div>
                </th>

                <th scope="col">
                    <div class="d-flex justify-content-between align-items-center">
                        <span>Price</span>
                    </div>
                </th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="orderItem" items="${order.orderItems}" varStatus="i">
                <tr>
                    <td>
                            ${orderItem.phone.brand}
                    </td>
                    <td>${orderItem.phone.model}</td>
                    <td>
                        <c:forEach var="color" items="${orderItem.phone.colors}">
                            ${color.code}
                        </c:forEach>
                    </td>
                    <td>
                            ${orderItem.phone.displaySizeInches}"
                    </td>
                    <td>
                            ${orderItem.quantity}
                    </td>
                    <td>${orderItem.phone.price}</td>
                </tr>
            </c:forEach>
            <tr style="border: none !important;">
                <td colspan="4" class="borderless"></td>

                <td class="font-weight-bold text-primary text-truncate">
                    Subtotal
                </td>
                <td class="font-weight-bold text-primary text-truncate">
                        ${order.subtotal}
                </td>
            </tr>

            <tr style="border: none !important;">
                <td colspan="4" class="borderless"></td>

                <td class="font-weight-bold text-primary text-truncate">
                    Delivery
                </td>
                <td class="font-weight-bold text-primary text-truncate">
                        ${order.deliveryPrice}
                </td>
            </tr>

            <tr style="border: none !important;">
                <td colspan="4" class="borderless"></td>
                <td class="font-weight-bold text-primary text-truncate">
                    TOTAL
                </td>
                <td class="font-weight-bold text-primary text-truncate">
                        ${order.totalPrice}
                </td>
            </tr>
            </tbody>
        </table>

        <form:form method="POST" modelAttribute="orderForm">

            <div class="mb-3 row">
                <label class="col-form-label">First name*</label>
                <div class="col-sm-2">
                    <form:input path="firstName" cssClass="form-control"/>
                    <form:errors path="firstName" cssClass="text-danger"/>
                </div>
            </div>

            <div class="mb-3 row">
                <label class="col-form-label">Last name*</label>
                <div class="col-sm-2">
                    <form:input path="lastName" cssClass="form-control"/>
                    <form:errors path="lastName" cssClass="text-danger"/>
                </div>
            </div>

            <div class="mb-3 row">
                <label class="col-form-label">Address*</label>
                <div class="col-sm-2">
                    <form:input path="deliveryAddress" cssClass="form-control"/>
                    <form:errors path="deliveryAddress" cssClass="text-danger"/>
                </div>
            </div>

            <div class="mb-3 row">
                <label class="col-form-label">Phone*</label>
                <div class="col-sm-2">
                    <form:input path="contactPhoneNo" cssClass="form-control"/>
                    <form:errors path="contactPhoneNo" cssClass="text-danger"/>
                </div>
            </div>

            <div class="mb-3 row">
                <div class="col-sm-2">
                    <form:textarea path="additionalInfo" cssClass="form-control" placeholder="Additional info"
                                   rows="6"/>
                    <form:errors path="additionalInfo" cssClass="text-danger"/>
                </div>
            </div>

            <button class="btn btn-primary" type="submit">
                Place order
            </button>

        </form:form>

    </c:when>

    <c:otherwise>
        <h1>Cart is empty. You can't make an order</h1>
    </c:otherwise>
</c:choose>