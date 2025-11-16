import dao.AnimalDAO;
import dao.ProprietarioDAO;
import model.Animal;
import model.Proprietario;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {

        AnimalDAO animalDao = new AnimalDAO();
        ProprietarioDAO propDAO = new ProprietarioDAO();

        System.out.println(animalDao.getAll());

        Animal a = new Animal("cachorro", "cachorro", "cachorro", LocalDate.of(2025, 10, 10), 20.0, 1);

//        System.out.println(animalDao.create(a));
        
        Proprietario proprietario = new Proprietario("Adalton", "04117734599", "a@gmail.com", "719177-1512");
        
        //System.out.println(propDAO.create(proprietario));
      
        /*for(Proprietario prop : propDAO.getAll()) {
        	System.out.println(prop);
        	System.out.println();
        }*/
        
        //System.out.println(propDAO.getById(1));
        
       System.out.println(propDAO.update(1, proprietario));
        
        
        



    }
}