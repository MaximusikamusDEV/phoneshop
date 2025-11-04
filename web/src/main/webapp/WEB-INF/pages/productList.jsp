<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<head>
    <jsp:include page="headerWithMiniCart.jsp"/>
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
</head>

<div style="margin-top: 20px;"></div>
<table style="width: 100%;">
    <td>
        <a class="navbar-brand fw-bold text-primary fs-3 d-flex align-items-center" href="productList">
            Phones
        </a>
    </td>

    <td style="width: 500px;">
        <form method="get" action="productList" class="d-flex" role="search">
            <input class="form-control me-2" name="query" type="search" placeholder="Search" aria-label="Search"
                   value='${param.query}'/>
            <input type="hidden" name="page" value="1"/>
            <button class="btn btn-outline-success" type="submit">Search</button>
        </form>
    </td>
</table>

<c:choose>
    <c:when test="${not empty phones and phones.size() > 0}">
        <table class="table table-bordered text-center align-middle">
            <thead>
            <tr class="table-secondary">
                <th scope="col" width="100px;">Image</th>
                <th scope="col">
                    <div class="d-flex justify-content-between align-items-center">
                        <span>Brand</span>
                        <div>
                            <a href="<t:generateUrl page="1" sortField="brand" sortOrder="asc"/>">
                                <i class="bi bi-sort-alpha-up ${param.sortField == 'brand' && param.sortOrder == 'asc' ? 'fw-bold text-danger' : ''}"></i></a>

                            <a href="<t:generateUrl page="1" sortField="brand" sortOrder="desc"/>">
                                <i class="bi bi-sort-alpha-down ${param.sortField == 'brand' && param.sortOrder == 'desc' ? 'fw-bold text-danger' : ''}"></i></a>
                        </div>
                    </div>
                </th>
                <th scope="col">
                    <div class="d-flex justify-content-between align-items-center">
                        <span>Model</span>
                        <div>
                            <a href="<t:generateUrl page="1" sortField="model" sortOrder="asc"/>">
                                <i class="bi bi-sort-alpha-up ${param.sortField == 'model' && param.sortOrder == 'asc' ? 'fw-bold text-danger' : ''}"></i></a>

                            <a href="<t:generateUrl page="1" sortField="model" sortOrder="desc"/>">
                                <i class="bi bi-sort-alpha-down ${param.sortField == 'model' && param.sortOrder == 'desc' ? 'fw-bold text-danger' : ''}"></i></a>
                        </div>
                    </div>
                </th>
                <th scope="col">Color</th>
                <th scope="col">
                    <div class="d-flex justify-content-between align-items-center">
                        <span>Display size</span>
                        <div>
                            <a href="<t:generateUrl page="1" sortField="display_Size_Inches" sortOrder="asc"/>">
                                <i class="bi bi-sort-numeric-up ${param.sortField == 'display_Size_Inches' && param.sortOrder == 'asc' ? 'fw-bold text-danger' : ''}"></i></a>

                            <a href="<t:generateUrl page="1" sortField="display_Size_Inches" sortOrder="desc"/>">
                                <i class="bi bi-sort-numeric-down ${param.sortField == 'display_Size_Inches' && param.sortOrder == 'desc' ? 'fw-bold text-danger' : ''}"></i></a>
                        </div>
                    </div>
                </th>
                <th scope="col">
                    <div class="d-flex justify-content-between align-items-center">
                        <span>Price</span>
                        <div>
                            <a href="<t:generateUrl page="1" sortField="price" sortOrder="asc"/>">
                                <i class="bi bi-sort-numeric-up ${param.sortField == 'price' && param.sortOrder == 'asc' ? 'fw-bold text-danger' : ''}"></i></a>

                            <a href="<t:generateUrl page="1" sortField="price" sortOrder="desc"/>">
                                <i class="bi bi-sort-numeric-down ${param.sortField == 'price' && param.sortOrder == 'desc' ? 'fw-bold text-danger' : ''}"></i></a>
                        </div>
                    </div>
                </th>
                <th scope="col">Quantity</th>
                <th scope="col" style="width: 1%; height: 1%; white-space: nowrap;">Action</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="phone" items="${phones}">
                <tr>
                    <td>
                        <img style="width:100px; height: auto"
                             src="https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/${phone.imageUrl}">
                    </td>
                    <td>
                        <a href="productDetails/${phone.id}">${phone.brand}</a>
                    </td>
                    <td>${phone.model}</td>
                    <td>
                        <c:forEach var="color" items="${phone.colors}">
                            ${color.code}
                        </c:forEach>
                    </td>
                    <td>${phone.displaySizeInches}"</td>
                    <td>${phone.price}</td>
                    <td>
                        <input type="text" class="form-control" name="quantity" value="1">
                    </td>
                    <td class="text-center align-middle" style="width: 1%; height: 1%; white-space: nowrap;">
                        <button type="button" class="btn btn-primary" phone-id="${phone.id}">Add to cart</button>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>

        <script>
            $(document).ready(function () {
                $(document).ajaxSend(function (e, xhr, options){
                    var token =  $("meta[name='_csrf']").attr('content');
                    var header = $("[name='_csrf_header']").attr('content');
                    xhr.setRequestHeader(header, token);
                });

                $('button[phone-id]').click(function () {
                    var row = $(this).closest('tr');
                    var phoneId = $(this).attr('phone-id');
                    var quantityInput = row.find('input[name="quantity"]');
                    var quantity = quantityInput.val();


                    row.find('.error-message').remove();

                    $.ajax({
                        url: '${pageContext.request.contextPath}/ajaxCart',
                        type: 'POST',
                        contentType: 'application/json',
                        dataType: 'json',
                        data: JSON.stringify({
                            phoneId: phoneId,
                            quantity: quantity
                        }),
                        success: function (response) {
                            if (response.status === 'success') {
                                updateMiniCart();
                                quantityInput.removeClass('is-invalid');
                                quantityInput.val('1');
                            } else {
                                showError(row, quantityInput, response.message);
                            }
                        },
                        error: function (xhr) {
                            let message = 'Error';
                            try {
                                const err = JSON.parse(xhr.responseText);
                                message = err.message || message;
                            } catch (e) {
                                message = xhr.statusText || message;
                            }
                            showError(row, quantityInput, message);
                        }
                    });

                    function showError(row, quantityInput, message) {
                        quantityInput.addClass('is-invalid');
                        quantityInput.val('1');
                        var $err = $('<div>').addClass('text-danger error-message mt-1').text(message);
                        quantityInput.after($err);
                    }
                });

                function updateMiniCart() {
                    $.ajax({
                        url: '${pageContext.request.contextPath}/ajaxCart/miniCart',
                        type: 'GET',
                        dataType: 'json',
                        success: function (miniCart) {
                            $('#cartButton').text('My cart: ' + miniCart.totalQuantity + ' items, ' + miniCart.totalCost + ' $');
                        },
                        error: function (xhr) {
                            $('#cartButton').text('Error updating mini cart');
                        }
                    })
                }
            });
        </script>

        <nav aria-label="Page navigation example">
            <ul class="pagination justify-content-end">
                <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                    <a class="page-link"
                       href="<t:generateUrl page="${currentPage-1}"/>"
                       aria-label="Previous">
                        <span aria-hidden="true">&laquo;</span>
                    </a>


                    <c:if test="${currentPage == 1}">
                <li class="page-item active"><a class="page-link"
                                                href="<t:generateUrl page="${currentPage}"/>">
                        ${currentPage}</a></li>

                <c:if test="${totalPages >= 2}">
                    <li class="page-item"><a class="page-link"
                                             href="<t:generateUrl page="${currentPage+1}"/>">
                            ${currentPage+1}</a></li>
                </c:if>

                <c:if test="${totalPages >= 3}">
                    <li class="page-item"><a class="page-link"
                                             href="<t:generateUrl page="${currentPage+2}"/>">
                            ${currentPage+2}</a></li>
                </c:if>
                </c:if>

                <c:if test="${currentPage == totalPages && totalPages > 1}">
                    <c:if test="${totalPages >= 3}">
                        <li class="page-item"><a class="page-link"
                                                 href="<t:generateUrl page="${currentPage-2}"/>">
                                ${currentPage-2}</a></li>
                    </c:if>

                    <c:if test="${totalPages >= 2}">
                        <li class="page-item"><a class="page-link"
                                                 href="<t:generateUrl page="${currentPage-1}"/>">
                                ${currentPage-1}</a></li>
                    </c:if>

                    <li class="page-item active"><a class="page-link"
                                                    href="<t:generateUrl page="${currentPage}"/>">
                            ${currentPage}</a></li>
                </c:if>

                <c:if test="${currentPage > 1 && currentPage < totalPages}">
                    <li class="page-item"><a class="page-link"
                                             href="<t:generateUrl page="${currentPage-1}"/>">
                            ${currentPage-1}</a></li>

                    <li class="page-item active"><a class="page-link"
                                                    href="<t:generateUrl page="${currentPage}"/>">
                            ${currentPage}</a></li>

                    <li class="page-item"><a class="page-link"
                                             href="<t:generateUrl page="${currentPage+1}"/>">
                            ${currentPage+1}</a></li>
                </c:if>

                <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                    <a class="page-link"
                       href="<t:generateUrl page="${currentPage+1}"/>"
                       aria-label="Next">
                        <span aria-hidden="true">&raquo;</span>
                    </a>
                </li>
            </ul>
        </nav>

    </c:when>
    <c:otherwise>
        <h1> Products not found </h1>
    </c:otherwise>
</c:choose>