package main.devops;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "LoggerServlet", value = {"/logs", "/registrar-log"})
public class LoggerServlet extends HttpServlet {
    private LogDAO dao = new LogDAO();

    // GET  Listar logs
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        List<Log> logs = dao.listar();
        req.setAttribute("logs", logs);
        req.getRequestDispatcher("/logs.jsp").forward(req, resp);
    }
    // POST Registrar novo log
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String pessoaId = req.getParameter("pessoaId");
        String nome = req.getParameter("nome");
        String acao = req.getParameter("acao");

        if (pessoaId != null && nome != null && acao != null) {
            String mensagem = "Pessoa ID: " + pessoaId + ", Nome: " + nome + ", Ação: " + acao;
            dao.registrarLog(mensagem);
            resp.setStatus(HttpServletResponse.SC_OK);
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parâmetros de log incompletos");
        }
    }
}