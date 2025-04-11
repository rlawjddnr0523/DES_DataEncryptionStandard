package gen2;

import static gen2.Functions.Decrypt.decrypt;
import static gen2.Functions.Encrypt.encrypt;

public class Main {
    public static void main(String[] args) {
        // 최대 8바이트 (64비트 = 16자리 hex)
        String plainHex = "0123456789ABCDEF";
        String keyHex = "626C796174";
        
        long plaintext = Long.parseUnsignedLong(plainHex, 16);
        long key = Long.parseUnsignedLong(keyHex, 16);
        
        long ciphertext = encrypt(plaintext, key);
        System.out.printf("암호화 결과: %016X\n", ciphertext);
        
        long dec = decrypt(ciphertext, key);
        System.out.printf("복호화 결과: %016X\n", dec);
        
        System.out.println("----------------------");
        long test1 = weakKeyTest(plaintext, key);
        System.out.printf("Cipher Test (hex): %016X\n", test1);
    }
    
    private static long weakKeyTest(long pt, long key) {
        long firstEnc = encrypt(pt, key);
        return encrypt(firstEnc, key);
    }
}
