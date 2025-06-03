package top.jwmc.kuri.ezdrawboard.server;

import top.jwmc.kuri.ezdrawboard.data.DatabaseAccessor;
import top.jwmc.kuri.ezdrawboard.data.User;

import java.sql.*;
import java.util.UUID;

public class MySQLImpl implements DatabaseAccessor {

    private static final String URL = "jdbc:mysql://localhost:3306/ezdrawboard_db";
    private static final String USER = "user";
    private static final String PASSWORD = "password";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    @Override
    public User getUserByName(String username) {
        String sql = "SELECT id, username, passwordHash, salt FROM users WHERE username = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getString("id"),
                            rs.getString("username"),
                            rs.getString("passwordHash"),
                            rs.getString("salt")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error while fetching user", e);
        }
        return null;
    }

    @Override
    public User registerUser(String username, String passwordHash, String salt) throws IllegalStateException {
        if (getUserByName(username) != null) {
            throw new IllegalStateException("Username already exists");
        }

        String id = UUID.randomUUID().toString();
        String sql = "INSERT INTO users (id, username, passwordHash, salt) VALUES (?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, id);
            stmt.setString(2, username);
            stmt.setString(3, passwordHash);
            stmt.setString(4, salt);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                return new User(id, username, passwordHash, salt);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean authenticateUser(String username, String passwordHash) {
        User user = getUserByName(username);
        if (user == null) {
            return false;
        }
        return user.passwordHash().equals(passwordHash);
    }

    @Override
    public void updateToken(String username, String token) {
        long expireTime = System.currentTimeMillis() + 3600000;

        String sql = "INSERT INTO user_tokens (username, token, expire_time) VALUES (?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE token = ?, expire_time = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, token);
            stmt.setLong(3, expireTime);
            stmt.setString(4, token);
            stmt.setLong(5, expireTime);

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Database error while updating token", e);
        }
    }

    @Override
    public long checkTokenExpire(String username, String token) {
        String sql = "SELECT expire_time FROM user_tokens WHERE username = ? AND token = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, token);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    long expireTime = rs.getLong("expire_time");
                    long currentTime = System.currentTimeMillis();
                    return Math.max(0, expireTime - currentTime);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}