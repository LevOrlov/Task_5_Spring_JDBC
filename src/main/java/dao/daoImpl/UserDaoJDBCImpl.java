package dao.daoImpl;

import dao.UserDao;
import model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import java.sql.*;
import java.util.List;

@Repository
@Transactional
public class UserDaoJDBCImpl implements UserDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override

    public void addUser(User application) {
        String sqlreq = "insert into table_name(name,login,password) values (?, ?, ?)";
        jdbcTemplate.update(sqlreq, application.getName(), application.getLogin(),
                application.getPassword());
    }

    @Override
    public void deleteUser(int userId) {
        String sqlreq = "delete from table_name where id=?";
        jdbcTemplate.update(sqlreq, userId);
    }

    @Override
    public void updateUser(User application) {
        String sqlreq = "update table_name set name=?, login=?, password=? where id=?";
        jdbcTemplate.update(sqlreq, application.getName(), application.getLogin(),
                application.getPassword(), application.getId());
    }

    @Override
    public List<User> getAllUsers() {

        String sqlreq = "select * from table_name";
        List<User> listContact = jdbcTemplate.query(sqlreq, new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                return UserDaoJDBCImpl.this.getUser(rs);
            }
        });

        return listContact;

    }

    @Override
    public User getUserById(int userId) {
        String sqlreq = "select * from table_name where id=" + userId;
        return getUser(sqlreq);

    }

    @Override
    public User getUserByLogin(String login) {
        String sqlreq = "select * from table_name where login=?";
        return getUser(sqlreq);

    }

    private User getUser(String sqlreq) {
        return jdbcTemplate.query(sqlreq, new ResultSetExtractor<User>() {
            @Override
            public User extractData(ResultSet rs) throws SQLException,
                    DataAccessException {
                if (rs.next()) {
                    return UserDaoJDBCImpl.this.getUser(rs);
                }

                return null;
            }

        });
    }

    private User getUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setName(rs.getString("name"));
        user.setLogin(rs.getString("login"));
        user.setPassword(rs.getString("password"));
        user.setRole(rs.getString("role"));
        return user;
    }
}
