package com.IntroductionDBApps;

import java.sql.*;
import java.util.*;

public class ChangeTownNamesCasing {
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

        String country = sc.nextLine();
        PreparedStatement stmt =
                connection.prepareStatement("SELECT `name` from towns WHERE country = ?");
        stmt.setString(1,country);
        ResultSet rs = stmt.executeQuery();
        Set<String> towns = new LinkedHashSet<>();

        while(rs.next()){
            String town = rs.getString("name");
            String upperCase = town.toUpperCase();
            if(!town.equals(upperCase)){
                towns.add(upperCase);
                PreparedStatement updateTown =
                        connection.prepareStatement("UPDATE towns SET `name`=? WHERE `name`=?");
                updateTown.setString(1,upperCase);
                updateTown.setString(2,town);
                updateTown.executeUpdate();
                towns.add(upperCase);
            }
        }

        if(towns.isEmpty()){
            System.out.println("No town names were affected.");}
        else{
            System.out.println(towns.size()+" town names were affected.");
            System.out.println(towns);
        }


        connection.close();
    }
}
