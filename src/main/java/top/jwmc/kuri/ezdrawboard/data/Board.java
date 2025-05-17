package top.jwmc.kuri.ezdrawboard.data;

public record Board(long id,
                    String name,
                    String description,
                    String image,
                    long owner,
                    long time,
                    boolean isPrivate) {
}
