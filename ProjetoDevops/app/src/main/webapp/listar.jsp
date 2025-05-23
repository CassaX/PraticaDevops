<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List, main.devops.Pessoa" %>
<!DOCTYPE html>
<html>
<head>
    <title>Lista de Pessoas</title>
    <link rel="stylesheet" href="css/styles.css">
</head>
<body>
    <h1>Lista de Pessoas</h1>
    <table>
        <tr>
            <th>Nome</th>
            <th>Idade</th>
            <th>CPF</th>
            <th>Telefone</th>
            <th>Email</th>
            <th>Ações</th>
        </tr>
        <% List<Pessoa> pessoas = (List<Pessoa>) request.getAttribute("pessoas");
           for (Pessoa p : pessoas) { %>
        <tr>
            <td><%= p.getNome() %></td>
            <td><%= p.getIdade() %></td>
            <td><%= p.getCpf() %></td>
            <td><%= p.getTelefone() %></td>
            <td><%= p.getEmail() %></td>
            <td>
                <a href="editar?id=<%= p.getId() %>" class="btn">Editar</a>
                <a href="remover?id=<%= p.getId() %>" class="btn btn-danger">Remover</a>
            </td>
        </tr>
        <% } %>
    </table>
    <div class="btn-container">
        <a href="addPessoa" class="btn">Adicionar Pessoa</a>
        <a href="/" class="btn">Voltar ao Menu</a>
    </div>
</body>
</html>