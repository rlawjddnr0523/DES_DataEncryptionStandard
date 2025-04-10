package gen1.Functions;

import java.math.BigInteger;
import java.util.*;

public class Functions {
    // XOR 연산 함수
    public static String xor(String a, String b) {
        if (a.length() != b.length()) throw new IllegalArgumentException("Error : XOR - length diff");
        
        StringBuilder res = new StringBuilder();
        
        for (int i = 0; i < a.length(); i++) {
            char bitA = a.charAt(i);
            char bitB = b.charAt(i);
            
            res.append(bitA == bitB ? "0" : "1");
        }
        
        return res.toString();
    }
    
    // F 함수
    public static String F(String R, String Ki) {
        String expandedR = E(R);         // E 확장
        String xored = xor(expandedR, Ki); // XOR 수행
        String sBoxed = S(xored); // S-Box 변환
        return SP(sBoxed);               // 단순 치환 후 반환
    }
    
    // PC-1 함수
    public static String[] PC1(String hexKey) {
        // 16진수 입력값을 64비트 이진수로 변환
        long key = Long.parseUnsignedLong(hexKey, 16); // 16진수 → 10진수 변환
        String binary = String.format("%64s", Long.toBinaryString(key)).replace(' ', '0'); // 2진수 변환 후 64비트 맞춤
        
        // 56비트로 변환된 결과
        StringBuilder pc1Result = new StringBuilder();
        for (int j : Table.PC1) {
            int index = j - 1; // 1-based index → 0-based index
            pc1Result.append(binary.charAt(index));
        }
        
        // C와 D로 나누기
        String C = pc1Result.substring(0, 28);
        String D = pc1Result.substring(28, 56);
        
        return new String[]{C, D};
    }
    
    // PC-2
    public static String PC2(String combined) {
        StringBuilder permutedKey = new StringBuilder();
        for (int index : Table.PC2) {
            permutedKey.append(combined.charAt(index - 1)); // 1-based → 0-based 변환
        }
        return permutedKey.toString();
    }
    
    // 비트 좌측 이동 함수
    public static String LShift(String binary, int shift) {
        int length = binary.length();
        shift %= length; // 문자열 길이를 초과하는 경우를 방지
        
        // 왼쪽으로 shift만큼 이동 후, 잘린 부분을 뒤에 붙이기
        return binary.substring(shift) + binary.substring(0, shift);
    }
    
    // E 확장 함수
    public static String E(String R) {
        StringBuilder expanded = new StringBuilder();
        for (int index : Table.E) {
            expanded.append(R.charAt(index - 1)); // 1-based → 0-based 변환
        }
        return expanded.toString();
    }
    
    // SP 함수 (Straight Permutation)
    public static String SP(String input) {
        StringBuilder permuted = new StringBuilder();
        for (int index : Table.SP) {
            permuted.append(input.charAt(index - 1)); // 1-based → 0-based 변환
        }
        return permuted.toString();
    }
    
    // S-box 함수
    public static String S(String input) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            String sixBits = input.substring(i * 6, (i + 1) * 6);
            int row = Integer.parseInt("" + sixBits.charAt(0) + sixBits.charAt(5), 2);
            int col = Integer.parseInt(sixBits.substring(1, 5), 2);
            int value = Table.SBoxs[i][row * 16 + col]; // 2D 배열 활용
            output.append(String.format("%4s", Integer.toBinaryString(value)).replace(' ', '0'));
        }
        return output.toString();
    }
    
    // String -> Binary converter
    public static String textToBinary(String text) {
        StringBuilder binary = new StringBuilder();
        
        for (char c : text.toCharArray()) {
            String bin = Integer.toBinaryString(c); // 문자 → 2진수 변환
            
            // 8비트 맞추기 (앞에 0 채우기)
            while (bin.length() < 8) {
                bin = "0" + bin;
            }
            
            binary.append(bin);
        }
        
        // 64비트 맞추기 (패딩 처리)
        while (binary.length() < 64) {
            binary.append("00000000"); // 공백 패딩 (NULL 문자 '\0' 사용 가능)
        }
        
        return binary.toString();
    }
    
    public static String binaryToText(String binary) {
        StringBuilder text = new StringBuilder();
        
        // 8비트씩 나누어 ASCII 문자로 변환
        for (int i = 0; i < binary.length(); i += 8) {
            String byteStr = binary.substring(i, i + 8); // 8비트 추출
            int asciiValue = Integer.parseInt(byteStr, 2); // 2진수를 10진수로 변환
            
            // NULL(0)은 무시 가능
            if (asciiValue != 0) {
                text.append((char) asciiValue);
            }
        }
        
        return text.toString();
    }
    
    public static String encodeBase64(String binaryString) {
        byte[] bytes = new BigInteger(binaryString, 2).toByteArray();
        return Base64.getEncoder().encodeToString(bytes);
    }
    
    public static String decodeBase64(String base64String) {
        byte[] bytes = Base64.getDecoder().decode(base64String);
        return new BigInteger(1, bytes).toString(2);
    }
}
