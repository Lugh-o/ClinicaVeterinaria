package view;

import dao.VeterinarioDAO;
import model.Veterinario;

import java.util.List;
import java.util.Scanner;

import static utils.InputReader.readInt;

public record VeterinarioCLI(Scanner scanner, VeterinarioDAO veterinarioDAO) {
    public void start() {
        int option;
        do {
            System.out.println("===== MENU VETERINÁRIOS =====");
            System.out.println("1 - Listar veterinários");
            System.out.println("2 - Cadastrar veterinário");
            System.out.println("3 - Buscar veterinário por ID");
            System.out.println("4 - Atualizar veterinário");
            System.out.println("5 - Excluir veterinário");
            System.out.println("0 - Sair");
            System.out.print("Escolha uma opção: ");

            option = readInt(scanner);

            switch (option) {
                case 1:
                    listarVeterinarios();
                    break;
                case 2:
                    cadastrarVeterinario();
                    break;
                case 3:
                    buscarVeterinarioPorId();
                    break;
                case 4:
                    atualizarVeterinario();
                    break;
                case 5:
                    excluirVeterinario();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        } while (option != 0);
    }

    private void listarVeterinarios() {
        try {
            List<Veterinario> lista = veterinarioDAO.getAll();
            if (lista.isEmpty()) {
                System.out.println("Nenhum veterinário cadastrado.");
                return;
            }
            for (Veterinario v : lista) {
                System.out.println(v);
                System.out.println("---------------------");
            }
        } catch (RuntimeException e) {
            System.err.println("Erro ao listar veterinários: " + e.getMessage());
        }
    }

    private void cadastrarVeterinario() {
        scanner.nextLine();
        System.out.print("Nome: ");
        String nome = scanner.nextLine().trim();
        System.out.print("CRMV: ");
        String crmv = scanner.nextLine().trim();
        System.out.print("ID da especialidade (número): ");
        int idEspecialidade = readInt(scanner);
        scanner.nextLine();
        System.out.print("Telefone: ");
        String telefone = scanner.nextLine().trim();

        Veterinario v = new Veterinario(0, nome, crmv, idEspecialidade, "", 0, telefone);

        try {
            Veterinario criado = veterinarioDAO.create(v);
            System.out.println("Veterinário criado com sucesso:");
            System.out.println(criado);
        } catch (RuntimeException e) {
            System.err.println("Erro ao criar veterinário: " + e.getMessage());
        }
    }

    private void buscarVeterinarioPorId() {
        System.out.print("ID do veterinário: ");
        int id = readInt(scanner);
        try {
            Veterinario v = veterinarioDAO.getById(id);
            if (v == null) {
                System.out.println("Veterinário não encontrado para id=" + id);
            } else {
                System.out.println(v);
            }
        } catch (RuntimeException e) {
            System.err.println("Erro ao buscar veterinário: " + e.getMessage());
        }
    }

    private void atualizarVeterinario() {
        System.out.print("ID do veterinário a atualizar: ");
        int id = readInt(scanner);
        scanner.nextLine();
        try {
            Veterinario existente = veterinarioDAO.getById(id);
            if (existente == null) {
                System.out.println("Veterinário não encontrado para id=" + id);
                return;
            }

            System.out.print("Nome (novo, enter para manter): ");
            String nome = scanner.nextLine().trim();
            System.out.print("CRMV (novo, enter para manter): ");
            String crmv = scanner.nextLine().trim();
            System.out.print("ID da especialidade (novo, enter para manter): ");
            String idEspStr = scanner.nextLine().trim();
            System.out.print("Telefone (novo, enter para manter): ");
            String telefone = scanner.nextLine().trim();

            String novoNome = nome.isEmpty() ? existente.getNome() : nome;
            String novoCrmv = crmv.isEmpty() ? existente.getCrmv() : crmv;
            int novoIdEsp = idEspStr.isEmpty() ? existente.getIdEspecialidade() : Integer.parseInt(idEspStr);
            String novoTelefone = telefone.isEmpty() ? existente.getTelefone() : telefone;

            Veterinario atualizado = new Veterinario(id, novoNome, novoCrmv, novoIdEsp, "", 0, novoTelefone);

            Veterinario ret = veterinarioDAO.update(id, atualizado);
            if (ret == null) {
                System.out.println("Falha ao atualizar (retorno nulo)");
            } else {
                System.out.println("Atualizado:");
                System.out.println(ret);
            }
        } catch (RuntimeException e) {
            System.err.println("Erro ao atualizar veterinário: " + e.getMessage());
        }
    }

    private void excluirVeterinario() {
        System.out.print("ID do veterinário a excluir: ");
        int id = readInt(scanner);
        try {
            veterinarioDAO.delete(id);
            System.out.println("Veterinário excluído (se existia) id=" + id);
        } catch (RuntimeException e) {
            System.err.println("Erro ao excluir veterinário: " + e.getMessage());
        }
    }
}