package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.sql.ConnectionFactory;
import ru.javawebinar.basejava.sql.SQLExecute;
import ru.javawebinar.basejava.sql.SQLHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SqlStorage implements Storage {
    private final ConnectionFactory connectionFactory;
    int colSize=0;
    ResultSet rs;
    Resume r;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        connectionFactory = () -> DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    @Override
    public void clear()  {
        String sql="DELETE FROM resume";
        SQLHelper.transactionExecute(sql, connectionFactory, ps -> {
            ps.execute();
            return null;
        });
    }

    @Override
    public Resume get(String uuid) {

        String sql="SELECT * FROM resume r WHERE r.uuid =?";
        SQLHelper.transactionExecute(sql, connectionFactory, ps -> {
            ps.setString(1, uuid);
            rs = ps.executeQuery();
            if (!rs.next()) {
                throw new NotExistStorageException(uuid);
            }
            r=new Resume(uuid, rs.getString("full_name"));
            return r;

        });
        return r;
    }

/*
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM resume r WHERE r.uuid =?")) {
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new NotExistStorageException(uuid);
            }
            return new Resume(uuid, rs.getString("full_name"));
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }
    */

    @Override
    public void update(Resume r) {
        String sql="UPDATE resume SET full_name=? WHERE uuid=?";
        SQLHelper.transactionExecute(sql,connectionFactory, ps -> {
            ps.setString(2, r.getUuid());
            ps.setString(1, r.getFullName());
            ps.execute();
            return null;
        });
        }


    @Override
    public void save(Resume r) {
        String sql="INSERT INTO resume (uuid, full_name) VALUES (?,?)";
        SQLHelper.transactionExecute(sql, connectionFactory, ps -> {
            try {
                ps.setString(1, r.getUuid());
                ps.setString(2, r.getFullName());
                ps.execute();
                return null;
            } catch (SQLException e) {
            throw new ExistStorageException(r.getFullName());
        }
        });
    }

    @Override
    public void delete(String uuid) {
        String sql = "DELETE FROM resume WHERE uuid=?";
        SQLHelper.transactionExecute(sql, connectionFactory, ps -> {
            ps.setString(1, uuid);
            int eu = ps.executeUpdate();
            if (eu == 0) {
                throw new NotExistStorageException(uuid);
            }
            return null;
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        //copy pasta https://alvinalexander.com/blog/post/jdbc/jdbc-preparedstatement-select-like
        String sql = "SELECT * from resume";
        List<Resume>resumes=new ArrayList<>();
        SQLHelper.transactionExecute(sql, connectionFactory, ps -> {
            ResultSet rs = ps.executeQuery();
            while (rs.next())
            {
                Resume resume=new Resume(rs.getString("uuid"),rs.getString("full_name"));
                resumes.add(resume);
            }
            Collections.sort(resumes);
            return resumes;
        });
        return resumes;
    }

    @Override
    public int size() {
        String sql="SELECT count(*) FROM resume";
        SQLHelper.transactionExecute(sql, connectionFactory, ps -> {
            ResultSet rs=ps.executeQuery();
            while (rs.next())
            {
                colSize=rs.getInt(1);
            }
            return colSize;
        });
        return colSize;
    }
}
