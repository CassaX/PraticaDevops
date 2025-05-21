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

@WebServlet("/remover")
public class RemoverPessoaServlet extends HttpServlet {
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
            if (dao.remover(id)) {
                registrarLog(p.getId(), p.getNome(), "Registro removido");
            }
        }
        resp.sendRedirect("listar");
    }
}
