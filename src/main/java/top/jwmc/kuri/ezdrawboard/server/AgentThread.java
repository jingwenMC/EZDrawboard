package top.jwmc.kuri.ezdrawboard.server;

import top.jwmc.kuri.ezdrawboard.data.User;
import top.jwmc.kuri.ezdrawboard.networking.util.PacketBoardTerminate;

import java.io.IOException;
import java.net.Socket;

public class AgentThread extends Thread {
    private final Socket connection;
    private final ServerRouterImpl router;
    private User userInfo;
    private Board board;
    private final Server instance;
    public AgentThread(Socket _connection,Server instance) throws IOException {
        connection = _connection;
        this.instance = instance;
        router = new ServerRouterImpl(connection,this);
        start();
    }
    @Override
    public void run() {
        try {
            router.startHandleRequest();
            if(!connection.isClosed())connection.close();
        }catch (Exception e) {
            System.err.println("Agent thread got exception: "+e);
            System.err.println("Socket INET: "+connection.getInetAddress());
            e.printStackTrace();
        }
        //离线销毁
        if(board != null) {
            if(board.getOwner().equals(this)) {
                for(AgentThread agentThread : board.getUsers()) {
                    if(!agentThread.equals(this)) {
                        try {
                            new PacketBoardTerminate(agentThread).sendPacket(agentThread.getRouter().getDataOutputStream());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                instance.getBoardMap().remove(board.id);
            } else {
                board.getUsers().remove(this);
            }
        }
    }

    public User getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(User userInfo) {
        this.userInfo = userInfo;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public Server getInstance() {
        return instance;
    }

    public ServerRouterImpl getRouter() {
        return router;
    }
}
