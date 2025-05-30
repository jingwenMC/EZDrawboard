package top.jwmc.kuri.ezdrawboard.data;

public record User(long id, String name, String passwordHash, String salt) {

}
