package com.IntroductionDBApps;

import java.sql.*;

import java.util.Properties;
import java.util.Scanner;


public class DeleteVillain {
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

        int villainID = Integer.parseInt(sc.nextLine());

        try{
        connection.setAutoCommit(false);
        PreparedStatement stmt =
                connection.prepareStatement("delete from minions_villains where villain_id=?");
        stmt.setInt(1, villainID);
        int minionCount = stmt.executeUpdate();
        PreparedStatement selectVillain =
                connection.prepareStatement("SELECT `name` FROM villains WHERE id=?");
        selectVillain.setInt(1, villainID);

        ResultSet rs = selectVillain.executeQuery();

        if (rs.next()) {
            String villainName = rs.getString("name");
            PreparedStatement deleteVillain =
                    connection.prepareStatement("delete from villains where id=?");
            deleteVillain.setInt(1, villainID);
            int villainCount = deleteVillain.executeUpdate();
            if (villainCount > 0) {
                System.out.println(villainName + " was deleted\n" + minionCount + " minions released");
            }
        } else {
            System.out.println("No such villain was found");
        }
        connection.commit();}
        catch(SQLException se){connection.rollback();}
        connection.close();
    }
}
