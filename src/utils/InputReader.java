package utils;

import java.util.Scanner;

public class InputReader {

    public static int readInt(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.print("Digite um número válido: ");
            scanner.next();
        }
        return scanner.nextInt();
    }

    public static double readDouble(Scanner scanner) {
        while (!scanner.hasNextDouble()) {
            System.out.print("Digite um número válido (ex: 12.5): ");
            scanner.next();
        }
        return scanner.nextDouble();
    }
}
