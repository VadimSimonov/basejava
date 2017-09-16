package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.*;
import ru.javawebinar.basejava.sql.SqlHelper;

import java.sql.*;
import java.util.*;

// TODO implement Section (except OrganizationSection)
// TODO Join and split ListSection by `\n`
public class SqlStorage implements Storage {
    public final SqlHelper sqlHelper;
    private Resume rsm;
    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        sqlHelper = new SqlHelper(() -> DriverManager.getConnection(dbUrl, dbUser, dbPassword));
    }

    @Override
    public void clear() {
        sqlHelper.execute("DELETE FROM resume");
    }

    @Override
    public Resume get(String uuid) {
        return sqlHelper.execute("" +
                        "SELECT * FROM resume r " +
                        " LEFT JOIN contact c ON r.uuid = c.resume_uuid "+
                        " LEFT JOIN section s ON r.uuid = s.resume_uuid "+
                        " WHERE r.uuid =? ",
            ps -> {
                ps.setString(1, uuid);
                ResultSet rs = ps.executeQuery();
                if (!rs.next()) {
                    throw new NotExistStorageException(uuid);
                }
                Resume r = new Resume(uuid, rs.getString("full_name"));
                do {
                    addContact(rs, r);
                    addSection(rs, r);
                } while (rs.next());
                return r;
        });
    }

    @Override
    public void update(Resume r) {
        sqlHelper.transactionalExecute(conn -> {
            try (PreparedStatement ps = conn.prepareStatement("UPDATE resume SET full_name = ? WHERE uuid = ?")) {
                ps.setString(1, r.getFullName());
                ps.setString(2, r.getUuid());
                if (ps.executeUpdate() != 1) {
                    throw new NotExistStorageException(r.getUuid());
                }
            }
            deleteContacts(conn, r);
            deleteSection(conn,r);
            insertContact(conn, r);
            insertSection(conn,r);
            return null;
        });
    }

    @Override
    public void save(Resume r) {
        sqlHelper.transactionalExecute(conn -> {
            try (PreparedStatement ps = conn.prepareStatement("INSERT INTO resume (uuid, full_name) VALUES (?,?)")) {
                ps.setString(1, r.getUuid());
                ps.setString(2, r.getFullName());
                ps.execute();
            }
            insertContact(conn, r);
            insertSection(conn,r);
            return null;
        });
    }

    @Override
    public void delete(String uuid) {
        sqlHelper.execute("DELETE FROM resume WHERE uuid=?", ps -> {
            ps.setString(1, uuid);
            if (ps.executeUpdate() == 0) {
                throw new NotExistStorageException(uuid);
            }
            return null;
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        Map<String, Resume> map = new LinkedHashMap<>();
        return sqlHelper.transactionalExecute(conn -> {
            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM resume")) {

                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    String uuid = rs.getString("uuid");
                    rsm = map.get(uuid);
                    if (rsm == null) {
                        rsm = new Resume(uuid, rs.getString("full_name"));
                        map.put(uuid, rsm);
                    }
                }
            }
            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM contact")) {
                String tableName="contact";
                mapPut(ps, map, tableName);

            }
            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM section")) {
                String tableName="section";
                mapPut(ps, map, tableName);
                ps.addBatch();
                ps.executeBatch();
            }
            //  LinkedHashMap result = MapSort(map);
            List<Resume> resumes = new ArrayList<>(map.values());
            Collections.sort(resumes);
            //return new ArrayList<>(map.values());
            return resumes;
        });
    }

        /*
        return sqlHelper.execute("" +
                "   SELECT * FROM resume r\n" +
                "LEFT JOIN contact c ON r.uuid = c.resume_uuid\n" +
                "LEFT JOIN section s ON r.uuid = s.resume_uuid\n"+
                "ORDER BY full_name, uuid", ps -> {
            ResultSet rs = ps.executeQuery();
            Map<String, Resume> map = new LinkedHashMap<>();
            while (rs.next()) {
                String uuid = rs.getString("uuid");
                Resume resume = map.get(uuid);
                if (resume == null) {
                    resume = new Resume(uuid, rs.getString("full_name"));
                    map.put(uuid, resume);
                }
                addContact(rs, resume);
                addSection(rs,resume);
            }
            return new ArrayList<>(map.values());
        });
        */

/*
    private LinkedHashMap MapSort(Map<String, Resume> map) {
        return map.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }
*/
    @Override
    public int size() {
        return sqlHelper.execute("SELECT count(*) FROM resume", st -> {
            ResultSet rs = st.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        });
    }

    private void insertContact(Connection conn, Resume r) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO contact (resume_uuid, type, value) VALUES (?,?,?)")) {
            for (Map.Entry<ContactType, String> e : r.getContacts().entrySet()) {
                ps.setString(1, r.getUuid());
                ps.setString(2, e.getKey().name());
                ps.setString(3, e.getValue());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void insertSection(Connection conn, Resume r) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO section (resume_uuid, sectiontype, name) VALUES (?,?,?)")) {
            for (Map.Entry<SectionType, Section> e : r.getSections().entrySet()) {
                ps.setString(1, r.getUuid());
                String type = e.getKey().name();
                ps.setString(2, type);
                switch (SectionType.valueOf(type)){
                    case PERSONAL:
                    case OBJECTIVE:
                        ps.setString(3, e.getValue().toString());
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        ListSection a = (ListSection) e.getValue();
                        List<String> list = a.getItems();
                        String listString = String.join("\n",list);
                        ps.setString(3, listString);
                        break;
                    case EXPERIENCE:
                    case EDUCATION:
                        break;
                }
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void addSection(ResultSet rs, Resume r) throws SQLException {
        String section = rs.getString("name");
        String type=rs.getString("sectiontype");
        if (type!=null && section!=null) {
            switch (SectionType.valueOf(type)){
                case PERSONAL:
                case OBJECTIVE:
                    r.addSection(SectionType.valueOf(type), new TextSection(section));
                    break;
                case ACHIEVEMENT:
                case QUALIFICATIONS:
                    String[] m = section.split("\n");
                    r.addSection(SectionType.valueOf(type), new ListSection(m));
                    break;
                case EXPERIENCE:
                case EDUCATION:
                    break;
            }
        }
    }

    private void deleteContacts(Connection conn, Resume r) {
        sqlHelper.execute("DELETE  FROM contact WHERE resume_uuid=?", ps -> {
            ps.setString(1, r.getUuid());
            ps.execute();
            return null;
        });
    }
    private void deleteSection(Connection conn, Resume r) {
        sqlHelper.execute("DELETE  FROM section WHERE resume_uuid=?", ps -> {
            ps.setString(1, r.getUuid());
            ps.execute();
            return null;
        });
    }

    private void addContact(ResultSet rs, Resume r) throws SQLException {
        String value = rs.getString("value");
        if (value != null) {
            r.addContact(ContactType.valueOf(rs.getString("type")), value);
        }
    }
    private void mapPut(PreparedStatement ps, Map<String, Resume> map, String tableName) throws SQLException {
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            String uuid = rs.getString("resume_uuid");
            Resume rsm = map.get(uuid);
            if (rsm != null && tableName.equals("contact")) {
                addContact(rs, rsm);
            }else
            if (rsm != null && tableName.equals("section")) {
                addSection(rs, rsm);
            }
        }
    }

}
