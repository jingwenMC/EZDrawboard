package top.jwmc.kuri.ezdrawboard.data;

public interface DatabaseAccessor {
    Board getBoardByID(long BoardID);
    Board getBoardByOwner(long ownerID);
    boolean isBoardAccessible(long BoardID,long userID);
    User getUserByID(long UserID);
    User getUserByName(String username);
    User getUserByEmail(String email);
    User getUserByStr(String username);
    boolean authenticateUser(String str,String password_hash);
    void updateToken(String username,String token);
    long checkTokenExpire(String token);
    String getTokenUsername(String token);
}
