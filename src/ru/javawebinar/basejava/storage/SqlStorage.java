package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.sql.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SqlStorage implements Storage {
    private final ConnectionFactory connectionFactory;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        connectionFactory = () -> DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    @Override
    public void clear() {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM resume")) {
            ps.execute();
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    @Override
    public Resume get(String uuid) {
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

    @Override
    public void update(Resume r) {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement("UPDATE resume SET full_name=? WHERE uuid=?")) {
            ps.setString(2, r.getUuid());
            ps.setString(1, r.getFullName());
            ps.execute();
        } catch (SQLException e) {
            throw new StorageException(e);
        }


    }

    @Override
    public void save(Resume r) {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO resume (uuid, full_name) VALUES (?,?)")) {
            ps.setString(1, r.getUuid());
            ps.setString(2, r.getFullName());
            ps.execute();
        } catch (SQLException e) {
            throw new ExistStorageException(r.getFullName());
        }

    }

    @Override
    public void delete(String uuid) {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM resume WHERE uuid=?")) {
            ps.setString(1, uuid);
            ps.execute();
        } catch (SQLException e) {
            throw new NotExistStorageException(uuid);
        }
    }

    @Override
    public List<Resume> getAllSorted() {
        //copy pasta https://alvinalexander.com/blog/post/jdbc/jdbc-preparedstatement-select-like
        List<Resume>resumes=new ArrayList<>();
        try(Connection connection = connectionFactory.getConnection();
            PreparedStatement ps=connection.prepareStatement("SELECT * from resume")){
            ResultSet rs = ps.executeQuery();
            while (rs.next())
            {
                Resume resume=new Resume(rs.getString("uuid"),rs.getString("full_name"));
                resumes.add(resume);
            }
            Collections.sort(resumes);
        } catch (SQLException e) {
            throw new StorageException(e);
        }
        return resumes;
    }

    @Override
    public int size() {
        int colSize=0;
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT count(*) FROM resume")) {
            ResultSet rs=ps.executeQuery();
            while (rs.next())
            {
                colSize=rs.getInt(1);
            }
            return colSize;
        } catch (SQLException e) {
            throw new StorageException(e);
        }

    }
}
