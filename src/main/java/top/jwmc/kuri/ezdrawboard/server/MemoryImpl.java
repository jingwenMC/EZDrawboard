package top.jwmc.kuri.ezdrawboard.server;

import top.jwmc.kuri.ezdrawboard.data.DatabaseAccessor;
import top.jwmc.kuri.ezdrawboard.data.User;

public class MemoryImpl implements DatabaseAccessor {
    @Override
    public User getUserByName(String username) {
        return new User(1,"admin","123456","123456");
    }

    @Override
    public User registerUser(String username, String password_hash, String salt) throws IllegalStateException {
        return new User(1,"admin","123456","123456");
    }

    @Override
    public boolean authenticateUser(String str, String password_hash) {
        return getUserByName(str).passwordHash().equals(password_hash);
    }

    @Override
    public void updateToken(String username, String token) {
        
    }

    @Override
    public long checkTokenExpire(String username, String token) {
        return 1;
    }
}
