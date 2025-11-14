package dao;

import model.Animal;
import utils.DatabaseHandler;

import java.sql.*;
import java.util.ArrayList;

public class AnimalDAO implements IDAO<Animal> {
    @Override
    public ArrayList<Animal> getAll() {
        String sql = """
                    SELECT
                        a.id, a.nome, a.especie, a.raca, a.data_nascimento, a.peso
                    FROM animal a;
                """;
        ArrayList<Animal> animais = new ArrayList<>();

        try (Connection connection = DatabaseHandler.getConnection(); PreparedStatement statement = connection.prepareStatement(sql); ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Animal a = new Animal(resultSet.getInt("id"), resultSet.getString("nome"), resultSet.getString("especie"), resultSet.getString("raca"), resultSet.getDate("data_nascimento").toLocalDate(), resultSet.getDouble("peso"));
                animais.add(a);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return animais;
    }

    @Override
    public Animal getById(int id) {
        String sql = """
                    SELECT
                        a.id, a.nome, a.especie, a.raca, a.data_nascimento, a.peso
                    FROM animal a
                    WHERE a.id = ?;
                """;

        try (Connection connection = DatabaseHandler.getConnection(); PreparedStatement statement = connection.prepareStatement(sql);) {
            statement.setObject(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) return null;
                return new Animal(resultSet.getInt("id"), resultSet.getString("nome"), resultSet.getString("especie"), resultSet.getString("raca"), resultSet.getDate("data_nascimento").toLocalDate(), resultSet.getDouble("peso"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Animal create(Animal animal) {
        String sql = """
                    INSERT INTO animal (nome, especie, raca, data_nascimento, peso, id_proprietario) VALUES
                    (?, ?, ?, ?, ?, ?);
                """;

        try (Connection connection = DatabaseHandler.getConnection(); PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, animal.getNome());
            statement.setString(2, animal.getEspecie());
            statement.setString(3, animal.getRaca());
            statement.setDate(4, Date.valueOf(animal.getDataNascimento()));
            statement.setDouble(5, animal.getPeso());
            statement.setInt(6, animal.getIdProprietario());
            statement.executeUpdate();

            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    animal.setId(resultSet.getInt("id"));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return animal;
    }

    @Override
    public Animal update(int id, Animal animal) {
        String sql = """
                    UPDATE animal SET nome = ?, especie = ?, raca = ?, data_nascimento = ?, peso = ?, id_proprietario = ?)
                    WHERE id = ?;
                """;
        try (Connection connection = DatabaseHandler.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, animal.getNome());
            statement.setString(2, animal.getEspecie());
            statement.setString(3, animal.getRaca());
            statement.setDate(4, Date.valueOf(animal.getDataNascimento()));
            statement.setDouble(5, animal.getPeso());
            statement.setInt(6, animal.getIdProprietario());
            statement.setInt(7, id);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return getById(id);
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM animal WHERE id = ?";
        try (Connection connection = DatabaseHandler.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
