import entities.Employee;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.List;

public class RD_05 {
    public static void main(String[] args) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("PU_Name");
        EntityManager entityManager = factory.createEntityManager();

        entityManager.getTransaction().begin();

        Query from_employees = entityManager.createQuery("SELECT e FROM Employee e ORDER by e.salary,e.id", Employee.class);
        List<Employee> resultList = from_employees.getResultList();

        for (Employee employee : resultList) {
            if (employee.getDepartment().getName().equals("Research and Development")) {
                System.out.println(employee.getFirstName()+ " "+employee.getLastName()+" from Research and Development - $"+employee.getSalary());
            }
        }

        entityManager.getTransaction().commit();
    }
}
