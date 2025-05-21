<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="main.devops.PessoaDAO, main.devops.Pessoa" %>
<%
    String idParam = request.getParameter("id");
    Pessoa p = null;
    if (idParam != null) {
        int id = Integer.parseInt(idParam);
        PessoaDAO dao = new PessoaDAO();
        p = dao.buscarPorId(id);
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Detalhes da Pessoa</title>
</head>
<body>
<% if (p != null) { %>
    <h1>Detalhes de <%= p.getNome() %></h1>
    <p><strong>Idade:</strong> <%= p.getIdade() %></p>
    <p><strong>CPF:</strong> <%= p.getCpf() %></p>
    <p><strong>Telefone:</strong> <%= p.getTelefone() %></p>
    <p><strong>Email:</strong> <%= p.getEmail() %></p>
<% } else { %>
    <p>Pessoa não encontrada.</p>
<% } %>
<br>
<a href="listar">Voltar à lista</a>
</body>
</html>
