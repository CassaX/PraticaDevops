<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <title>Logs do Sistema</title>
</head>
<body>
<h1>Logs do Sistema</h1>
<ul>
    <%
        List<String> logs = (List<String>) request.getAttribute("logs");
        for (String log : logs) {
    %>
    <li><%= log %></li>
    <% } %>
</ul>
<br>
<a href="http://localhost:8080/">Voltar ao menu principal</a>
</body>
</html>
