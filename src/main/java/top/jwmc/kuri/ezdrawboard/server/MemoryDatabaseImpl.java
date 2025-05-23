package top.jwmc.kuri.ezdrawboard.server;

import top.jwmc.kuri.ezdrawboard.data.Board;
import top.jwmc.kuri.ezdrawboard.data.DatabaseAccessor;
import top.jwmc.kuri.ezdrawboard.data.User;

public class MemoryDatabaseImpl implements DatabaseAccessor {
    @Override
    public Board getBoardByID(long BoardID) {
        return null;
    }

    @Override
    public Board getBoardByOwner(long ownerID) {
        return null;
    }

    @Override
    public boolean isBoardAccessible(long BoardID, long userID) {
        return false;
    }

    @Override
    public User getUserByID(long UserID) {
        return null;
    }

    @Override
    public User getUserByName(String username) {
        return null;
    }

    @Override
    public User getUserByEmail(String email) {
        return null;
    }

    @Override
    public User getUserByStr(String username) {
        return null;
    }

    @Override
    public boolean authenticateUser(String str, String password_hash) {
        return false;
    }

    @Override
    public void updateToken(String username, String token) {

    }

    @Override
    public long checkTokenExpire(String token) {
        return 0;
    }

    @Override
    public String getTokenUsername(String token) {
        return "";
    }
}
