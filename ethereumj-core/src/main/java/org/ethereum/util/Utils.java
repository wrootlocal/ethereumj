package org.ethereum.util;

import java.math.BigInteger;
import java.net.URL;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;
import javax.swing.ImageIcon;

import org.spongycastle.util.encoders.DecoderException;
import org.spongycastle.util.encoders.Hex;

public class Utils {

    private static SecureRandom random = new SecureRandom();

    /**
     * @param hexNum should be in form '0x34fabd34....'
     * @return String
     */
    public static String hexStringToDecimalString(String hexNum) {

        boolean match = Pattern.matches("0[xX][0-9a-fA-F]+", hexNum);
        if (!match) throw new Error("The string doesn't conains hex num in form 0x.. : [" + hexNum + "]");

        byte[] numberBytes = Hex.decode(hexNum.substring(2));
        return (new BigInteger(1, numberBytes)).toString();
    }
    
    /** 
     * Return formatted Date String: yyyy.MM.dd HH:mm:ss
     * Based on Unix's time() input in seconds
     * 
     * @param timestamp seconds since start of Unix-time
     * @return String formatted as - yyyy.MM.dd HH:mm:ss
     */
    public static String longToDateTime(long timestamp) {
    	Date date = new Date(timestamp * 1000);
    	DateFormat formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
    	return formatter.format(date);
    }
    
    public static ImageIcon getImageIcon(String resource) {
        URL imageURL = ClassLoader.getSystemResource(resource);
        ImageIcon image = new ImageIcon(imageURL);
        return image;
    }

    static BigInteger _1000_ = new BigInteger("1000");
    public static String getValueShortString(BigInteger number) {
        BigInteger result = number;
        int pow = 0;
        while (result.compareTo(_1000_) == 1 || result.compareTo(_1000_) == 0) {
            result = result.divide(_1000_);
            pow += 3;
        }
        return result.toString() + "·(" + "10^" + pow + ")";
    }
    
    /**
     * Decodes a hex string to address bytes and checks validity 
     * 
     * @param hex - a hex string of the address, e.g., 6c386a4b26f73c802f34673f7248bb118f97424a
     * @return - decode and validated address byte[]
     */
    public static byte[] addressStringToBytes(String hex) {
    	byte[] addr = null;
		try { addr = Hex.decode(hex); }
    	catch(DecoderException addressIsNotValid) { return null; }
		
		if(isValidAddress(addr))
			return addr;
		return null;
    }
    
    public static boolean isValidAddress(byte[] addr) {
    	return addr != null && addr.length == 20;
    }

    /**
     * @param addr length should be 20
     * @return short string represent 1f21c...
     */
    public static String getAddressShortString(byte[] addr) {

        if (!isValidAddress(addr)) throw new Error("not an address");

        String addrShort = Hex.toHexString(addr, 0, 3);

        StringBuffer sb = new StringBuffer();
        sb.append(addrShort);
        sb.append("...");

        return sb.toString();
    }

    public static SecureRandom getRandom() {
        return random;
    }

    public static double JAVA_VERSION = getJavaVersion();
    static double getJavaVersion() {
        String version = System.getProperty("java.version");

        // on android this property equals to 0
        if (version.equals("0")) return 0;

        int pos = 0, count = 0;
        for ( ; pos<version.length() && count < 2; pos ++) {
            if (version.charAt(pos) == '.') count ++;
        }
        return Double.parseDouble (version.substring (0, pos - 1));
    }

	public static StringBuffer getHashlistShort(List<byte[]> blockHashes) {

        StringBuffer sb = new StringBuffer();

        if (blockHashes.isEmpty()) return sb.append("[]");

        String firstHash = Hex.toHexString(blockHashes.get(0));
		String lastHash = Hex.toHexString(blockHashes.get(blockHashes.size()-1));
		return sb.append(" ").append(firstHash).append("...").append(lastHash);
	}

}
