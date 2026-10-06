package com.pratik.pharma.util;

import java.sql.Connection;

public class DBTest {

    public static void main(String[] args) {

        try {

            Connection connection = DBConnection.getConnection();

            System.out.println("Database connection successful!");

            connection.close();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}