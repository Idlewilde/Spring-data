import entities.Employee;
import entities.Town;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.List;

public class SalaryOver50000_04 {
    public static void main(String[] args) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("PU_Name");
        EntityManager entityManager=factory.createEntityManager();

        entityManager.getTransaction().begin();

        Query from_employees = entityManager.createQuery("SELECT e FROM Employee e", Employee.class);
        List<Employee> resultList = from_employees.getResultList();

        for (Employee employee : resultList) {
           if(employee.getSalary().intValue()>50000){
               System.out.println(employee.getFirstName());
           }
        }

        entityManager.getTransaction().commit();
    }
}
