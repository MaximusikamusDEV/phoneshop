<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<jsp:include page="headerWithoutMiniCart.jsp"/>


<div style="margin-top: 20px;"></div>

<div class="container mt-4">
    <div class="row justify-content-center">
        <div class="col-md-6 col-lg-4">

            <div class="text-center mb-4">
                <p class="fw-bold text-primary fs-3 d-flex align-items-center">
                    Enter your credentials
                </p>
            </div>

            <div style="margin-top: 20px;"></div>

            <form action="/phoneshop-web/authentication/login" method="post">
                <div class="row mb-3">
                    <label for="username" class="col-sm-2 col-form-label">Username</label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" id="username" name="username" placeholder="Username">
                    </div>
                </div>
                <div class="row mb-3">
                    <label for="inputPassword" class="col-sm-2 col-form-label">Password</label>
                    <div class="col-sm-10">
                        <input type="password" class="form-control" id="inputPassword" name="password"
                               placeholder="Password">
                    </div>
                </div>

                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <button type="submit" class="btn btn-primary">Log in</button>
            </form>

            <c:if test="${not empty(error)}">
                <h1 class="text-danger">${error}</h1>
            </c:if>
        </div>
    </div>
</div>

