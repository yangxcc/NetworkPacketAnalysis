package code.service;

import code.structure.TLSHandshake;
import code.structure.TLSStruct;
import code.utils.DataUtils;

import javax.xml.crypto.Data;
import java.util.Arrays;

public class TLSParseService {
    public TLSStruct parseTLS(byte[] TLSData) {
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
            int len = DataUtils.byteArray2Int(DataUtils.reverseArray(buff_3), 3);
            tlsHandshake.setLen(len);
            buff_2 = Arrays.copyOfRange(buff, 4, 6);
            int ver = DataUtils.byteArrayToShort(buff_2);
            tlsHandshake.setVersion(ver);
            // 32位的随机数
            byte[] buff_32 = Arrays.copyOfRange(buff, 6, 38);
            String random = DataUtils.byteArrayToString(buff_32);
            tlsHandshake.setRandom(random);
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
                StringBuilder temp = new StringBuilder();
                temp.append(buff[41 + i]);
                temp.append(buff[42 + i]);
                cipherSuits[index++] = temp.toString();
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
