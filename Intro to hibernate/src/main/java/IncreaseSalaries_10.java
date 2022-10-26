import entities.Employee;
import entities.Town;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.List;

public class IncreaseSalaries_10 {

    public static void main(String[] args) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("PU_Name");
        EntityManager entityManager=factory.createEntityManager();

        entityManager.getTransaction().begin();

        String selectQuery =
                "SELECT e FROM Employee AS e " +
                        "WHERE e.department.name IN ('Engineering', 'Tool Design', 'Marketing', 'Information Services')";
        Query query = entityManager.createQuery(selectQuery);
        List<Employee> employees = query.getResultList();


        for (Employee employee : employees) {
            employee.setSalary(employee.getSalary().multiply(BigDecimal.valueOf(1.12)));
            entityManager.persist(employee);
            System.out.println(employee.getFirstName()+" "+employee.getLastName()+" ($"+employee.getSalary()+")");
        }

        entityManager.getTransaction().commit();


    }
}
