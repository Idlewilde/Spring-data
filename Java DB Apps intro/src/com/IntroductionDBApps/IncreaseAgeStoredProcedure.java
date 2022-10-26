package com.IntroductionDBApps;

import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class IncreaseAgeStoredProcedure {
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

//        Stored procedure to be created in Workbench:
//        DELIMITER $$
//        CREATE PROCEDURE usp_get_older (minion_id INT)
//        BEGIN
//        UPDATE minions
//        SET age=age+1 WHERE id=minion_id;
//        END $$

        int minionID = Integer.parseInt(sc.nextLine());

        PreparedStatement stmt =
                connection.prepareStatement("call usp_get_older (?)");
        stmt.setInt(1,minionID);

        stmt.executeUpdate();

        PreparedStatement printMinions = connection.prepareStatement("SELECT `name`,age FROM minions WHERE id=?");
        printMinions.setInt(1,minionID);

        ResultSet rs = printMinions.executeQuery();

        while (rs.next()) {
            System.out.println(rs.getString("name") + " " + rs.getInt("age"));
        }

        connection.close();
    }
}
