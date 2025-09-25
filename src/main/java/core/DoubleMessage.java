package core;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DoubleMessage {
    List<Double> values;
    int accesses;

    public static DoubleMessage from(ByteBuffer buffer) {
        DoubleMessage response = new DoubleMessage();
        response.accesses = 0;
        response.values = new ArrayList<>();
        DoubleBuffer doubleBuffer = buffer.asDoubleBuffer();
        doubleBuffer.position(0);
        for(int i=0;i<doubleBuffer.limit();i++){
            response.values.add(doubleBuffer.get());
        }
        return response;
    }
}
