package top.jwmc.kuri.ezdrawboard.server;

import java.util.concurrent.CopyOnWriteArraySet;

public class Board {
    public final String id;
    public final String name;
    public final String description;
    private final AgentThread owner;
    private final CopyOnWriteArraySet<AgentThread> users = new CopyOnWriteArraySet<>();
    public Board(String id, String name, String description, AgentThread owner) {
        this.id = id;
        this.owner = owner;
        this.name = name;
        this.description = description;
        users.add(owner);
    }

    public AgentThread getOwner() {
        return owner;
    }

    public CopyOnWriteArraySet<AgentThread> getUsers() {
        return users;
    }
}
