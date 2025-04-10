package re2.Functions;

import static re2.Functions.Permute.permute;
import static re2.Functions.Tables.*;

public class F {
    public static int f(int R, long subKey) {
        long expanded = permute(E, 48, R & 0xFFFFFFFFL);
        long xored = expanded ^ subKey;
        
        int output = 0;
        for (int i = 0; i < 8; i++) {
            int sixBits = (int) ((xored >>> (42 - i * 6)) & 0x3F);
            int row = ((sixBits & 0x20) >> 4) | (sixBits & 0x01);
            int col = (sixBits >> 1) & 0x0F;
            output = (output << 4) | SBoxs[i][row][col];
        }
        
        return (int) permute(SP, 32, output);
    }
}
