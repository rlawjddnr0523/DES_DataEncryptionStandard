package re2.Functions;

public class Permute {
    public static long permute(byte[] table, int nBits, long input) {
        long output = 0;
        for (int i = 0; i < table.length; i++) {
            int bitPos = nBits - table[i];
            long bit = (input >>> bitPos) & 1L;
            output = (output << 1) | bit;
        }
        return output;
    }
}
