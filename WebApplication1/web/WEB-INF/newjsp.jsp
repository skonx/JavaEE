<%-- 
    Document   : newjsp
    Created on : Sep 19, 2016, 2:31:42 PM
    Author     : jsie
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>My JSP #2</title>
    </head>
    <body>
        <div>
            <h1>Hello World!</h1>
            <p>This page is accessed through a servlet : <c:out value="${requestScope.myBean.svname}"/></p>
    </div>
    <div>
        <a href="index.html">Back to index.html</a>
    </div>
</body>
</html>
