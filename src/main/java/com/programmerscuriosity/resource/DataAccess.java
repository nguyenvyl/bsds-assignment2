package com.programmerscuriosity.resource;

import com.programmerscuriosity.model.RFIDLiftData;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class DataAccess {

    private static final String PUBLIC_DNS = "aa4o9xnp4f1176.cdqh8w1txiil.us-west-2.rds.amazonaws.com";
    private static final String PORT = "3306";
    private static final String DATABASE = "ebdb";
    private static final String REMOTE_DATABASE_USERNAME = "nguyenvyl";
    private static final String DATABASE_USER_PASSWORD = "Typhlosion1";

    private static final String userQuery = "SELECT lift_id, COUNT(*) FROM rides WHERE skier_id=? AND day=? GROUP BY lift_id";

    private Connection connection;

    public DataAccess() {
        getAWSConnection();
    }

    public void getAWSConnection() {

        System.out.println("----MySQL JDBC Connection Testing -------");

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your MySQL JDBC Driver?");
            e.printStackTrace();
        }

        System.out.println("MySQL JDBC Driver Registered!");
        Connection connection = null;

        try {
            connection = DriverManager.
                    getConnection("jdbc:mysql://" + PUBLIC_DNS + ":" + PORT + "/" + DATABASE, REMOTE_DATABASE_USERNAME, DATABASE_USER_PASSWORD);
        } catch (SQLException e) {
            System.out.println("Connection Failed!:\n" + e.getMessage());
        }

        if (connection == null) {
            System.out.println("Unable to connect to database!");
        } else {
            this.connection = connection;
        }
    }

    public void writeRFIDDataToDatabase(RFIDLiftData liftData) {
        Statement statement = null;
        String tableName = "rfid_data_day_" + liftData.getDayNum();
        try {
            String dayNum = Integer.toString(liftData.getDayNum());
            statement = this.connection.createStatement();
            String sql = "INSERT INTO " + tableName + " VALUES " + liftData.toSQLString();
            System.out.println(sql);
            int result = statement.executeUpdate(sql);
            System.out.println(Integer.toString(result));
        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        }
    }

    public void writeRFIDBatchToDatabase(List<RFIDLiftData> dataList) {
        Statement statement = null;
        try {
            for (RFIDLiftData data : dataList) {
                String tableName = "rfid_data_day_" + data.getDayNum();
                connection.setAutoCommit(false);
                statement = this.connection.createStatement();
                String sql = "INSERT INTO " + tableName + " VALUES " + data.toSQLString();
                statement.addBatch(sql);
            }
            statement.executeBatch();
            connection.commit();
        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        }

    }


    /**
     * One time use to load user IDs into user table
     *
     * @param liftData
     */
    public void writeUserIdToDatabase(RFIDLiftData liftData) {
        Statement statement;
        try {
            statement = this.connection.createStatement();
            String sql = "INSERT INTO user_data VALUES (" + Integer.toString(liftData.getSkierID()) + ", 0,0)";
            System.out.println(sql);
            statement.executeUpdate(sql);
//            System.out.println(Integer.toString(result));
        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        }
    }

}
