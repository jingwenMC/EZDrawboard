package top.jwmc.kuri.ezdrawboard.client;

import top.jwmc.kuri.ezdrawboard.networking.board.PacketDrawingAdd;

import java.io.IOException;
import java.util.LinkedList;

public class DrawingList extends LinkedList<EnhancedDrawingBoard.Drawing> {
    public static DrawingList INSTANCE;
    public boolean available;
    public DrawingList() {
        super();
        INSTANCE = this;
    }
    public boolean add(boolean receive,EnhancedDrawingBoard.Drawing drawing) {
        if(!receive) {
            try {
                if(Mainapp.ONLINE_MODE)new PacketDrawingAdd(drawing).sendPacket(Mainapp.out);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        boolean ret = super.add(drawing);
        Painter.INSTANCE.redrawAll(this);
        return ret;
    }
    @Override
    public boolean add(EnhancedDrawingBoard.Drawing drawing) {
        return add(false, drawing);
    }
}
