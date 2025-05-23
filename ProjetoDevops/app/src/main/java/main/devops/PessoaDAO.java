package main.devops;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PessoaDAO {

    private static final String URL = "jdbc:mysql://db:3306/devopsdb";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    public PessoaDAO() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    //metodo para pegar a lista de pessoas
    public List<Pessoa> listar() {
        List<Pessoa> pessoas = new ArrayList<>();
        String sql = "SELECT * FROM pessoa";
        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Pessoa p = new Pessoa(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getInt("idade"),
                        rs.getString("cpf"),
                        rs.getString("telefone"),
                        rs.getString("email")
                );
                pessoas.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pessoas;
    }
    //metodo para buscar a pessoa especifica no banco
    public Pessoa buscarPorId(int id) {
        String sql = "SELECT * FROM pessoa WHERE id = ?";
        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try(ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Pessoa(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getInt("idade"),
                            rs.getString("cpf"),
                            rs.getString("telefone"),
                            rs.getString("email")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    //metodo para adicionar no banco
    public boolean adicionar(Pessoa p) {
        String sql = "INSERT INTO pessoa (nome, idade, cpf, telefone, email) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getNome());
            ps.setInt(2, p.getIdade());
            ps.setString(3, p.getCpf());
            ps.setString(4, p.getTelefone());
            ps.setString(5, p.getEmail());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    //metodo para remover do banco
    public boolean remover(int id) {
        String sql = "DELETE FROM pessoa WHERE id = ?";
        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            int affected = ps.executeUpdate();
            return affected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //metodo para editar a pessoa
    public boolean atualizar(Pessoa p) {
        String sql = "UPDATE pessoa SET nome = ?, idade = ?, cpf = ?, telefone = ?, email = ? WHERE id = ?";
        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getNome());
            ps.setInt(2, p.getIdade());
            ps.setString(3, p.getCpf());
            ps.setString(4, p.getTelefone());
            ps.setString(5, p.getEmail());
            ps.setInt(6, p.getId());
            int affected = ps.executeUpdate();
            return affected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
