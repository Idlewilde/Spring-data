package com.IntroductionDBApps;

import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class MinionNames {
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
                connection.prepareStatement("SELECT v.`name`,m.`name`, m.age FROM minions AS m\n" +
                        "JOIN minions_villains AS mv ON m.id=mv.minion_id\n" +
                        "JOIN villains AS v ON mv.villain_id=v.id\n" +
                        "WHERE mv.villain_id = ? \n" +
                        "GROUP BY m.`name`;");

        int villainId = Integer.parseInt(sc.nextLine());
        stmt.setInt(1,villainId);

        ResultSet rs = stmt.executeQuery();
        boolean villainNamePrinted = false;
        boolean hasResults=false;
        int minionIndex=0;
        while (rs.next()) {
            hasResults=true;
            if(!villainNamePrinted){
                System.out.println("Villain: "+rs.getString(1));villainNamePrinted=true;}
            minionIndex++;
            System.out.println(minionIndex+". "+rs.getString(2) + " " + rs.getInt(3));
        }
        if(!hasResults){
            System.out.println("No villain with ID "+villainId+" exists in the database.");
        }
        connection.close();
    }
}
