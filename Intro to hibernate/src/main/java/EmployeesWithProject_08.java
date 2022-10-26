import entities.Employee;
import entities.Project;
import entities.Town;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;


public class EmployeesWithProject_08 {
    public static void main(String[] args) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("PU_Name");
        EntityManager entityManager=factory.createEntityManager();

        entityManager.getTransaction().begin();

        Scanner scanner = new Scanner(System.in);
        int id = Integer.parseInt(scanner.nextLine());

        Employee employee = entityManager.find(Employee.class, id);
        List <String> prNames = new ArrayList<>();
        employee.getProjects().forEach(e->prNames.add(e.getName()));
        Collections.sort(prNames);

        System.out.println(employee.getFirstName()+" "+employee.getLastName()+" - "+employee.getJobTitle());
        prNames.forEach(System.out::println);

        entityManager.getTransaction().commit();
    }
}
