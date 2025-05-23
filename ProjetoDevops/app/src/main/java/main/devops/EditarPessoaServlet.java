package main.devops;

import java.io.IOException;
import java.net.URLEncoder;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;

@WebServlet("/editar")
public class EditarPessoaServlet extends HttpServlet {
    private PessoaDAO dao = new PessoaDAO();
    
    private void registrarLog(int pessoaId, String nome, String acao) throws IOException {
        String url = "http://logger-service:8080/registrar-log?" +
                    "pessoaId=" + URLEncoder.encode(String.valueOf(pessoaId), "UTF-8") +
                    "&nome=" + URLEncoder.encode(nome, "UTF-8") +
                    "&acao=" + URLEncoder.encode(acao, "UTF-8");
        
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(url);
            httpClient.execute(httpPost);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, jakarta.servlet.http.HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");
        if (idParam != null) {
            int id = Integer.parseInt(idParam);
            Pessoa p = dao.buscarPorId(id);
            if (p != null) {
                req.setAttribute("pessoa", p);
                req.getRequestDispatcher("editarPessoa.jsp").forward(req, resp);
                return;
            }
        }
        resp.sendRedirect("listar");
    }

    @Override
    protected void doPost(HttpServletRequest req, jakarta.servlet.http.HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        String nome = req.getParameter("nome");
        int idade = Integer.parseInt(req.getParameter("idade"));
        String cpf = req.getParameter("cpf");
        String telefone = req.getParameter("telefone");
        String email = req.getParameter("email");

        Pessoa p = new Pessoa(id, nome, idade, cpf, telefone, email);
        boolean sucesso = dao.atualizar(p);

        if (sucesso) {
            registrarLog(p.getId(), p.getNome(), "Registro atualizado");
            resp.sendRedirect("listar");
        } else {
            req.setAttribute("erro", "Erro ao atualizar pessoa.");
            req.setAttribute("pessoa", p);
            req.getRequestDispatcher("editarPessoa.jsp").forward(req, resp);
        }
    }
}