<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<jsp:include page="header.jsp"/>

<div style="margin-top: 20px;"></div>

<p class="fw-bold text-primary fs-3 d-flex align-items-center">
    Cart
</p>


<div style="display: flex; justify-content: space-between; align-items: center;">
    <a href="/phoneshop-web/productList" class="btn btn-primary">
        <i class="bi bi-arrow-left"></i> Back to product list
    </a>


    <a href="" class="btn btn-primary">
        Order page
        <i class="bi bi-arrow-right"></i>
    </a>
</div>

<div style="margin-top: 20px;"></div>

<c:choose>
    <c:when test="${not empty cart.cartItems && cart.cartItems.size() > 0}">
        <form:form method="post" modelAttribute="cartForm" action="/phoneshop-web/cart/updateCart">
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
                            <span>Price</span>
                        </div>
                    </th>

                    <th scope="col">
                        Quantity
                    </th>

                    <th scope="col" style="width: 1%; height: 1%; white-space: nowrap;">
                        Action
                    </th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="cartItem" items="${cart.cartItems}" varStatus="i">
                    <tr>
                        <td>
                                ${cartItem.phone.brand}
                        </td>
                        <td>${cartItem.phone.model}</td>
                        <td>
                            <c:forEach var="color" items="${cartItem.phone.colors}">
                                ${color.code}
                            </c:forEach>
                        </td>
                        <td>${cartItem.phone.displaySizeInches}"</td>
                        <td>${cartItem.phone.price}</td>
                        <td>
                            <form:input path="items[${i.index}].quantity" cssClass="form-control"/>
                            <form:errors path="items[${i.index}].quantity" cssClass="text-danger"/>
                            <form:hidden path="items[${i.index}].phoneId"/>
                        </td>

                        <td class="text-center align-middle" style="width: 1%; height: 1%; white-space: nowrap;">
                            <button type="button" class="btn btn-primary delete-btn"
                                    data-phone-id="${cartItem.phone.id}">
                                Delete
                            </button>
                        </td>

                    </tr>
                </c:forEach>
                </tbody>
            </table>

            <div style="display: flex; justify-content: flex-end; align-items: center; gap: 10px; margin-top: 10px; margin-bottom: 10px;">

                <button type="submit" name="updateCart" value="update" class="btn btn-primary me-4">
                    Update
                </button>

            </div>

        </form:form>

        <form id="deleteForm" method="post">
            <input type="hidden" name="_method" value="DELETE"/>
        </form>

        <script>
            document.querySelectorAll('.delete-btn').forEach(
                button => {
                    button.addEventListener('click', function () {
                        const phoneId = this.getAttribute('data-phone-id');
                        const form = document.getElementById('deleteForm');
                        form.action = `/phoneshop-web/cart/delete/` + phoneId;
                        form.submit();
                    });
                });
        </script>

        <div style="display: flex; justify-content: flex-end; align-items: center; gap: 10px; margin-top: 10px; margin-bottom: 10px; margin-right: 33px;">
            <form method="get" action="/phoneshop-web/order" style="display: flex; justify-content: end; align-items: end;">
                <button class="btn btn-primary" type="submit">
                    Order
                </button>
            </form>
        </div>

    </c:when>

    <c:otherwise>
        <h1>Cart is empty</h1>
    </c:otherwise>
</c:choose>