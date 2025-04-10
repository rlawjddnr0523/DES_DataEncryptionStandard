package re2.Functions;

import static re2.Functions.F.f;
import static re2.Functions.KeySchedule.generateSubKeys;
import static re2.Functions.Permute.permute;

public class Round {
    public static long round(long PT, long[] subKeys) {
        long IP = permute(Tables.IP, 64, PT); // 초기 치환
        int L = (int) (IP >>> 32);
        int R = (int) (IP & 0xFFFFFFFFL);
        
        for (int i = 0; i < 16; i++) {
            int nextL = R;
            int nextR = L ^ f(R, subKeys[i]);
            L = nextL;
            R = nextR;
        }
        
        // 마지막에 L과 R을 뒤집어서 병합 후 FP
        long preOutput = (((long) R) << 32) | (((long) L) & 0xFFFFFFFFL);
        return permute(Tables.FP, 64, preOutput);
    }
    
    public static void main(String[] args) {
        String temp1 = "0123456789ABCDEF";
        String temp2 = "a1b2c3d4e5f60718";
        long[] subKeys = generateSubKeys(Long.parseUnsignedLong(temp2, 16));
        long result = round(Long.parseUnsignedLong(temp1, 16), subKeys);
        System.out.println(Long.toHexString(result).toUpperCase());
    }
}
