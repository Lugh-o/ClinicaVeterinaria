package view;

import dao.EspecialidadeDAO;
import model.Especialidade;

import java.util.ArrayList;
import java.util.Scanner;

import static utils.InputReader.readInt;

public record EspecialidadeCLI(Scanner scanner, EspecialidadeDAO dao) {
    public void start() {
        int op;

        do {
            System.out.println("===== MENU ESPECIALIDADES =====");
            System.out.println("1 - Listar");
            System.out.println("2 - Cadastrar");
            System.out.println("3 - Buscar por ID");
            System.out.println("4 - Atualizar");
            System.out.println("5 - Excluir");
            System.out.println("0 - Voltar");
            System.out.print("Escolha uma opção: ");

            op = readInt(scanner);

            switch (op) {
                case 1 -> listar();
                case 2 -> cadastrar();
                case 3 -> buscarPorId();
                case 4 -> atualizar();
                case 5 -> excluir();
                case 0 -> {
                }
                default -> System.out.println("Opção inválida.");
            }

        } while (op != 0);
    }

    private void listar() {
        try {
            ArrayList<Especialidade> lista = dao.getAll();
            if (lista.isEmpty()) {
                System.out.println("Nenhuma especialidade cadastrada.");
                return;
            }
            for (Especialidade e : lista) {
                System.out.println(e);
                System.out.println("---------------------");
            }
        } catch (RuntimeException e) {
            System.err.println("Erro ao listar especialidades: " + e.getMessage());
        }
    }

    private void cadastrar() {
        scanner.nextLine();
        System.out.print("Nome da especialidade: ");
        String nome = scanner.nextLine();

        Especialidade esp = new Especialidade(0, nome);
        dao.create(esp);

        System.out.println("Especialidade cadastrada. ID: " + esp.getId());
    }

    private void buscarPorId() {
        System.out.print("ID: ");
        int id = readInt(scanner);
        try {
            Especialidade esp = dao.getById(id);

            if (esp == null) {
                System.out.println("Especialidade não encontrada.");
            } else {
                System.out.println(esp);
            }
        } catch (RuntimeException e) {
            System.err.println("Erro ao buscar especialidade: " + e.getMessage());
        }
    }

    private void atualizar() {
        System.out.print("ID da especialidade: ");
        int id = readInt(scanner);

        scanner.nextLine();
        Especialidade existente = dao.getById(id);
        if (existente == null) {
            System.out.println("Especialidade não encontrada.");
            return;
        }

        System.out.print("Novo nome (enter para manter): ");
        String novoNome = scanner.nextLine();

        if (!novoNome.isEmpty()) existente.setNome(novoNome);

        Especialidade atualizado = dao.update(id, existente);

        if (atualizado == null) {
            System.out.println("Falha ao atualizar.");
        } else {
            System.out.println("Atualizado: " + atualizado);
        }
    }

    private void excluir() {
        System.out.print("ID da especialidade: ");
        int id = readInt(scanner);

        dao.delete(id);

        System.out.println("Especialidade excluída.");
    }
}
