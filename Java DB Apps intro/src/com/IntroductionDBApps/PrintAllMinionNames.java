package com.IntroductionDBApps;

import java.sql.*;
import java.util.*;

public class PrintAllMinionNames {
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
                connection.prepareStatement("SELECT `name` from minions");

        ResultSet rs = stmt.executeQuery();
        List<String> minions = new ArrayList<>();

        while (rs.next()) {
            minions.add(rs.getString(1));
        }

        int length = minions.size()/2;
        int lastMinion = minions.size()%2;

        for (int i = 0; i < length; i++) {
            System.out.println(minions.get(i));
            System.out.println(minions.get(minions.size()-i-1));
        }
        if(lastMinion==1){
             System.out.println(minions.get(length));}

        connection.close();
    }
}
