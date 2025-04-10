package gen1.Functions;

import static gen1.Functions.Functions.E;
import static gen1.Functions.Functions.LShift;
import static gen1.Functions.Functions.PC1;
import static gen1.Functions.Functions.PC2;
import static gen1.Functions.Functions.S;
import static gen1.Functions.Functions.SP;
import static gen1.Functions.Functions.xor;

public class Encrypt {
    
    public static String EncryptFunction(String PT, String KEY) {
        // 1. Initial Permutation (IP)
        String permutedText = InitialPermutation(PT);
        
        // 2. L0, R0 분리 (32비트씩)
        String L = permutedText.substring(0, 32);
        String R = permutedText.substring(32, 64);
        
        // 3. 16라운드 수행
        for (int i = 1; i <= 16; i++) {
            String Ki = K(KEY, i); // 라운드 키 생성
            
            System.out.println(i + "라운드 키 : " + Ki);
            
            String FResult = F(R, Ki); // F 함수 적용
            String newR = xor(L, FResult); // Ri = Li-1 ⊕ F(Ri-1, Ki)
            L = R; // Li = Ri-1
            R = newR; // Ri = 계산된 값
        }
        
        // 4. 최종 라운드 결과 조합 후 순열 적용 (FP)
        String preOutput = R + L; // R16L16 순서로 결합
        return FinalPermutation(preOutput);
    }
    
    // -------------------- 필요한 함수들 --------------------
    
    // 초기 순열 (IP)
    public static String InitialPermutation(String input) {
        int[] IP_TABLE = {
                58, 50, 42, 34, 26, 18, 10, 2,
                60, 52, 44, 36, 28, 20, 12, 4,
                62, 54, 46, 38, 30, 22, 14, 6,
                64, 56, 48, 40, 32, 24, 16, 8,
                57, 49, 41, 33, 25, 17, 9, 1,
                59, 51, 43, 35, 27, 19, 11, 3,
                61, 53, 45, 37, 29, 21, 13, 5,
                63, 55, 47, 39, 31, 23, 15, 7
        };
        return permute(input, IP_TABLE);
    }
    
    // 최종 순열 (FP)
    public static String FinalPermutation(String input) {
        int[] FP_TABLE = {
                40, 8, 48, 16, 56, 24, 64, 32,
                39, 7, 47, 15, 55, 23, 63, 31,
                38, 6, 46, 14, 54, 22, 62, 30,
                37, 5, 45, 13, 53, 21, 61, 29,
                36, 4, 44, 12, 52, 20, 60, 28,
                35, 3, 43, 11, 51, 19, 59, 27,
                34, 2, 42, 10, 50, 18, 58, 26,
                33, 1, 41, 9, 49, 17, 57, 25
        };
        return permute(input, FP_TABLE);
    }
    
    // PC1 + LShift + PC2 적용한 키 생성 함수
    public static String K(String hexKey, int round) {
        String[] CD = PC1(hexKey); // PC-1 수행
        int shiftAmount = (round == 1 || round == 2 || round == 9 || round == 16) ? 1 : 2;
        
        // LShift 적용
        CD[0] = LShift(CD[0], shiftAmount);
        CD[1] = LShift(CD[1], shiftAmount);
        
        return PC2(CD[0] + CD[1]); // PC-2 수행 후 반환
    }
    
    // F 함수
    public static String F(String R, String K) {
        String expandedR = E(R); // 확장
        String xored = xor(expandedR, K); // XOR
        String substituted = S(xored); // S-Box 적용
        return SP(substituted); // Straight Permutation 적용
    }
    
    // 퍼뮤테이션 공통 함수
    private static String permute(String input, int[] table) {
        StringBuilder output = new StringBuilder();
        for (int index : table) {
            output.append(input.charAt(index - 1));
        }
        return output.toString();
    }
}
