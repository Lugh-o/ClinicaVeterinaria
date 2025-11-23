package view;

import dao.AnimalDAO;
import model.Animal;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

import static utils.InputReader.readDouble;
import static utils.InputReader.readInt;

public record AnimalCLI(AnimalDAO animalDAO, Scanner scanner) {
    public void start() {
        int option;
        do {
            System.out.println("===== MENU ANIMAIS =====");
            System.out.println("1 - Listar animais");
            System.out.println("2 - Cadastrar animal");
            System.out.println("3 - Buscar animal por ID");
            System.out.println("4 - Atualizar animal");
            System.out.println("5 - Excluir animal");
            System.out.println("0 - Sair");
            System.out.print("Escolha uma opção: ");

            option = readInt(scanner);

            switch (option) {
                case 1:
                    listarAnimais();
                    break;
                case 2:
                    cadastrarAnimal();
                    break;
                case 3:
                    buscarAnimalPorId();
                    break;
                case 4:
                    atualizarAnimal();
                    break;
                case 5:
                    excluirAnimal();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        } while (option != 0);
    }

    private void listarAnimais() {
        try {
            List<Animal> lista = animalDAO.getAll();
            if (lista.isEmpty()) {
                System.out.println("Nenhum animal cadastrado.");
                return;
            }
            for (Animal a : lista) {
                System.out.println(a);
                System.out.println("---------------------");
            }
        } catch (RuntimeException e) {
            System.err.println("Erro ao listar animais: " + e.getMessage());
        }
    }

    private void cadastrarAnimal() {
        scanner.nextLine();
        System.out.print("Nome: ");
        String nome = scanner.nextLine().trim();
        System.out.print("Espécie: ");
        String especie = scanner.nextLine().trim();
        System.out.print("Raça: ");
        String raca = scanner.nextLine().trim();
        System.out.print("Data de nascimento (YYYY-MM-DD): ");
        String data = scanner.nextLine().trim();
        LocalDate dataNascimento;
        try {
            dataNascimento = LocalDate.parse(data);
        } catch (DateTimeParseException ex) {
            System.err.println("Data inválida. Operação cancelada.");
            return;
        }
        System.out.print("Peso (ex: 12.5): ");
        double peso = readDouble(scanner);
        System.out.print("ID do proprietário: ");
        int idProp = readInt(scanner);

        Animal a = new Animal(
                0,
                nome,
                especie,
                raca,
                dataNascimento,
                peso,
                idProp
        );

        try {
            Animal criado = animalDAO.create(a);
            System.out.println("Animal criado com sucesso:");
            System.out.println(criado);
        } catch (RuntimeException e) {
            System.err.println("Erro ao criar animal: " + e.getMessage());
        }
    }

    private void buscarAnimalPorId() {
        System.out.print("ID do animal: ");
        int id = readInt(scanner);
        try {
            Animal a = animalDAO.getById(id);
            if (a == null) {
                System.out.println("Animal não encontrado para id=" + id);
            } else {
                System.out.println(a);
            }
        } catch (RuntimeException e) {
            System.err.println("Erro ao buscar animal: " + e.getMessage());
        }
    }

    private void atualizarAnimal() {
        System.out.print("ID do animal a atualizar: ");
        int id = readInt(scanner);
        scanner.nextLine();
        System.out.print("Nome (novo, enter para manter): ");
        String nome = scanner.nextLine().trim();
        System.out.print("Espécie (novo, enter para manter): ");
        String especie = scanner.nextLine().trim();
        System.out.print("Raça (novo, enter para manter): ");
        String raca = scanner.nextLine().trim();
        System.out.print("Data de nascimento (YYYY-MM-DD, enter para manter): ");
        String data = scanner.nextLine().trim();
        System.out.print("Peso (enter para manter): ");
        String pesoStr = scanner.nextLine().trim();
        System.out.print("ID do proprietário (enter para manter): ");
        String idPropStr = scanner.nextLine().trim();

        try {
            Animal existente = animalDAO.getById(id);
            if (existente == null) {
                System.out.println("Animal não encontrado para id=" + id);
                return;
            }

            String novoNome = nome.isEmpty() ? existente.getNome() : nome;
            String novoEspecie = especie.isEmpty() ? existente.getEspecie() : especie;
            String novoRaca = raca.isEmpty() ? existente.getRaca() : raca;
            LocalDate novoData = data.isEmpty() ? existente.getDataNascimento() : LocalDate.parse(data);
            double novoPeso = pesoStr.isEmpty() ? existente.getPeso() : Double.parseDouble(pesoStr);
            int novoIdProp = idPropStr.isEmpty() ? existente.getIdProprietario() : Integer.parseInt(idPropStr);

            Animal atualizado = new Animal(
                    id,
                    novoNome,
                    novoEspecie,
                    novoRaca,
                    novoData,
                    novoPeso,
                    novoIdProp
            );

            atualizado.setIdProprietario(novoIdProp);
            Animal ret = animalDAO.update(id, atualizado);
            if (ret == null) {
                System.out.println("Falha ao atualizar (retorno nulo)");
            } else {
                System.out.println("Atualizado: ");
                System.out.println(ret);
            }
        } catch (RuntimeException e) {
            System.err.println("Erro ao atualizar animal: " + e.getMessage());
        }
    }

    private void excluirAnimal() {
        System.out.print("ID do animal a excluir: ");
        int id = readInt(scanner);
        try {
            animalDAO.delete(id);
            System.out.println("Animal excluído (se existia) id=" + id);
        } catch (RuntimeException e) {
            System.err.println("Erro ao excluir animal: " + e.getMessage());
        }
    }
}