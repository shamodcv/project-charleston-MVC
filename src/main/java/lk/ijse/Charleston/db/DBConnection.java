package lk.ijse.Charleston.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static DBConnection dbConnection;
    private Connection connection;

    private DBConnection() throws SQLException {
        connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/charleston",
                "root",
                "1234"
        );

    }

    public static DBConnection getInstance() throws SQLException {
        return (null == dbConnection) ? dbConnection = new DBConnection()
                :dbConnection;
    }
    public Connection getConnection(){
        return connection;

    }
}
