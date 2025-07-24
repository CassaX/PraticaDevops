package main.devops;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class LogDAO {

    private static final String HOST = System.getenv("DB_HOST");
    private static final String PORT = System.getenv("DB_PORT");
    private static final String DATABASE = System.getenv("DB_NAME");
    private static final String USER = System.getenv("DB_USER");
    private static final String PASSWORD = System.getenv("DB_PASSWORD");

    private static final String URL = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE;


    public LogDAO() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public List<Log> listar() {
        List<Log> logs = new ArrayList<>();
        String sql = "SELECT data, mensagem FROM logs ORDER BY data DESC";
        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Timestamp data = rs.getTimestamp("data");
                String mensagem = rs.getString("mensagem");
                logs.add(new Log(data, mensagem));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return logs;
    }

    public void registrarLog(String mensagem) {
        String sql = "INSERT INTO logs (mensagem) VALUES (?)";
        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, mensagem);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
