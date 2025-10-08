<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<jsp:include page="header.jsp"/>

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

<table class="table table-bordered text-center align-middle">
    <thead>
    <tr class="table-secondary">
        <th scope="col" width="100px;">Image</th>
        <th scope="col">
            <div class="d-flex justify-content-between align-items-center">
                <span>Brand</span>
                <div>
                    <a href="<c:url value="productList">
                    <c:param name="page" value="1"/>
                    <c:param name="query" value="${param.query}"/>
                    <c:param name="sortField" value="brand"/>
                    <c:param name="sortOrder" value="asc"/>
                    </c:url> ">
                        <i class="bi bi-sort-alpha-up ${param.sortField == 'brand' && param.sortOrder == 'asc' ? 'fw-bold text-danger' : ''}"></i></a>

                    <a href="<c:url value="productList">
                    <c:param name="page" value="1"/>
                    <c:param name="query" value="${param.query}"/>
                    <c:param name="sortField" value="brand"/>
                    <c:param name="sortOrder" value="desc"/>
                    </c:url> ">
                        <i class="bi bi-sort-alpha-down ${param.sortField == 'brand' && param.sortOrder == 'desc' ? 'fw-bold text-danger' : ''}"></i></a>
                </div>
            </div>
        </th>
        <th scope="col">
            <div class="d-flex justify-content-between align-items-center">
                <span>Model</span>
                <div>
                    <a href="<c:url value="productList">
                    <c:param name="page" value="1"/>
                    <c:param name="query" value="${param.query}"/>
                    <c:param name="sortField" value="model"/>
                    <c:param name="sortOrder" value="asc"/>
                    </c:url> ">
                        <i class="bi bi-sort-alpha-up ${param.sortField == 'model' && param.sortOrder == 'asc' ? 'fw-bold text-danger' : ''}"></i></a>

                    <a href="<c:url value="productList">
                    <c:param name="page" value="1"/>
                    <c:param name="query" value="${param.query}"/>
                    <c:param name="sortField" value="model"/>
                    <c:param name="sortOrder" value="desc"/>
                    </c:url> ">
                        <i class="bi bi-sort-alpha-down ${param.sortField == 'model' && param.sortOrder == 'desc' ? 'fw-bold text-danger' : ''}"></i></a>
                </div>
            </div>
        </th>
        <th scope="col">Color</th>
        <th scope="col">
            <div class="d-flex justify-content-between align-items-center">
                <span>Display size</span>
                <div>
                    <a href="<c:url value="productList">
                    <c:param name="page" value="1"/>
                    <c:param name="query" value="${param.query}"/>
                    <c:param name="sortField" value="displaySizeInches"/>
                    <c:param name="sortOrder" value="asc"/>
                    </c:url> ">
                        <i class="bi bi-sort-numeric-up ${param.sortField == 'displaySizeInches' && param.sortOrder == 'asc' ? 'fw-bold text-danger' : ''}"></i></a>

                    <a href="<c:url value="productList">
                    <c:param name="page" value="1"/>
                    <c:param name="query" value="${param.query}"/>
                    <c:param name="sortField" value="displaySizeInches"/>
                    <c:param name="sortOrder" value="desc"/>
                    </c:url> ">
                        <i class="bi bi-sort-numeric-down ${param.sortField == 'displaySizeInches' && param.sortOrder == 'desc' ? 'fw-bold text-danger' : ''}"></i></a>
                </div>
            </div>
        </th>
        <th scope="col">
            <div class="d-flex justify-content-between align-items-center">
                <span>Price</span>
                <div>
                    <a href="<c:url value="productList">
                    <c:param name="page" value="1"/>
                    <c:param name="query" value="${param.query}"/>
                    <c:param name="sortField" value="price"/>
                    <c:param name="sortOrder" value="asc"/>
                    </c:url> ">
                        <i class="bi bi-sort-numeric-up ${param.sortField == 'price' && param.sortOrder == 'asc' ? 'fw-bold text-danger' : ''}"></i></a>

                    <a href="<c:url value="productList">
                    <c:param name="page" value="1"/>
                    <c:param name="query" value="${param.query}"/>
                    <c:param name="sortField" value="price"/>
                    <c:param name="sortOrder" value="desc"/>
                    </c:url> ">
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
            <td>${phone.brand}</td>
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
    $(document).ready(function(){
        $('button[phone-id]').click(function(){
            var row = $(this).closest('tr');
            var phoneId = $(this).attr('phone-id');
            var quantity = row.find('input[name="quantity"]').val();

            $.ajax({
                url : '${pageContext.request.contextPath}/ajaxCart',
                type : 'POST',
                data : {
                    phoneId : phoneId,
                    quantity : quantity
                },
                success : function(){
                    location.reload();
                }
            });
        });
    });
</script>

<nav aria-label="Page navigation example">
    <ul class="pagination justify-content-end">
        <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
            <a class="page-link"
               href="<c:url value="productList">
                <c:param name="page" value="${currentPage-1}"/>
                <c:param name="query" value="${param.query}"/>
                <c:param name="sortField" value="${param.sortField}"/>
                <c:param name="sortOrder" value="${param.sortOrder}"/>
            </c:url>"
               aria-label="Previous">
                <span aria-hidden="true">&laquo;</span>
            </a>


            <c:if test="${currentPage == 1}">
        <li class="page-item active"><a class="page-link"
                                        href="<c:url value="productList">
                <c:param name="page" value="${currentPage}"/>
                <c:param name="query" value="${param.query}"/>
                <c:param name="sortField" value="${param.sortField}"/>
                <c:param name="sortOrder" value="${param.sortOrder}"/>
            </c:url>">
                ${currentPage}</a></li>

        <c:if test="${totalPages >= 2}">
            <li class="page-item"><a class="page-link"
                                     href="<c:url value="productList">
                <c:param name="page" value="${currentPage+1}"/>
                <c:param name="query" value="${param.query}"/>
                <c:param name="sortField" value="${param.sortField}"/>
                <c:param name="sortOrder" value="${param.sortOrder}"/>
            </c:url>">
                    ${currentPage+1}</a></li>
        </c:if>

        <c:if test="${totalPages >= 3}">
            <li class="page-item"><a class="page-link"
                                     href="<c:url value="productList">
                <c:param name="page" value="${currentPage+2}"/>
                <c:param name="query" value="${param.query}"/>
                <c:param name="sortField" value="${param.sortField}"/>
                <c:param name="sortOrder" value="${param.sortOrder}"/>
            </c:url>">
                    ${currentPage+2}</a></li>
        </c:if>
        </c:if>

        <c:if test="${currentPage == totalPages && totalPages > 1}">
            <c:if test="${totalPages >= 3}">
                <li class="page-item"><a class="page-link"
                                         href="<c:url value="productList">
                <c:param name="page" value="${currentPage-2}"/>
                <c:param name="query" value="${param.query}"/>
                <c:param name="sortField" value="${param.sortField}"/>
                <c:param name="sortOrder" value="${param.sortOrder}"/>
            </c:url>">
                        ${currentPage-2}</a></li>
            </c:if>

            <c:if test="${totalPages >= 2}">
                <li class="page-item"><a class="page-link"
                                         href="<c:url value="productList">
                <c:param name="page" value="${currentPage-1}"/>
                <c:param name="query" value="${param.query}"/>
                <c:param name="sortField" value="${param.sortField}"/>
                <c:param name="sortOrder" value="${param.sortOrder}"/>
            </c:url>">
                        ${currentPage-1}</a></li>
            </c:if>

            <li class="page-item active"><a class="page-link"
                                            href="<c:url value="productList">
                <c:param name="page" value="${currentPage}"/>
                <c:param name="query" value="${param.query}"/>
                <c:param name="sortField" value="${param.sortField}"/>
                <c:param name="sortOrder" value="${param.sortOrder}"/>
            </c:url>">
                    ${currentPage}</a></li>
        </c:if>

        <c:if test="${currentPage > 1 && currentPage < totalPages}">
            <li class="page-item"><a class="page-link"
                                     href="<c:url value="productList">
                <c:param name="page" value="${currentPage-1}"/>
                <c:param name="query" value="${param.query}"/>
                <c:param name="sortField" value="${param.sortField}"/>
                <c:param name="sortOrder" value="${param.sortOrder}"/>
            </c:url>">
                    ${currentPage-1}</a></li>
            <li class="page-item active"><a class="page-link"
                                            href="<c:url value="productList">
                <c:param name="page" value="${currentPage}"/>
                <c:param name="query" value="${param.query}"/>
                <c:param name="sortField" value="${param.sortField}"/>
                <c:param name="sortOrder" value="${param.sortOrder}"/>
            </c:url>">
                    ${currentPage}</a></li>
            <li class="page-item"><a class="page-link"
                                     href="<c:url value="productList">
                <c:param name="page" value="${currentPage+1}"/>
                <c:param name="query" value="${param.query}"/>
                <c:param name="sortField" value="${param.sortField}"/>
                <c:param name="sortOrder" value="${param.sortOrder}"/>
            </c:url>">
                    ${currentPage+1}</a></li>
        </c:if>

        <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
            <a class="page-link"
               href="<c:url value="productList">
                <c:param name="page" value="${currentPage+1}"/>
                <c:param name="query" value="${param.query}"/>
                <c:param name="sortField" value="${param.sortField}"/>
                <c:param name="sortOrder" value="${param.sortOrder}"/>
            </c:url>"
               aria-label="Next">
                <span aria-hidden="true">&raquo;</span>
            </a>
        </li>
    </ul>
</nav>


</p>