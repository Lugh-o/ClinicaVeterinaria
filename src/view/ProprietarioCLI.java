package view;

import dao.ProprietarioDAO;
import model.Proprietario;

import java.util.List;
import java.util.Scanner;

import static utils.InputReader.readInt;

public record ProprietarioCLI(Scanner scanner, ProprietarioDAO proprietarioDAO) {
    public void start() {
        int option;
        do {
            System.out.println("===== MENU PROPRIETÁRIOS =====");
            System.out.println("1 - Listar proprietários");
            System.out.println("2 - Cadastrar proprietário");
            System.out.println("3 - Buscar proprietário por ID");
            System.out.println("4 - Atualizar proprietário");
            System.out.println("5 - Excluir proprietário");
            System.out.println("0 - Sair");
            System.out.print("Escolha uma opção: ");

            option = readInt(scanner);

            switch (option) {
                case 1:
                    listarProprietarios();
                    break;
                case 2:
                    cadastrarProprietario();
                    break;
                case 3:
                    buscarProprietarioPorId();
                    break;
                case 4:
                    atualizarProprietario();
                    break;
                case 5:
                    excluirProprietario();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        } while (option != 0);
    }

    private void listarProprietarios() {
        try {
            List<Proprietario> lista = proprietarioDAO.getAll();
            if (lista.isEmpty()) {
                System.out.println("Nenhum proprietário cadastrado.");
                return;
            }
            for (Proprietario p : lista) {
                System.out.println(p);
                System.out.println("---------------------");
            }
        } catch (RuntimeException e) {
            System.err.println("Erro ao listar proprietários: " + e.getMessage());
        }
    }

    private void cadastrarProprietario() {
        scanner.nextLine();
        System.out.print("Nome: ");
        String nome = scanner.nextLine().trim();
        System.out.print("CPF: ");
        String cpf = scanner.nextLine().trim();
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Telefone: ");
        String telefone = scanner.nextLine().trim();

        Proprietario p = new Proprietario(
                0,
                0,
                nome,
                cpf,
                email,
                telefone
        );

        try {
            Proprietario criado = proprietarioDAO.create(p);
            System.out.println("Proprietário criado com sucesso: ");
            System.out.println(criado);
        } catch (RuntimeException e) {
            System.err.println("Erro ao criar proprietário: " + e.getMessage());
        }
    }

    private void buscarProprietarioPorId() {
        System.out.print("ID do proprietário: ");
        int id = readInt(scanner);
        try {
            Proprietario p = proprietarioDAO.getById(id);
            if (p == null) {
                System.out.println("Proprietário não encontrado para id=" + id);
            } else {
                System.out.println(p);
            }
        } catch (RuntimeException e) {
            System.err.println("Erro ao buscar proprietário: " + e.getMessage());
        }
    }

    private void atualizarProprietario() {
        System.out.print("ID do proprietário a atualizar: ");
        int id = readInt(scanner);
        scanner.nextLine();
        System.out.print("Nome (novo, enter para manter): ");
        String nome = scanner.nextLine().trim();
        System.out.print("CPF (novo, enter para manter): ");
        String cpf = scanner.nextLine().trim();
        System.out.print("Email (novo, enter para manter): ");
        String email = scanner.nextLine().trim();
        System.out.print("Telefone (novo, enter para manter): ");
        String telefone = scanner.nextLine().trim();

        try {
            Proprietario existente = proprietarioDAO.getById(id);
            if (existente == null) {
                System.out.println("Proprietário não encontrado para id=" + id);
                return;
            }

            String novoNome = nome.isEmpty() ? existente.getNome() : nome;
            String novoCpf = cpf.isEmpty() ? existente.getCpf() : cpf;
            String novoEmail = email.isEmpty() ? existente.getEmail() : email;
            String novoTelefone = telefone.isEmpty() ? existente.getTelefone() : telefone;

            Proprietario atualizado = new Proprietario(
                    id,
                    0,
                    novoNome,
                    novoCpf,
                    novoEmail,
                    novoTelefone
            );

            Proprietario ret = proprietarioDAO.update(id, atualizado);
            if (ret == null) {
                System.out.println("Falha ao atualizar (retorno nulo)");
            } else {
                System.out.println("Atualizado: ");
                System.out.println(ret);
            }
        } catch (RuntimeException e) {
            System.err.println("Erro ao atualizar proprietário: " + e.getMessage());
        }
    }

    private void excluirProprietario() {
        System.out.print("ID do proprietário a excluir: ");
        int id = readInt(scanner);
        try {
            proprietarioDAO.delete(id);
            System.out.println("Proprietário excluído (se existia) id=" + id);
        } catch (RuntimeException e) {
            System.err.println("Erro ao excluir proprietário: " + e.getMessage());
        }
    }
}