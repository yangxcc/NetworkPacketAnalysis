package code.service;

import code.structure.DataLinkLayer;
import code.utils.DataUtils;

import java.util.Arrays;

public class DataLinkLayerService {
    // 解析数据链路层,一共14个字节
    /*
     *     |  6-bytes dis | 6-bytes sr | 2-bytes protocol type |
     *     |---------------------- 14bytes  -------------------|
     *     IPv4 中type为采用 0x0800
     */
    public DataLinkLayer parseDataLinkLayer(byte[] protocolBuffer) {
        DataLinkLayer dataLinkLayer = new DataLinkLayer();
//        byte[] protocolBuffer = Arrays.copyOfRange(data, 40, 54);

        byte[] buff_6 = new byte[6];    // 6字节的缓冲区
        byte[] buff_2 = new byte[2];    // 2字节的缓冲区

        int offset = 0;
        for (int i = 0; i < 6; i++) {
            buff_6[i] = protocolBuffer[i + offset];
        }
        offset += 6;
        int source = DataUtils.byteArrayToInt(buff_6);
        dataLinkLayer.setSourceMac(source);

        for (int i = 0; i < 4; i++) {
            buff_6[i] = protocolBuffer[i + offset];
        }
        offset += 6;
        int dst = DataUtils.byteArrayToInt(buff_6);
        dataLinkLayer.setDstMac(dst);

        for (int i = 0; i < 2; i++) {
            buff_2[1 - i] = protocolBuffer[i + offset];
        }
        int protocol = DataUtils.byteArrayToShort(buff_2);    // 2048表示的就是ipv4
        dataLinkLayer.setProtocol(protocol);
        return dataLinkLayer;
    }
}
