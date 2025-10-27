<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<jsp:include page="header.jsp"/>

<div style="margin-top: 20px;"></div>

<a href="/phoneshop-web/productList" class="btn btn-primary">
    <i class="bi bi-arrow-left"></i> Back to product list
</a>

<div style="margin-top: 20px;"></div>

<div class="row">

    <div class="col-md-5">
        <div class="card">
            <div class="card-body text-center">
                <h4>${phone.model}</h4>
                <img style="max-width:350px; height: auto"
                     src="https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/${phone.imageUrl}">
                <p class="text-start text-break">${phone.description}</p>

                <div class="border border-primary rounded p-3">
                    <h5 class="text-primary">Price: ${phone.price}</h5>
                    <div class="input-container d-flex justify-content-center align-items-center">
                        <label class="me-4">
                            <input type="text" class="form-control" name="quantity" value="1">
                        </label>
                        <button type="button" class="btn btn-primary md-3" phone-id="${phone.id}">Add to cart</button>
                    </div>
                    <div class="error-placeholder" style="height: 20px;"></div>
                </div>
            </div>
        </div>
    </div>


    <div class="col-md-5">
        <h3 class="mb-4">Specifications</h3>

        <div class="mb-4">
            <h5 class="fw-semibold mb-2">Display</h5>
            <table class="table table-bordered table-striped">
                <tbody>
                <t:tableRowProductDetails name="Size" value="${phone.displaySizeInches}"/>
                <t:tableRowProductDetails name="Resolution" value="${phone.displayResolution}"/>
                <t:tableRowProductDetails name="Technology" value="${phone.displayTechnology}"/>
                <t:tableRowProductDetails name="Pixel Density" value="${phone.displaySizeInches}"/>
                </tbody>
            </table>
        </div>

        <div class="mb-4">
            <h5 class="fw-semibold mb-2">Dimensions and weight</h5>
            <table class="table table-bordered table-striped">
                <tbody>
                <t:tableRowProductDetails name="Length" value="${phone.lengthMm} mm"/>
                <t:tableRowProductDetails name="Width" value="${phone.widthMm} mm"/>
                <t:tableRowProductDetails name="Height" value="${phone.heightMm} mm"/>
                <t:tableRowProductDetails name="Weight" value="${phone.weightGr} gr"/>
                </tbody>
            </table>
        </div>

        <div class="mb-4">
            <h5 class="fw-semibold mb-2">Camera</h5>
            <table class="table table-bordered table-striped">
                <tbody>
                <t:tableRowProductDetails name="Front" value="${phone.frontCameraMegapixels} megapixels"/>
                <t:tableRowProductDetails name="Back" value="${phone.backCameraMegapixels} megapixels"/>
                </tbody>
            </table>
        </div>

        <div class="mb-4">
            <h5 class="fw-semibold mb-2">Display</h5>
            <table class="table table-bordered table-striped">
                <tbody>
                <t:tableRowProductDetails name="Brand" value="${phone.brand}"/>
                <t:tableRowProductDetails name="Anounce date" value="${phone.announced}"/>

                <tr>
                    <th scope="row" class="border rounded text-uppercase bg-light">
                        Colors
                    </th>
                    <td>
                        <c:forEach var="color" items="${phone.colors}">
                            ${color.code}
                        </c:forEach>
                    </td>
                </tr>

                <t:tableRowProductDetails name="Device type" value="${phone.deviceType}"/>
                <t:tableRowProductDetails name="Operationing system" value="${phone.os}"/>
                <t:tableRowProductDetails name="RAM size" value="${phone.ramGb} Gb"/>
                <t:tableRowProductDetails name="Internal storage size" value="${phone.internalStorageGb} Gb"/>
                <t:tableRowProductDetails name="Battery capacity" value="${phone.batteryCapacityMah} MAH"/>
                <t:tableRowProductDetails name="Talk time" value="${phone.talkTimeHours}"/>
                <t:tableRowProductDetails name="Stand by time" value="${phone.standByTimeHours}"/>
                <t:tableRowProductDetails name="Positioning" value="${phone.positioning}"/>
                <t:tableRowProductDetails name="Bluetooth" value="${phone.bluetooth}"/>
                </tbody>
            </table>
        </div>
    </div>
</div>

<script>
    $(document).ready(function () {
        $('button[phone-id]').click(function () {
            var container = $(this).closest('.border');
            var quantityInput = container.find('input[name="quantity"]');
            var phoneId = $(this).attr('phone-id');
            var quantity = quantityInput.val();

            $('.error-placeholder').empty();

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
                        showError(quantityInput, response.message);
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
                    showError(quantityInput, message);
                }
            });

            function showError(quantityInput, message) {
                quantityInput.addClass('is-invalid');
                quantityInput.val('1');
                var container = quantityInput.closest('.border');
                var placeholder = container.find('.error-placeholder');
                placeholder.html('<div class="text-danger mt-1">' + message + '</div>');
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
