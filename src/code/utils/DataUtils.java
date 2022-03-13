package code.utils;

import java.math.BigInteger;
import java.security.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.*;

public class DataUtils {

    public static final String GSTIME="yyyy-MM-dd HH:mm:ss";

    /**
     * Compare 2 byte array with 32 bytes each other
     *
     * @param data
     * @param comparedArray
     * @param isBigEndian   define if data is in big endian or little endian format
     * @return
     */
    public static boolean compare32Bytes(byte[] data, byte[] comparedArray,
                                         boolean isBigEndian) {
        if (data.length == 4 && comparedArray.length == 4) {
            boolean error = false;
            if (isBigEndian) {
                for (int i = 0; i < 4; i++) {
                    if (data[i] != comparedArray[i]) {
                        error = true;
                    }
                }
            } else {
                for (int i = 0; i < 4; i++) {
                    if (data[3 - i] != comparedArray[i]) {
                        error = true;
                    }
                }
            }

            if (!error) {
                return true;
            }
        } else {

        }
        return false;
    }


    public static int byteArrayToInt(byte[] b) {
        return   b[3] & 0xFF |
                (b[2] & 0xFF) << 8 |
                (b[1] & 0xFF) << 16 |
                (b[0] & 0xFF) << 24;
    }



    public static short byteArrayToShort(byte[] b) {
        return (short) ( b[0] & 0xFF |
                         (b[1] & 0xFF) << 8);
//        short s0 = (short) (b[0] & 0xff);// 最低位
//        short s1 = (short) (b[1] & 0xff);
//        s1 <<= 8;
//        return  (short) (s0 | s1);
    }

    /**
     * Convert from int data into String hexadecimal (ex 255 => "0xFF")
     *
     * @param dataTmp data to convert into hexa
     * @return data converted into hexa
     */
    public static String convertFromIntToHexa(int dataTmp) {
//        int dataTmp = data & 0xFF;
        /* Put character in uppercase */
        String value = Integer.toHexString(dataTmp).toUpperCase();
        /* Add 0 if length equal to 1 */
        if (value.length() == 1) {
            value = "0" + value;
        }
        return value;
    }


    public static int decodeHEX(String hexs) {
        BigInteger bigint = new BigInteger(hexs, 16);
        int numb = bigint.intValue();
        return numb;
    }


        /**
         * Convert Little indian byte array to big endian byte array
         *
         * @param data
         * @return
         */
    public static byte[] convertLeToBe(byte[] data) {
        byte[] temp = new byte[data.length];

        for (int i = data.length - 1; i >= 0; i--) {
            temp[data.length - 1 - i] = data[i];
        }
        return temp;
    }

    /**
     * unix时间戳转换成日期格式
     * @param timestamp
     * @return
     */
    public static String unixTimeStampToDate(String timestamp) {
        long time = Long.parseLong(timestamp) * 1000;
        Date date=new Date(time);
        SimpleDateFormat format=new SimpleDateFormat(GSTIME);
        return format.format(date);
    }


    public static byte[] reverseArray(byte[] source) {
        byte[] res = new byte[source.length];
        for (int i = 0; i < source.length; i++) {
            res[i] = source[source.length - i - 1];
        }
        return res;
    }


    public static int byteArray2Int(byte[] array, int length) {
        if (length == 2) {
            return (array[0] & 0xff) * 256 + (array[1] & 0xff);
        } else if (length == 4) {
            int value= 0;
            for (int i = 0; i < 4; i++) {
                int shift= (4 - 1 - i) * 8;
                value +=(array[i] & 0x000000FF) << shift;
            }

            return value;
        }
        return -1;
    }
}
