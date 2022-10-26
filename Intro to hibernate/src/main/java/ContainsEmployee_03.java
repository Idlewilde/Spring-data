import entities.Employee;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.List;
import java.util.Scanner;

public class ContainsEmployee_03 {
    public static void main(String[] args) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("PU_Name");
        EntityManager entityManager = factory.createEntityManager();

        entityManager.getTransaction().begin();
        Scanner scanner = new Scanner(System.in);

        String name = scanner.nextLine();

        Query from_employees = entityManager.createQuery("SELECT e FROM Employee e", Employee.class);
        List<Employee> resultList = from_employees.getResultList();
        boolean containsEmployee = false;

        for (Employee employee : resultList) {
            if ((employee.getFirstName() + " " + employee.getLastName()).equals(name)) {
                System.out.println("Yes");
                containsEmployee = true;
                break;
            }
        }

        if (!containsEmployee) {
            System.out.println("No");
        }
        entityManager.getTransaction().commit();
    }
}
