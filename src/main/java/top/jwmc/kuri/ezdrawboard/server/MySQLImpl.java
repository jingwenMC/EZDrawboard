package top.jwmc.kuri.ezdrawboard.server;

import top.jwmc.kuri.ezdrawboard.data.DatabaseAccessor;
import top.jwmc.kuri.ezdrawboard.data.User;

public class MySQLImpl implements DatabaseAccessor {
    @Override
    public User getUserByName(String username) {
        return null;
    }

    @Override
    public User registerUser(String username, String password_hash, String salt) throws IllegalStateException {
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
    public long checkTokenExpire(String username, String token) {
        return 0;
    }
}
