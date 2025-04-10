package re2.Functions;

import static re2.Functions.F.f;
import static re2.Functions.KeySchedule.generateSubKeys;
import static re2.Functions.Permute.permute;
import static re2.Functions.Tables.FP;

public class Encrypt {
    public static long encrypt(long plaintext, long key) {
        long[] subKeys = generateSubKeys(key);
        
        long IP = permute(Tables.IP, 64, plaintext);
        int L = (int) (IP >>> 32);
        int R = (int) IP;
        
        for (int i = 0; i < 16; i++) {
            int temp = R;
            R = L ^ f(R, subKeys[i]);
            L = temp;
            
//            System.out.printf("Round %2d: L = %08X, R = %08X\n", i + 1, L, R);
        }
        
        long preoutput = (((long) R) << 32) | (L & 0xFFFFFFFFL);
        return permute(FP, 64, preoutput);
    }
}
