import entities.Department;
import entities.Employee;
import entities.Project;
import entities.Town;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class EmployeesMaxSalaries_12 {
    public static void main(String[] args) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("PU_Name");
        EntityManager entityManager=factory.createEntityManager();

        entityManager.getTransaction().begin();

        Query from_dept = entityManager.createQuery("SELECT d FROM Department d", Department.class);
        List <Department> resultList = from_dept.getResultList();


        List <String> output = new ArrayList<>();
        for (Department department : resultList) {

            BigDecimal salary= department.getEmployees()
                        .stream()
                        .sorted(Comparator.comparing(Employee::getSalary).reversed())
                        .limit(1).collect(Collectors.toList()).get(0).getSalary();
            double value = salary.doubleValue();
            String name = department.getName();

           if(value<=30000||value>=70000){
               output.add(department.getName()+" "+salary);
           }
        }
        entityManager.getTransaction().commit();

        System.out.println(String.join("\n",output));


    }
}
