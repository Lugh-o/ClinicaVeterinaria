package dao;

import model.Especialidade;
import utils.DatabaseHandler;

import java.sql.*;
import java.util.ArrayList;

public class EspecialidadeDAO implements IDAO<Especialidade> {

    @Override
    public ArrayList<Especialidade> getAll() {
        String sql = "SELECT id, nome FROM especialidade ORDER BY id";
        ArrayList<Especialidade> lista = new ArrayList<>();

        try (Connection conn = DatabaseHandler.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(new Especialidade(rs.getInt("id"), rs.getString("nome")));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return lista;
    }

    @Override
    public Especialidade getById(int id) {
        String sql = "SELECT id, nome FROM especialidade WHERE id = ?";

        try (Connection conn = DatabaseHandler.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {

                if (!rs.next()) return null;

                return new Especialidade(rs.getInt("id"), rs.getString("nome"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Especialidade create(Especialidade esp) {
        String sql = "INSERT INTO especialidade (nome) VALUES (?)";

        try (Connection conn = DatabaseHandler.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, esp.getNome());
            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    esp.setId(keys.getInt(1));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return esp;
    }

    @Override
    public Especialidade update(int id, Especialidade esp) {
        String sql = "UPDATE especialidade SET nome = ? WHERE id = ?";

        try (Connection conn = DatabaseHandler.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, esp.getNome());
            stmt.setInt(2, id);

            int rows = stmt.executeUpdate();

            if (rows == 0) return null;
            return getById(id);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM especialidade WHERE id = ?";

        try (Connection conn = DatabaseHandler.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
