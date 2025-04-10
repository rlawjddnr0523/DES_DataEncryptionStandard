package re2.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static re2.Functions.Decrypt.decrypt;
import static re2.Functions.Encrypt.encrypt;

import org.junit.jupiter.api.Test;

public class InspectTest {
    @Test
    void algorithmIntegrity() {
        String[] PTS = {
                "0123456789ABCDEF",
                "1111111111111111",
                "0000000000000000",
                "0000000000000000",
                "FFFFFFFFFFFFFFFF",
        };
        String[] KEYS = {
                "133457799BBCDFF1",
                "0123456789ABCDEF",
                "8001010101010101",
                "0101010101010101",
                "FEDCBA9876543210",
        };
        long[] expectedVals = {
                0x85E813540F0AB405L,
                0xF40379AB9E0EC533L,
                0x95A8D72813DAA94DL,
                0x8CA64DE9C1B123A7L,
                0x00FCB3E7E05ED6C3L,
        };
        for (int i = 0; i < PTS.length; i++) {
            long plainHex = Long.parseUnsignedLong(PTS[i], 16);
            long keyHex = Long.parseUnsignedLong(KEYS[i], 16);
            long res = encrypt(plainHex, keyHex); // ❗복호화 제거
            
            // Debugging Code
            System.out.printf("Case %d\n", i + 1);
            System.out.printf("Plain : %016X\n", plainHex);
            System.out.printf("Key   : %016X\n", keyHex);
            System.out.printf("Cipher: %016X\n", res);
            System.out.printf("Expected: %016X\n", expectedVals[i]);
            
            assertEquals(expectedVals[i], res, "암호화 실패 at case " + (i + 1));
        }
        System.out.println("암호화 무결성 테스트에 통과했습니다.");
    }
    
    @Test
    void encryptTestTmp() {
        String PT = "0123456789ABCDEF";
        String KEY = "133457799BBCDFF1";
        long expected = 0x85E813540F0AB405L;
        
        long plainHex = Long.parseUnsignedLong(PT, 16);
        long keyHex = Long.parseUnsignedLong(KEY, 16);
        long res = encrypt(plainHex, keyHex); // ❗복호화 제거
        
        assertEquals(expected, res, "암호화 실패 at this case" );
        
        System.out.println("암호화 무결성 테스트에 통과했습니다.");
    }
    
    @Test
    void WeakKeyTest() {
        String PT = "0123456789ABCDEF";
        String[] KEY = {"0101010101010101", "FEFEFEFEFEFEFEFE", "E0E0E0E0F1F1F1F1", "1F1F1F1F0E0E0E0E"};
        
        long plainHex = Long.parseUnsignedLong(PT, 16);
        long expected = 0x0123456789ABCDEFL;
        
        for (int i = 0; i < KEY.length; i++) {
            long keyHex = Long.parseUnsignedLong(KEY[i], 16);
            long res = encrypt(encrypt(plainHex, keyHex), keyHex);
            System.out.printf("Case"+(i+1)+" : %016x\n", res);
            assertEquals(expected, encrypt(encrypt(plainHex, keyHex), keyHex), "취약키 테스트에 실패 했습니다.");
        }System.out.println("취약키 테스트에 통과했습니다.");
    }
}
