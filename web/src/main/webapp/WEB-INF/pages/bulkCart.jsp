<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<jsp:include page="headerWithMiniCart.jsp"/>

<div style="margin-top: 20px;"></div>

<p class="fw-bold text-primary fs-3 d-flex align-items-center">
    BulkCart
</p>


<div style="display: flex; justify-content: space-between; align-items: center;">
    <a href="/phoneshop-web/productList" class="btn btn-primary">
        <i class="bi bi-arrow-left"></i> Back to product list
    </a>

</div>

<div style="margin-top: 20px;"></div>


<form:form method="post" modelAttribute="bulkCartForm" action="/phoneshop-web/bulkCart/addToCart">
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
                Quantity
            </th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="i" begin="0" end="9" varStatus="status">
            <tr>
                <td>
                    <form:input path="items[${status.index}].phoneBrand" cssClass="form-control"/>
                    <form:errors path="items[${status.index}].phoneBrand" cssClass="text-danger"/>
                </td>
                <td>
                    <form:input path="items[${status.index}].phoneModel" cssClass="form-control"/>
                    <form:errors path="items[${status.index}].phoneModel" cssClass="text-danger"/>
                </td>
                <td>
                    <form:input path="items[${status.index}].quantity" cssClass="form-control"/>
                    <form:errors path="items[${status.index}].quantity" cssClass="text-danger"/>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <div style="display: flex; justify-content: flex-end; align-items: center; gap: 10px; margin-top: 10px; margin-bottom: 10px;">

        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <button type="submit" name="updateCart" value="update" class="btn btn-primary me-4">
            Update
        </button>

    </div>

</form:form>