package ru.javawebinar.basejava.sql;

import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.exception.StorageException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SQLHelper {
    private ConnectionFactory connectionFactory;

    public SQLHelper(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }
/*
    public SQLHelper(String dbUrl, String dbUser, String dbPassword) {
        connectionFactory = () -> DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }
*/


    public void sqlexecute(String sql, String uuid) throws SQLException {
        Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, uuid);
            int eu=ps.executeUpdate();
            if (eu==0) {
                throw new NotExistStorageException(uuid);
            }

    }


    public static void sqlexecute(String sql, ConnectionFactory connectionFactory) {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.execute();
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }
}
