package top.jwmc.kuri.ezdrawboard.data;

public interface DatabaseAccessor {
    User getUserByName(String username);
    User registerUser(String username,String password_hash,String salt)throws IllegalStateException;
    boolean authenticateUser(String str,String password_hash);
    void updateToken(String username,String token);
    long checkTokenExpire(String username,String token);
}
