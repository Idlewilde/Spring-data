import entities.Address;
import entities.Employee;
import entities.Town;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class AddressesEmployeeCount_07 {
    public static void main(String[] args) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("PU_Name");
        EntityManager entityManager=factory.createEntityManager();

        entityManager.getTransaction().begin();

        Query from_addresses = entityManager.createQuery("SELECT a FROM Address a", Address.class);
        List<Address> resultList = from_addresses.getResultList();
        resultList= resultList.stream()
                .sorted((e1,e2)->Integer.compare(e2.getEmployees().size(),e1.getEmployees().size()))
                .limit(10)
                .collect(Collectors.toList());

        for (Address address : resultList) {
            System.out.println
                    (address.getText()+", "
                            +address.getTown().getName()+" - "
                            +address.getEmployees().size()+" employees");
        }
        entityManager.getTransaction().commit();
    }
}
