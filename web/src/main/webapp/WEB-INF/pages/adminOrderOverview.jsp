<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<head>
    <jsp:include page="headerWithoutMiniCart.jsp"/>
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
</head>

<div style="margin-top: 20px;"></div>

<p class="fw-bold text-primary fs-3 d-flex align-items-center">
    Cart
</p>

<div style="margin-top: 20px;"></div>

<div style="display: flex; justify-content: space-between; align-items: center;">
    <h3 class="mb-3">Order number: ${order.id}</h3>


    <h3 class="mb-3">Order status: ${order.getStatusAsString()}</h3>
</div>

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
            <td>${orderItem.phone.displaySizeInches}"</td>
            <td>${orderItem.quantity}</td>
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

<div class="mb-3 row">
    <label class="col-form-label">First name* ${order.firstName}</label>

</div>

<div class="mb-3 row">
    <label class="col-form-label">Last name* ${order.lastName}</label>
</div>

<div class="mb-3 row">
    <label class="col-form-label">Address* ${order.deliveryAddress}</label>
</div>

<div class="mb-3 row">
    <label class="col-form-label">Phone* ${order.contactPhoneNo}</label>
</div>

<div class="mb-3 row">
    <label class="col-form-label">${order.additionalInfo}</label>
</div>


<c:choose>
    <c:when test="${order.getStatusAsString().equals('NEW')}">
        <div style="display: flex; justify-content: space-around; align-items: center;">
            <a href="/phoneshop-web/admin/orders" class="btn btn-primary">
                <i class="bi bi-arrow-left"></i> Back
            </a>

            <button type="button" class="btn btn-success" onclick="updateStatus('DELIVERED')">
                Delivered
            </button>

            <button type="button" class="btn btn-danger" onclick="updateStatus('REJECTED')">
                Rejected
            </button>

            <script>
                const token =  document.querySelector('meta[name="_csrf"]').getAttribute('content');
                const header = document.querySelector('[name="_csrf_header"]').getAttribute('content');

                function updateStatus(status) {
                    fetch('/phoneshop-web/admin/orders/${order.id}/status', {
                        method: 'PUT',
                        headers: {
                            'Content-Type': 'application/json',
                            [header]: token
                        },
                        body: JSON.stringify(status)
                    })
                        .then(response => {
                            if (response.redirected) {
                                window.location.href = response.url;
                            } else if (response.ok) {
                                location.reload();
                            }
                        });
                }
            </script>
        </div>
    </c:when>

    <c:otherwise>
        <a href="/phoneshop-web/admin/orders" class="btn btn-primary">
            <i class="bi bi-arrow-left"></i> Back
        </a>
    </c:otherwise>
</c:choose>
