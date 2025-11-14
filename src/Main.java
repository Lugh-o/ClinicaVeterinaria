import dao.AnimalDAO;
import model.Animal;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {

        AnimalDAO animalDao = new AnimalDAO();

        System.out.println(animalDao.getAll());

        Animal a = new Animal("cachorro", "cachorro", "cachorro", LocalDate.of(2025, 10, 10), 20.0, 1);

//        System.out.println(animalDao.create(a));



    }
}