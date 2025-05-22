<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8" />
    <title>Logs do Sistema</title>
    <link rel="stylesheet" href="../css/styles.css" />
</head>
<body>
    <h1>Logs do Sistema</h1>

    <table>
        <thead>
            <tr>
                <th>Data/Hora</th>
                <th>Mensagem</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${logs}" var="log">
                <tr>
                    <td><c:out value="${log.data}"/></td>
                    <td><c:out value="${log.mensagem}"/></td>
                </tr>
            </c:forEach>
        </tbody>
    </table>

    <div class="btn-container">
        <button class="btn" onclick="window.location.reload();">Atualizar</button>
        <a href="/" class="btn">Voltar ao menu principal</a>
    </div>

</body>
</html>
