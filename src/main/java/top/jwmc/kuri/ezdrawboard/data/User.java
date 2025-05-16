package top.jwmc.kuri.ezdrawboard.data;

public record User(long id, String name, String email, String passwordHash, String salt, boolean online) {

}
