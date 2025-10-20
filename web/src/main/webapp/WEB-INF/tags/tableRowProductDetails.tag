<%@ tag pageEncoding="UTF-8" %>
<%@ attribute name="name" required="true" type="java.lang.String" %>
<%@ attribute name="value" required="false" type="java.lang.String" %>

<tr>
    <th scope="row" class="border rounded text-uppercase bg-light">
        ${name}
    </th>
    <td>${value}</td>
</tr>
