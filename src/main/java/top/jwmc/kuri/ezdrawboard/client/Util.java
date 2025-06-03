package top.jwmc.kuri.ezdrawboard.client;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

public class Util {
    public static String FILE_NAME = "tmp.png";
    public void byteToFile(final byte[] data) {
        try (FileOutputStream fos = new FileOutputStream(FILE_NAME)) {
            fos.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public byte[] fileToByte() {
        try {
            File file = new File(FILE_NAME);
            return Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
