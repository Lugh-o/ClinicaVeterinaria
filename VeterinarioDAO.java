package dao;

import model.Veterinario;
import utils.DatabaseHandler;

import java.sql.*;
import java.util.ArrayList;

public class VeterinarioDAO implements IDAO<Veterinario> {

    String sql;

    @Override
    public ArrayList<Veterinario> getAll() {
        sql = """
                SELECT v.id, v.nome, v.crmv,
                       v.id_especialidade, e.nome AS especialidade,
                       v.id_telefone, t.numero AS telefone
                FROM veterinario v
                INNER JOIN especialidade e ON e.id = v.id_especialidade
                INNER JOIN telefone t ON t.id = v.id_telefone;
              """;

        ArrayList<Veterinario> veterinarios = new ArrayList<>();

        try (Connection connection = DatabaseHandler.getConnection();
             PreparedStatement stm = connection.prepareStatement(sql);
             ResultSet rs = stm.executeQuery()) {

            while (rs.next()) {
                Veterinario v = new Veterinario(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("crmv"),
                        rs.getInt("id_especialidade"),
                        rs.getString("especialidade"),
                        rs.getInt("id_telefone"),
                        rs.getString("telefone")
                );
                veterinarios.add(v);
            }

            return veterinarios;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Veterinario getById(int id) {
        sql = """
                SELECT v.id, v.nome, v.crmv,
                       v.id_especialidade, e.nome AS especialidade,
                       v.id_telefone, t.numero AS telefone
                FROM veterinario v
                INNER JOIN especialidade e ON e.id = v.id_especialidade
                INNER JOIN telefone t ON t.id = v.id_telefone
                WHERE v.id = ?;
              """;

        try (Connection connection = DatabaseHandler.getConnection();
             PreparedStatement stm = connection.prepareStatement(sql)) {

            stm.setInt(1, id);
            try (ResultSet rs = stm.executeQuery()) {
                if (!rs.next()) return null;

                return new Veterinario(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("crmv"),
                        rs.getInt("id_especialidade"),
                        rs.getString("especialidade"),
                        rs.getInt("id_telefone"),
                        rs.getString("telefone")
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Veterinario create(Veterinario veterinario) {
        sql = "SELECT crmv FROM veterinario WHERE crmv = ?";

        try (Connection connection = DatabaseHandler.getConnection()) {

            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setString(1, veterinario.getCrmv());
            ResultSet rs = stm.executeQuery();

            if (rs.next()) throw new SQLException("CRMV já cadastrado!");

            sql = "INSERT INTO telefone (numero) VALUES (?)";
            stm = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            stm.setString(1, veterinario.getTelefone());
            stm.executeUpdate();

            rs = stm.getGeneratedKeys();
            if (rs.next()) veterinario.setIdTelefone(rs.getInt("id"));

            sql = "INSERT INTO veterinario (nome, crmv, id_especialidade, id_telefone) VALUES (?, ?, ?, ?)";
            stm = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            stm.setString(1, veterinario.getNome());
            stm.setString(2, veterinario.getCrmv());
            stm.setInt(3, veterinario.getIdEspecialidade());
            stm.setInt(4, veterinario.getIdTelefone());
            stm.executeUpdate();

            rs = stm.getGeneratedKeys();
            if (rs.next()) veterinario.setId(rs.getInt("id"));

            return veterinario;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Veterinario update(int id, Veterinario veterinario) {
        if (getById(id) == null) return null;

        sql = """
                UPDATE veterinario
                SET nome = ?, crmv = ?, id_especialidade = ?
                WHERE id = ?;
              """;

        try (Connection connection = DatabaseHandler.getConnection()) {

            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setString(1, veterinario.getNome());
            stm.setString(2, veterinario.getCrmv());
            stm.setInt(3, veterinario.getIdEspecialidade());
            stm.setInt(4, id);
            stm.executeUpdate();

            sql = "UPDATE telefone SET numero = ? WHERE id = ?";
            stm = connection.prepareStatement(sql);
            stm.setString(1, veterinario.getTelefone());
            stm.setInt(2, veterinario.getIdTelefone());
            stm.executeUpdate();

            return getById(id);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(int id) {
        sql = "DELETE FROM veterinario WHERE id = ?";

        try (Connection connection = DatabaseHandler.getConnection();
             PreparedStatement stm = connection.prepareStatement(sql)) {

            stm.setInt(1, id);
            stm.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
