package toqe.adventofcode;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Day04 {
    private final MessageDigest md5MessageDigest = MessageDigest.getInstance("MD5");

    public Day04() throws NoSuchAlgorithmException {
    }

    public void run() throws Exception {
        assert getMd5("abcdef", 609043).startsWith("00000");
        assert getMd5("pqrstuv", 1048970).startsWith("00000");

        var input = InputFileHelper.readInput(4);

        // part 1
        for (var i = 0L; i < Long.MAX_VALUE; i++) {
            if (getMd5(input, i).startsWith("00000")) {
                System.out.println("number " + i);
                break;
            }
        }

        // part 2
        for (var i = 0L; i < Long.MAX_VALUE; i++) {
            if (getMd5(input, i).startsWith("000000")) {
                System.out.println("number " + i);
                break;
            }
        }
    }

    public String getMd5(String secret, long number) throws Exception {
        byte[] bytes = (secret + number).getBytes("UTF-8");
        byte[] md5Bytes = this.md5MessageDigest.digest(bytes);
        String md5String = bytesToHex(md5Bytes);
        return md5String;
    }

    // https://stackoverflow.com/questions/9655181/java-convert-a-byte-array-to-a-hex-string
    private static final byte[] HEX_ARRAY = "0123456789ABCDEF".getBytes(StandardCharsets.US_ASCII);

    public static String bytesToHex(byte[] bytes) {
        byte[] hexChars = new byte[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars, StandardCharsets.UTF_8);
    }
}
