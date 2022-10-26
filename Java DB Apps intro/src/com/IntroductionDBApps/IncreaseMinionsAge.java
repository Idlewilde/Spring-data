package com.IntroductionDBApps;

import java.sql.*;
import java.util.*;

public class IncreaseMinionsAge {

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

        String [] input = sc.nextLine().split(" ");
        String ids = String.join(", ",input);

        PreparedStatement stmt =
                connection.prepareStatement("UPDATE minions SET age=age+1 WHERE id in ("+ids+")");

        stmt.executeUpdate();

        PreparedStatement printMinions = connection.prepareStatement("SELECT `name`,age FROM minions");

        ResultSet rs = printMinions.executeQuery();

        while (rs.next()) {
            System.out.println(rs.getString("name") + " " + rs.getInt("age"));
        }

        connection.close();
    }
}
