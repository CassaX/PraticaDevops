package main.devops;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;

@WebServlet("/listar")
public class ListarPessoasServlet extends HttpServlet {
    private PessoaDAO dao = new PessoaDAO();

    @Override
    //faz o direcionamento para a pagina que possui a lista de pessoas
    protected void doGet(HttpServletRequest req, jakarta.servlet.http.HttpServletResponse resp) throws ServletException, IOException {
        List<Pessoa> pessoas = dao.listar();
        req.setAttribute("pessoas", pessoas);
        req.getRequestDispatcher("listar.jsp").forward(req, resp);
    }
}