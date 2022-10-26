import entities.Employee;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class FindByFirstName_11 {
    public static void main(String[] args) {

        EntityManagerFactory factory = Persistence.createEntityManagerFactory("PU_Name");
        EntityManager entityManager=factory.createEntityManager();

        entityManager.getTransaction().begin();

        Scanner scanner = new Scanner(System.in);
        String pattern = scanner.nextLine();


        String selectQuery =
                "SELECT e FROM Employee AS e WHERE e.firstName LIKE '" + pattern + "%'";
        Query query = entityManager.createQuery(selectQuery);
        List<Employee> employees = query.getResultList();

        entityManager.getTransaction().commit();

        for (Employee employee : employees) {
            employee.setSalary(employee.getSalary().multiply(BigDecimal.valueOf(1.12)));
            entityManager.persist(employee);
            System.out.println(employee.getFirstName()+" "+employee.getLastName()+" - "
                    +employee.getJobTitle()+" - ($"+employee.getSalary()+")");
        }
    }
}
