import dao.*;
import view.*;

import java.util.Scanner;

import static utils.InputReader.readInt;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AnimalDAO animalDAO = new AnimalDAO();
        AnimalCLI animalCLI = new AnimalCLI(animalDAO, scanner);
        ProprietarioDAO proprietarioDAO = new ProprietarioDAO();
        ProprietarioCLI proprietarioCLI = new ProprietarioCLI(scanner, proprietarioDAO);
        VeterinarioDAO veterinarioDAO = new VeterinarioDAO();
        VeterinarioCLI veterinarioCLI = new VeterinarioCLI(scanner, veterinarioDAO);
        ConsultaDAO consultaDAO = new ConsultaDAO();
        ConsultaCLI consultaCLI = new ConsultaCLI(scanner, consultaDAO, animalDAO, veterinarioDAO);
        EspecialidadeDAO especialidadeDAO = new EspecialidadeDAO();
        EspecialidadeCLI especialidadeCLI = new EspecialidadeCLI(scanner, especialidadeDAO);

        int option;
        do {
            System.out.println("===== MENU GERAL =====");
            System.out.println("1 - Animal");
            System.out.println("2 - Consulta");
            System.out.println("3 - Proprietario");
            System.out.println("4 - Veterinário");
            System.out.println("5 - Especialidade");
            System.out.println("0 - Sair");
            System.out.print("Escolha uma opção: ");
            option = readInt(scanner);

            switch (option) {
                case 1:
                    animalCLI.start();
                    break;
                case 2:
                    consultaCLI.start();
                    break;
                case 3:
                    proprietarioCLI.start();
                    break;
                case 4:
                    veterinarioCLI.start();
                    break;
                case 5:
                    especialidadeCLI.start();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        } while (option != 0);
    }
}