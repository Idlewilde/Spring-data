import entities.Employee;
import entities.Project;
import entities.Town;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Last10_09 {
    public static void main(String[] args) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("PU_Name");
        EntityManager entityManager=factory.createEntityManager();

        entityManager.getTransaction().begin();

        Query from_projects = entityManager.createQuery("SELECT p FROM Project p ORDER BY p.startDate DESC", Project.class);
        List <Project> results = from_projects.getResultList().subList(0,10);
            results=results.stream()
                .sorted(Comparator.comparing(Project::getName)).collect(Collectors.toList());

        for (Project result : results) {
            System.out.println("Project name: "+result.getName());
            System.out.println("        Project description: "+result.getDescription());
            System.out.println("        Project Start Date: "+result.getStartDate());
            System.out.println("        Project End Date: "+result.getEndDate());

        }

        entityManager.getTransaction().commit();
    }
}
