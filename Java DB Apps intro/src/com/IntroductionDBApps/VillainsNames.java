package com.IntroductionDBApps;

import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class VillainsNames {
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

        PreparedStatement stmt =
                connection.prepareStatement("SELECT v.`name`, \" \", COUNT(vm.minion_id) as counts from villains as v \n" +
                        "join minions_villains as vm on v.id=vm.villain_id\n" +
                        "group by vm.villain_id \n" +
                        "having count(vm.minion_id)>15 order by counts DESC;");

        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            System.out.println(rs.getString("name") + " " + rs.getString("counts"));
        }
        connection.close();
    }
}