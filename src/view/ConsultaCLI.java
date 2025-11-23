package view;

import dao.AnimalDAO;
import dao.ConsultaDAO;
import dao.VeterinarioDAO;
import model.Animal;
import model.Consulta;
import model.Veterinario;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

import static utils.InputReader.readDouble;
import static utils.InputReader.readInt;

public record ConsultaCLI(Scanner scanner, ConsultaDAO consultaDAO, AnimalDAO animalDAO,
                          VeterinarioDAO veterinarioDAO) {
    public void start() {
        int option;
        do {
            System.out.println("===== MENU CONSULTAS =====");
            System.out.println("1 - Listar consultas");
            System.out.println("2 - Cadastrar consulta");
            System.out.println("3 - Buscar consulta por ID");
            System.out.println("4 - Atualizar consulta");
            System.out.println("5 - Excluir consulta");
            System.out.println("0 - Sair");
            System.out.print("Escolha uma opção: ");

            option = readInt(scanner);

            switch (option) {
                case 1:
                    listarConsultas();
                    break;
                case 2:
                    cadastrarConsulta();
                    break;
                case 3:
                    buscarConsultaPorId();
                    break;
                case 4:
                    atualizarConsulta();
                    break;
                case 5:
                    excluirConsulta();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        } while (option != 0);
    }

    private void listarConsultas() {
        try {
            List<Consulta> lista = consultaDAO.getAll();
            if (lista.isEmpty()) {
                System.out.println("Nenhuma consulta cadastrada.");
                return;
            }
            for (Consulta c : lista) {
                System.out.println(c);
                System.out.println("---------------------");
            }
        } catch (RuntimeException e) {
            System.err.println("Erro ao listar consultas: " + e.getMessage());
        }
    }

    private void cadastrarConsulta() {
        System.out.println("-- Animais disponíveis --");
        try {
            List<Animal> animals = animalDAO.getAll();
            if (animals.isEmpty()) {
                System.out.println("Nenhum animal cadastrado. Não é possível criar a consulta.");
                return;
            }
            for (Animal a : animals) {
                System.out.printf("ID: %d  Nome: %s%n", a.getId(), a.getNome());
            }
        } catch (RuntimeException e) {
            System.err.println("Erro ao listar animais: " + e.getMessage());
            return;
        }

        System.out.print("ID do animal: ");
        int idAnimal = readInt(scanner);
        Animal animal = animalDAO.getById(idAnimal);
        if (animal == null) {
            System.out.println("Animal não encontrado para id=" + idAnimal);
            return;
        }

        System.out.println("-- Veterinários disponíveis --");
        try {
            List<Veterinario> vets = veterinarioDAO.getAll();
            if (vets.isEmpty()) {
                System.out.println("Nenhum veterinário cadastrado. Não é possível criar a consulta.");
                return;
            }
            for (Veterinario v : vets) {
                System.out.printf("ID: %d  Nome: %s%n", v.getId(), v.getNome());
            }
        } catch (RuntimeException e) {
            System.err.println("Erro ao listar veterinários: " + e.getMessage());
            return;
        }

        System.out.print("ID do veterinário: ");
        int idVet = readInt(scanner);
        Veterinario vet = veterinarioDAO.getById(idVet);
        if (vet == null) {
            System.out.println("Veterinário não encontrado para id=" + idVet);
            return;
        }

        scanner.nextLine();
        System.out.print("Data/Hora (formato ISO: YYYY-MM-DDTHH:MM, ex: 2025-12-01T14:30): ");
        String dt = scanner.nextLine().trim();
        LocalDateTime horario;
        try {
            horario = LocalDateTime.parse(dt);
        } catch (DateTimeParseException e) {
            System.err.println("Formato de data/hora inválido. Operação cancelada.");
            return;
        }

        System.out.print("Diagnóstico (texto): ");
        String diagnostico = scanner.nextLine().trim();

        System.out.print("Valor (ex: 120.50): ");
        double valor = readDouble(scanner);

        Consulta c = new Consulta(0, horario, animal, vet, diagnostico, valor);

        try {
            Consulta criado = consultaDAO.create(c);
            System.out.println("Consulta criada com sucesso:");
            System.out.println(criado);
        } catch (RuntimeException e) {
            System.err.println("Erro ao criar consulta: " + e.getMessage());
        }
    }

    private void buscarConsultaPorId() {
        System.out.print("ID da consulta: ");
        int id = readInt(scanner);
        try {
            Consulta c = consultaDAO.getById(id);
            if (c == null) {
                System.out.println("Consulta não encontrada para id=" + id);
            } else {
                System.out.println(c);
            }
        } catch (RuntimeException e) {
            System.err.println("Erro ao buscar consulta: " + e.getMessage());
        }
    }

    private void atualizarConsulta() {
        System.out.print("ID da consulta a atualizar: ");
        int id = readInt(scanner);

        try {
            Consulta existente = consultaDAO.getById(id);
            if (existente == null) {
                System.out.println("Consulta não encontrada para id=" + id);
                return;
            }

            scanner.nextLine();
            System.out.print("Data/Hora (YYYY-MM-DDTHH:MM) (enter para manter): ");
            String dt = scanner.nextLine().trim();
            LocalDateTime novoHorario = dt.isEmpty() ? existente.getHorarioConsulta() : LocalDateTime.parse(dt);

            System.out.println("-- Animais disponíveis --");
            List<Animal> animals = animalDAO.getAll();
            for (Animal a : animals) System.out.printf("ID: %d  Nome: %s%n", a.getId(), a.getNome());
            System.out.print("ID do animal (enter para manter " + existente.getAnimal().getId() + "): ");
            String idAnimalStr = scanner.nextLine().trim();
            Animal novoAnimal = idAnimalStr.isEmpty() ? existente.getAnimal() : animalDAO.getById(Integer.parseInt(idAnimalStr));

            System.out.println("-- Veterinários disponíveis --");
            List<Veterinario> vets = veterinarioDAO.getAll();
            for (Veterinario v : vets) System.out.printf("ID: %d  Nome: %s%n", v.getId(), v.getNome());
            System.out.print("ID do veterinário (enter para manter " + existente.getVeterinario().getId() + "): ");
            String idVetStr = scanner.nextLine().trim();
            Veterinario novoVet = idVetStr.isEmpty() ? existente.getVeterinario() : veterinarioDAO.getById(Integer.parseInt(idVetStr));

            System.out.print("Diagnóstico (enter para manter): ");
            String diagnostico = scanner.nextLine().trim();
            diagnostico = diagnostico.isEmpty() ? existente.getDiagnostico() : diagnostico;

            System.out.print("Valor (enter para manter): ");
            String valorStr = scanner.nextLine().trim();
            double novoValor = valorStr.isEmpty() ? existente.getValor() : Double.parseDouble(valorStr);

            Consulta atualizado = new Consulta(id, novoHorario, novoAnimal, novoVet, diagnostico, novoValor);
            Consulta ret = consultaDAO.update(id, atualizado);
            if (ret == null) {
                System.out.println("Falha ao atualizar (retorno nulo)");
            } else {
                System.out.println("Atualizado:");
                System.out.println(ret);
            }

        } catch (DateTimeParseException dtEx) {
            System.err.println("Formato de data/hora inválido. Operação cancelada.");
        } catch (RuntimeException e) {
            System.err.println("Erro ao atualizar consulta: " + e.getMessage());
        }
    }

    private void excluirConsulta() {
        System.out.print("ID da consulta a excluir: ");
        int id = readInt(scanner);
        try {
            consultaDAO.delete(id);
            System.out.println("Consulta excluída (se existia) id=" + id);
        } catch (RuntimeException e) {
            System.err.println("Erro ao excluir consulta: " + e.getMessage());
        }
    }
}