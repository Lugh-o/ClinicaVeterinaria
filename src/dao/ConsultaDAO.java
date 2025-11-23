package dao;

import model.Animal;
import model.Consulta;
import model.Veterinario;
import utils.DatabaseHandler;

import java.sql.*;
import java.util.ArrayList;

public class ConsultaDAO implements IDAO<Consulta> {

    @Override
    public ArrayList<Consulta> getAll() {

        String sql = """
                SELECT
                   c.id, c.horario, c.diagnostico, c.valor,
                   a.id as id_animal, a.nome as nome_animal, a.especie, a.raca, a.data_nascimento, a.peso,
                   v.id as id_vet, v.nome as nome_vet
                FROM consultas c
                INNER JOIN animal a ON c.id_animal = a.id
                INNER JOIN veterinarios v ON c.id_veterinario = v.id
                """;

        ArrayList<Consulta> consultas = new ArrayList<>();

        try (Connection connection = DatabaseHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Animal animal = new Animal(
                        resultSet.getInt("id_animal"),
                        resultSet.getString("nome_animal"),
                        resultSet.getString("especie"),
                        resultSet.getString("raca"),
                        resultSet.getDate("data_nascimento").toLocalDate(),
                        resultSet.getDouble("peso")
                );


                Veterinario vet = new Veterinario();
                vet.setId(resultSet.getInt("id_vet"));
                vet.setNome(resultSet.getString("nome_vet"));


                Consulta c = new Consulta(
                        resultSet.getInt("id"),
                        resultSet.getTimestamp("horario").toLocalDateTime(), // Converte SQL Timestamp para LocalDateTime
                        animal,
                        vet,
                        resultSet.getString("diagnostico"),
                        resultSet.getDouble("valor")
                );

                consultas.add(c);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return consultas;
    }

    @Override
    public Consulta getById(int id) {
        String sql = """
                SELECT
                   c.id, c.horario, c.diagnostico, c.valor,
                   a.id as id_animal, a.nome as nome_animal, a.especie, a.raca, a.data_nascimento, a.peso,
                   v.id as id_vet, v.nome as nome_vet
                FROM consultas c
                INNER JOIN animal a ON c.id_animal = a.id
                INNER JOIN veterinarios v ON c.id_veterinario = v.id
                WHERE c.id = ?
                """;

        try (Connection connection = DatabaseHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) return null;


                Animal animal = new Animal(
                        resultSet.getInt("id_animal"),
                        resultSet.getString("nome_animal"),
                        resultSet.getString("especie"),
                        resultSet.getString("raca"),
                        resultSet.getDate("data_nascimento").toLocalDate(),
                        resultSet.getDouble("peso")
                );


                Veterinario vet = new Veterinario();
                vet.setId(resultSet.getInt("id_vet"));
                vet.setNome(resultSet.getString("nome_vet"));

                // 3. Consulta (Retornando o objeto completo)
                return new Consulta(
                        resultSet.getInt("id"),
                        resultSet.getTimestamp("horario").toLocalDateTime(),
                        animal,
                        vet,
                        resultSet.getString("diagnostico"),
                        resultSet.getDouble("valor")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Consulta create(Consulta consulta) {
        String sql = """
                INSERT INTO consultas (horario, diagnostico, valor, id_animal, id_veterinario)
                VALUES (?, ?, ?, ?, ?);
                """;

        try (Connection connection = DatabaseHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {


            statement.setTimestamp(1, Timestamp.valueOf(consulta.getHorarioConsulta()));
            statement.setString(2, consulta.getDiagnostico());
            statement.setDouble(3, consulta.getValor());


            statement.setInt(4, consulta.getAnimal().getId());
            statement.setInt(5, consulta.getVeterinario().getId());

            statement.executeUpdate();

            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    consulta.setId(resultSet.getInt(1));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return consulta;
    }

    @Override
    public Consulta update(int id, Consulta consulta) {
        String sql = """
                UPDATE consultas
                SET horario = ?, diagnostico = ?, valor = ?, id_animal = ?, id_veterinario = ?
                WHERE id = ?;
                """;

        try (Connection connection = DatabaseHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setTimestamp(1, Timestamp.valueOf(consulta.getHorarioConsulta()));
            statement.setString(2, consulta.getDiagnostico());
            statement.setDouble(3, consulta.getValor());
            statement.setInt(4, consulta.getAnimal().getId());
            statement.setInt(5, consulta.getVeterinario().getId());
            statement.setInt(6, id);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return getById(id);
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM consultas WHERE id = ?";

        try (Connection connection = DatabaseHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}