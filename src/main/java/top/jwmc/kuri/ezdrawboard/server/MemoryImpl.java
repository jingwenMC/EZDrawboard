package top.jwmc.kuri.ezdrawboard.server;

import top.jwmc.kuri.ezdrawboard.data.DatabaseAccessor;
import top.jwmc.kuri.ezdrawboard.data.User;

public class MemoryImpl implements DatabaseAccessor {
    @Override
    public User getUserByName(String username) {
        if(username.equals("admin"))return new User("admin","admin",Util.getSHA256Str("123456","admin"),"admin");
        if(username.equals("2"))return new User("2","2",Util.getSHA256Str("123456","2"),"2");
        else return null;
    }

    @Override
    public User registerUser(String username, String password_hash, String salt) throws IllegalStateException {
        return new User("admin","admin",Util.getSHA256Str("123456","admin"),"admin");
    }

    @Override
    public boolean authenticateUser(String str, String password_hash) {
        if(getUserByName(str)==null)return false;
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
