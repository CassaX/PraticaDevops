<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Adicionar Pessoa</title>
    <link rel="stylesheet" href="../css/styles.css">
</head>
<body>
    <h1>Adicionar Nova Pessoa</h1>
    <form action="addPessoa" method="post">
        <label>Nome:</label>
        <input type="text" name="nome" required>
        
        <label>Idade:</label>
        <input type="number" name="idade" required>
        
        <label>CPF:</label>
        <input type="text" name="cpf" required>
        
        <label>Telefone:</label>
        <input type="text" name="telefone" required>
        
        <label>Email:</label>
        <input type="email" name="email" required>
        
        <div class="btn-container">
            <input type="submit" value="Adicionar Pessoa" class="btn">
            <a href="listar" class="btn btn-danger">Cancelar</a>
        </div>
    </form>
</body>
</html>