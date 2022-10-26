import entities.Address;
import entities.Employee;
import entities.Town;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class RemoveTowns_13 {
    public static void main(String[] args) throws IOException {

        EntityManagerFactory factory = Persistence.createEntityManagerFactory("PU_Name");
        EntityManager entityManager = factory.createEntityManager();

        entityManager.getTransaction().begin();
        Scanner scanner = new Scanner(System.in);
        String townToDelete = scanner.nextLine();

        setEmployeeAdressToNull(entityManager, townToDelete);

        List<Address> addresses = deleteAddresses(entityManager, townToDelete);

        deleteTown(entityManager, townToDelete);

        System.out.println(addresses.size() + " address in " + townToDelete + " deleted");

        entityManager.getTransaction().commit();
    }

    private static void deleteTown(EntityManager entityManager, String townForDelete) {
        String deleteTownQuery = "SELECT t FROM Town AS t WHERE t.name = '" + townForDelete + "'";
        Query queryTown = entityManager.createQuery(deleteTownQuery);
        Town town = (Town) queryTown.getSingleResult();
        entityManager.remove(town);
    }

    private static List<Address> deleteAddresses(EntityManager entityManager, String townForDelete) {
        String hqlQuery = "SELECT a FROM Address AS a WHERE a.town.name = '" + townForDelete + "'";
        Query queryAddressesByTown = entityManager.createQuery(hqlQuery);
        List<Address> addresses = queryAddressesByTown.getResultList();

        for (Address address : addresses) {
            entityManager.remove(address);
        }
        return addresses;
    }

    private static void setEmployeeAdressToNull(EntityManager entityManager, String townForDelete) {
        String query = "SELECT e FROM Employee AS e WHERE e.address.town.name = '" + townForDelete + "'";
        Query getEmployeesFromAddr = entityManager.createQuery(query);
        List<Employee> employees = getEmployeesFromAddr.getResultList();
        employees.forEach(e -> e.setAddress(null));
    }
}