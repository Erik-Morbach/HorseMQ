package network;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;

public class InputUtils {
    public static StringBuilder readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        int c;
        while((c = is.read())>0){
            sb.append((char)c);
        }
        return sb;
    }
    public static int readInt(InputStream is) throws IOException {
        int value = 0;
        for(int i = 0; i < 4; i++){
            value = value << 8 | (is.read() & 0xFF);
        }
        return value;
    }

    public static double readDouble(InputStream is) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.put(is.readNBytes(8));
        return buffer.getDouble(0);
    }
}
