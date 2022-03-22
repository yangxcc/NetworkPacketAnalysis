package code.service;

import code.structure.TLSHandshake;
import code.structure.TLSStruct;
import code.utils.DataUtils;

import java.util.Arrays;

public class TLSParseService {
    public TLSStruct parseTLS(byte[] TLSData) {   // byte类型的数据最大是127，比这个数大的表示不了
        TLSStruct tlsStruct = new TLSStruct();
//        byte[] buff_1 = Arrays.copyOfRange(TLSData, 0, 1);
        int contentType = TLSData[0];
        tlsStruct.setContent_type(contentType);
        byte[] buff_2 = Arrays.copyOfRange(TLSData, 1, 3);
        int version = DataUtils.byteArrayToShort(buff_2);
        tlsStruct.setVersion(version);
        buff_2 = Arrays.copyOfRange(TLSData, 3, 5);
        int tlsLen = DataUtils.byteArrayToShort(DataUtils.reverseArray(buff_2));
        tlsStruct.setLength(tlsLen);
        if (contentType == 22) {
            TLSHandshake tlsHandshake = new TLSHandshake();
            byte[] buff = Arrays.copyOfRange(TLSData, 5, TLSData.length);
            int handShakeType = buff[0];
            tlsHandshake.setType(handShakeType);
            if (handShakeType == 16) {
                System.out.println("Client Key Exchange");
                tlsStruct.setHandshake(tlsHandshake);
                return tlsStruct;
            }
            byte[] buff_3 = Arrays.copyOfRange(buff, 1, 4);
            int len = DataUtils.byteArrayToInt3(DataUtils.reverseArray(buff_3));    // 这里不对，求三位字节
            tlsHandshake.setLen(len);
            buff_2 = Arrays.copyOfRange(buff, 4, 6);
            int ver = DataUtils.byteArrayToShort(buff_2);
            tlsHandshake.setVersion(ver);
            // 32位的随机数
            byte[] buff_32 = Arrays.copyOfRange(buff, 6, 38);
//            for (int i = 0; i < 32; i++) {
//                System.out.printf(buff_32[i] + " ");
//            }
//            System.exit(0);
            StringBuilder random = new StringBuilder();
            for (int i = 0; i < 32; ) {
                byte[] buff_4 = Arrays.copyOfRange(buff_32, i, i + 4);
                i += 4;
                int i1 = DataUtils.byteArrayToInt(buff_4);
                random.append(DataUtils.convertFromIntToHexa(i1));
//                System.out.println(i1);
//                System.out.println(DataUtils.convertFromIntToHexa(i1));
            }

            tlsHandshake.setRandom(random.toString());
            int sessionID = buff[38];
            tlsHandshake.setSessionID(sessionID);
            buff_2 = Arrays.copyOfRange(buff,39, 41);
            int cipherLen = DataUtils.byteArrayToShort(DataUtils.reverseArray(buff_2));
            tlsHandshake.setCipherSuitesLen(cipherLen);
            if (cipherLen % 2 != 0) {
                System.out.println("密码套件解析出错");
                return new TLSStruct();
            }
            String[] cipherSuits = new String[cipherLen / 2];
            int index = 0;
            for (int i = 0; i < cipherLen; i += 2) {
                byte[] buf_2 = new byte[]{buff[41 + i], buff[42 + i]};
                short i1 = DataUtils.byteArrayToShort(buf_2);
                cipherSuits[index++] = DataUtils.convertFromIntToHexa(i1);
//                StringBuilder temp = new StringBuilder();
//                temp.append(buff[41 + i]);
//                temp.append(buff[42 + i]);
//                cipherSuits[index++] = temp.toString();
            }
            tlsHandshake.setCipherSuites(cipherSuits);
            tlsStruct.setHandshake(tlsHandshake);
        } else if (contentType == 23){
            System.out.println("Application Data!");
        } else if (contentType == 21) {
            System.out.println("Alter Message: Encrypted Alert!");
        }

        return tlsStruct;
    }
}
