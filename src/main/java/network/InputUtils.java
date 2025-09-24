package network;

import java.io.IOException;
import java.io.InputStream;

public class InputUtils {
    public static StringBuilder readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        int c;
        while((c = is.read())>0){
            sb.append((char)c);
        }
        return sb;
    }
}
