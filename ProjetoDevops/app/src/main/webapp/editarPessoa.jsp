<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="main.devops.Pessoa" %>
<%
    Pessoa p = (Pessoa) request.getAttribute("pessoa");
    if (p == null) {
        response.sendRedirect("listar");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Editar Pessoa</title>
    <link rel="stylesheet" href="../css/styles.css">
</head>
<body>
    <h1>Editar Pessoa</h1>
    <form action="editar" method="post">
        <input type="hidden" name="id" value="<%= p.getId() %>">
        
        <label>Nome:</label>
        <input type="text" name="nome" value="<%= p.getNome() %>" required>
        
        <label>Idade:</label>
        <input type="number" name="idade" value="<%= p.getIdade() %>" required>
        
        <label>CPF:</label>
        <input type="text" name="cpf" value="<%= p.getCpf() %>" required>
        
        <label>Telefone:</label>
        <input type="text" name="telefone" value="<%= p.getTelefone() %>" required>
        
        <label>Email:</label>
        <input type="email" name="email" value="<%= p.getEmail() %>" required>
        
        <div class="btn-container">
            <input type="submit" value="Salvar Alterações" class="btn">
            <a href="listar" class="btn btn-danger">Cancelar</a>
        </div>
    </form>
</body>
</html>