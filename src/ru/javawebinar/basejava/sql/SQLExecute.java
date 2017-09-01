package ru.javawebinar.basejava.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface SQLExecute<T> {
    T SQLExecute(PreparedStatement ps) throws SQLException;

}
