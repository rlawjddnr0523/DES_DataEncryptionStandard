package re2.Functions;

import static re2.Functions.Permute.permute;
import static re2.Functions.Tables.*;

public class KeySchedule {
    public static long[] generateSubKeys(long key) {
        long permutedKey = permute(PC1, 56, key);
        
        int C = (int) (permutedKey >>> 28);
        int D = (int) (permutedKey & 0x0FFFFFFF);
        
        long[] subKeys = new long[16];
        for (int i = 0; i < 16; i++) {
            C = ((C << SHIFT_SCHEDULE[i]) | (C >>> (28 - SHIFT_SCHEDULE[i]))) & 0x0FFFFFFF;
            D = ((D << SHIFT_SCHEDULE[i]) | (D >>> (28 - SHIFT_SCHEDULE[i]))) & 0x0FFFFFFF;
            
            long combined = (((long) C) << 28) | D;
            subKeys[i] = permute(PC2, 48, combined);
        }
        return subKeys;
    }
}
