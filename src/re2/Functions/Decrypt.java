package re2.Functions;

import static re2.Functions.F.f;
import static re2.Functions.KeySchedule.generateSubKeys;
import static re2.Functions.Permute.permute;
import static re2.Functions.Tables.FP;

public class Decrypt {
    public static long decrypt(long ciphertext, long key) {
        long[] subKeys = generateSubKeys(key);
        
        // 초기 순열 (Initial Permutation)
        long IP = permute(Tables.IP, 64, ciphertext);
        int L = (int) (IP >>> 32);       // 상위 32비트 → L
        int R = (int) IP;                // 하위 32비트 → R
        
        // 16라운드 역순 Feistel 구조
        for (int i = 15; i >= 0; i--) {
            int temp = R;
            R = L ^ f(R, subKeys[i]);   // R' = L ⊕ f(R, K)
            L = temp;                   // L' = 이전 R
            
//            System.out.printf("Round %2d: L = %08X, R = %08X\n", 16 - i, L, R);
        }
        
        // 마지막에 L과 R을 그대로 합침 (스왑 없이)
        long preoutput = (((long) R) << 32) | (L & 0xFFFFFFFFL);
        
        // 역순열 (Final Permutation)
        return permute(FP, 64, preoutput);
    }
}
