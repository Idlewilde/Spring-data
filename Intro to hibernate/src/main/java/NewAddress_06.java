import entities.Address;
import entities.Employee;
import entities.Town;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.List;
import java.util.Scanner;

public class NewAddress_06 {
    public static void main(String[] args) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("PU_Name");
        EntityManager entityManager = factory.createEntityManager();

        entityManager.getTransaction().begin();

        Scanner scanner = new Scanner(System.in);
        String lastName = scanner.nextLine();
        Address address = new Address();
        address.setText("Vitoshka 15");
        entityManager.persist(address);

        Query from_employees = entityManager.createQuery("SELECT e FROM Employee e", Employee.class);
        List<Employee> resultList = from_employees.getResultList();

        for (Employee employee : resultList) {
            if (employee.getLastName().equals(lastName)) {
                employee.setAddress(address);
                entityManager.persist(employee);
            }}

            entityManager.getTransaction().commit();
        }
    }
