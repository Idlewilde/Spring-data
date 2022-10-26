package com.IntroductionDBApps;

import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class AddMinion {

    public static void main(String[] args) throws SQLException {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter username default (root): ");
        String user = sc.nextLine();
        user = user.equals("") ? "root" : user;
        System.out.println();

        System.out.print("Enter password default (empty):");
        String password = sc.nextLine().trim();
        System.out.println();

        Properties props = new Properties();
        props.setProperty("user", user);
        props.setProperty("password", password);

        Connection connection = DriverManager
                .getConnection("jdbc:mysql://localhost:3306/minions_db", props);

        PreparedStatement findVillain =
                connection.prepareStatement("SELECT * FROM villains WHERE `name`=?;");
        PreparedStatement findTown =
                connection.prepareStatement("SELECT * FROM towns WHERE `name`=?;");

        String[] minionInfo = sc.nextLine().split(" ");
        String minion = minionInfo[1];
        int age = Integer.parseInt(minionInfo[2]);
        String town = minionInfo[3];
        String villain = sc.nextLine().split(" ")[1];

        findVillain.setString(1, villain);
        findTown.setString(1, town);

        ResultSet villainResults = findVillain.executeQuery();
        ResultSet townResults = findTown.executeQuery();
        int townID = 0;
        int villainID = 0;

        if (!townResults.next()) {
            PreparedStatement insertTown =
                    connection.prepareStatement("INSERT INTO towns (`name`) VALUES(?);");
            insertTown.setString(1, town);
            insertTown.executeUpdate();
            System.out.println("Town " + town + " was added to the database.");
            ResultSet newTownResults = findTown.executeQuery();
            newTownResults.next();
            townID = newTownResults.getInt("id");

        } else {
            townID = townResults.getInt("id");
        }

        if (!villainResults.next()) {
            PreparedStatement insertVillain =
                    connection.prepareStatement("INSERT INTO villains (`name`,evilness_factor) VALUES(?,\"evil\");");
            insertVillain.setString(1, villain);
            insertVillain.executeUpdate();
            ResultSet newVillainResults = findVillain.executeQuery();
            newVillainResults.next();
            villainID = newVillainResults.getInt("id");
            System.out.println("Villain " + villain + " was added to the database.");
        } else {
            villainID = villainResults.getInt("id");
        }

        PreparedStatement insertMinion =
                connection.prepareStatement("INSERT INTO minions (`name`,age,town_id) VALUES(?,?,?)");
        insertMinion.setString(1, minion);
        insertMinion.setInt(2, age);
        insertMinion.setInt(3, townID);
        insertMinion.executeUpdate();
        PreparedStatement findMinion =
                connection.prepareStatement("SELECT id FROM minions WHERE `name`=?;");
        findMinion.setString(1, minion);
        ResultSet newMinionResult = findMinion.executeQuery();
        newMinionResult.next();
        int minionID = newMinionResult.getInt("id");

        PreparedStatement linkMinion =
                connection.prepareStatement("INSERT INTO minions_villains (minion_id,villain_id) VALUES(?,?)");
        linkMinion.setInt(1, minionID);
        linkMinion.setInt(2, villainID);
        linkMinion.executeUpdate();
        System.out.println("Successfully added " + minion + " to be minion of " + villain);

        connection.close();
    }
}
