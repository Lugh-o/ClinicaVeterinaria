package dao;

import model.Proprietario;
import utils.DatabaseHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProprietarioDAO implements IDAO<Proprietario> {
    String sql;

    @Override
    public ArrayList<Proprietario> getAll() {
        try (Connection connection = DatabaseHandler.getConnection()) {
            String sql = "SELECT prop.id, prop.nome, prop.cpf, prop.email, prop.id_telefone, tel.numero " + "FROM proprietario prop " + "INNER JOIN telefone tel ON tel.id = prop.id_telefone";
            ArrayList<Proprietario> proprietarios = new ArrayList<>();
            Proprietario novoProp;

            PreparedStatement stm = connection.prepareStatement(sql);
            ResultSet result = stm.executeQuery();

            while (result.next()) {
                novoProp = new Proprietario(
                        result.getInt("id"),
                        result.getInt("id_telefone"),
                        result.getString("nome"),
                        result.getString("cpf"),
                        result.getString("email"),
                        result.getString("numero"));
                proprietarios.add(novoProp);
            }

            return proprietarios;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Proprietario getById(int id) {
        try (Connection connection = DatabaseHandler.getConnection()) {
            sql = "SELECT prop.id, prop.nome, prop.cpf, prop.email, prop.id_telefone, tel.numero" + " FROM proprietario prop" + " INNER JOIN telefone tel ON tel.id = prop.id_telefone" + " WHERE prop.id = ?";

            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, id);
            ResultSet result = stm.executeQuery();

            if (!result.next()) return null;
            return new Proprietario(
                    result.getInt("id"),
                    result.getInt("id_telefone"),
                    result.getString("nome"),
                    result.getString("cpf"),
                    result.getString("email"),
                    result.getString("numero")
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Proprietario create(Proprietario proprietario) {

        try (Connection connection = DatabaseHandler.getConnection()) {

            sql = "SELECT cpf FROM proprietario WHERE cpf = ?";
            PreparedStatement stm = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            stm.setString(1, proprietario.getCpf());
            ResultSet result = stm.executeQuery();

            if (result.next()) {
                throw new SQLException("CPF já cadastrado!");
            }

            sql = "INSERT INTO telefone (numero) VALUES (?)";
            stm = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            stm.setString(1, proprietario.getTelefone());
            stm.executeUpdate();

            result = stm.getGeneratedKeys();
            if (result.next()) {
                proprietario.setId_telefone(result.getInt("id"));
            }

            sql = "INSERT INTO proprietario(nome, cpf, email, id_telefone) VALUES (?, ?, ?, ?)";
            stm = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            stm.setString(1, proprietario.getNome());
            stm.setString(2, proprietario.getCpf());
            stm.setString(3, proprietario.getEmail());
            stm.setInt(4, proprietario.getId_telefone());
            stm.executeUpdate();

            result = stm.getGeneratedKeys();

            if (result.next()) {
                proprietario.setId(result.getInt("id"));
            }

            return proprietario;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Proprietario update(int id, Proprietario proprietario) {

        if (getById(id) == null) {
            return null;
        }

        try (Connection connection = DatabaseHandler.getConnection()) {
            sql = "UPDATE proprietario" + " SET nome = ?, cpf = ?, email = ?" + " WHERE id = ?";

            PreparedStatement stm = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            stm.setString(1, proprietario.getNome());
            stm.setString(2, proprietario.getCpf());
            stm.setString(3, proprietario.getEmail());
            stm.setInt(4, id);
            stm.executeUpdate();

            ResultSet result = stm.getGeneratedKeys();

            sql = "UPDATE telefone" + " SET numero = ?" + " WHERE id = ?";

            stm = connection.prepareStatement(sql);
            stm.setString(1, proprietario.getTelefone());
            if (result.next()) stm.setInt(2, result.getInt("id_telefone"));

            stm.executeUpdate();

            return getById(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(int id) {
        try (Connection connection = DatabaseHandler.getConnection()) {
            sql = "DELETE FROM proprietario WHERE id = ?";

            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, id);
            stm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
