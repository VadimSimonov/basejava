package ru.javawebinar.basejava.storage;

import org.postgresql.core.ConnectionFactory;
import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.sql.SQLHelper;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqlStorage implements Storage {

    private int colSize=0;
    private ResultSet rs;
    Resume r;
    public final SQLHelper sqlHelper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        this.sqlHelper = new SQLHelper(() -> DriverManager.getConnection(dbUrl, dbUser, dbPassword));
    }

    @Override
    public void clear()  {
        String sql="DELETE FROM resume";
        SQLHelper.transactionExecute(sql, ps -> {
            ps.execute();
            return null;
        });
    }

    @Override
    public Resume get(String uuid) {

        String sql="SELECT * FROM resume r WHERE r.uuid =?";
        SQLHelper.transactionExecute(sql, ps -> {
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

    @Override
    public void update(Resume r) {
        String sql="UPDATE resume SET full_name=? WHERE uuid=?";
        SQLHelper.transactionExecute(sql, ps -> {
                ps.setString(2, r.getUuid());
                ps.setString(1, r.getFullName());
                int a=ps.executeUpdate();
                if (a==0) {
                    throw new NotExistStorageException(r.getUuid());
                }
                return null;
        });
        }


    @Override
    public void save(Resume r) {
        String sql="INSERT INTO resume (uuid, full_name) VALUES (?,?)";
        SQLHelper.transactionExecute(sql, ps -> {
                ps.setString(1, r.getUuid());
                ps.setString(2, r.getFullName());
               try {
                   ps.execute();
               }catch (SQLException e)
               {
                   throw new ExistStorageException(r.getUuid());
               }
        return null;
        });
    }

    @Override
    public void delete(String uuid) {
        String sql = "DELETE FROM resume WHERE uuid=?";
        SQLHelper.transactionExecute(sql, ps -> {
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
        String sql = "SELECT * from resume ORDER BY full_name";
        List<Resume>resumes=new ArrayList<>();
        SQLHelper.transactionExecute(sql, ps -> {
            ResultSet rs = ps.executeQuery();
            while (rs.next())
            {
                Resume resume=new Resume(rs.getString("uuid"),rs.getString("full_name"));
                resumes.add(resume);
            }
            return resumes;
        });
        return resumes;
    }

    @Override
    public int size() {
        String sql="SELECT count(*) FROM resume";
        SQLHelper.transactionExecute(sql, ps -> {
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
