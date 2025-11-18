<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
          rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
          crossorigin="anonymous">

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-FKyoEForCGlyvwx9Hj09JcYn3nv7wiPVlz7YYwJrWVcXK/BmnVDxM+D2scQbITxI"
            crossorigin="anonymous"></script>

    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.13.1/font/bootstrap-icons.min.css">
</head>

<nav class="navbar navbar-expand-lg bg-body-tertiary">
    <div class="container-fluid">

        <a class="navbar-brand fw-bold text-primary fs-3 d-flex align-items-center" href="/phoneshop-web/productList">
            <i class="bi bi-phone-fill me-2"></i> PHONIFY
        </a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent"
                aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <ul class="nav flex-row nav justify-content-end">
            <sec:authorize access="!isAuthenticated()">
                <ul class="nav flex-column nav justify-content-end me-3">
                    <li class="nav-item">
                        <p><a href="/phoneshop-web/authentication/login"
                              class="link-primary link-offset-2 link-underline-opacity-25 link-underline-opacity-100-hover">Login</a>
                        </p>
                    </li>
                </ul>
            </sec:authorize>

            <sec:authorize access="isAuthenticated()">
                <ul class="nav flex-column nav justify-content-end me-3">
                    <li class="nav-item">
                        <p>
                            <sec:authentication property="principal.username"/>
                        </p>
                    </li>
                </ul>

                <sec:authorize access="hasRole('ROLE_ADMIN')">
                    <ul class="nav flex-column nav justify-content-end me-3">
                        <li class="nav-item">
                            <p><a href="/phoneshop-web/admin/orders"
                                  class="link-primary link-offset-2 link-underline-opacity-25 link-underline-opacity-100-hover">Admin</a>
                            </p>
                        </li>
                    </ul>
                </sec:authorize>

                <ul class="nav flex-column nav justify-content-end me-3">
                    <li class="nav-item">
                        <form action="/phoneshop-web/authentication/logout" method="post">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                            <button type="submit" class=
                                    "btn btn-link p-0 m-0 align-baseline
                                     link-primary link-offset-2 link-underline-opacity-25 link-underline-opacity-100-hover">
                                Logout
                            </button>
                        </form>
                    </li>
                </ul>
            </sec:authorize>

            <a href="/phoneshop-web/bulkCart" id="bulkCart" type="button" class="btn btn-primary btn-lg me-3">Bulk cart</a>

        </ul>
    </div>
</nav>

<div class="bg-dark" style="height: 3px; width: 100%;"></div>

</html>
