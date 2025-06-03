package top.jwmc.kuri.ezdrawboard.client;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

public class Util {
    public static final String FILE_NAME = "tmp.png";
    public static void byteToFile(final byte[] data) {
        try (FileOutputStream fos = new FileOutputStream(FILE_NAME)) {
            fos.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static byte[] fileToByte() {
        return fileToByte(new File(FILE_NAME));
    }

    public static byte[] fileToByte(File file) {
        try {
            return Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
