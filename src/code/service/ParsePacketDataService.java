package code.service;

import code.structure.DataLinkLayer;
import code.structure.IPHeader;
import code.structure.TCPHeader;

import java.util.Arrays;

public class ParsePacketDataService {
    public void parsePacketData(byte[] packetDataBuffer) {
        // 仅仅处理以太网的帧
        byte[] framHeaderBuffer = Arrays.copyOfRange(packetDataBuffer, 0, 14);
        DataLinkLayerService dataLinkLayerService = new DataLinkLayerService();
        DataLinkLayer dataLinkLayer = dataLinkLayerService.parseDataLinkLayer(framHeaderBuffer);

        if (dataLinkLayer.getProtocol() != 2048) {
            System.out.println("This packet is not IP packet!");
            return;
        }
        System.out.println(dataLinkLayer.toString());

        int ipHeaderLen = (packetDataBuffer[14] - 64 ) * 4;
        byte[] ipHeaderBuffer = Arrays.copyOfRange(packetDataBuffer, 14, 14 + ipHeaderLen);
        IPLayerService ipLayerService = new IPLayerService();
        IPHeader ipHeader = ipLayerService.parseIPLayer(ipHeaderBuffer);
        if (ipHeader.getProtocol() != 6) {
            System.out.println("This packet is not TCP segment");
            return;
        }
        System.out.println(ipHeader.toString());

        // 数据偏移位于TCP字段第13个字节（0开始），占高4位，单位是4字节
        int tcpHeaderLen  = ((packetDataBuffer[14+ipHeaderLen+12] & 0xf0) >> 4) * 4;
        byte[] tcpHeaderBuffer = Arrays.copyOfRange(packetDataBuffer, 14 + ipHeaderLen, 14 + ipHeaderLen + tcpHeaderLen);
        TCPLayerService tcpLayerService = new TCPLayerService();
        TCPHeader tcpHeader = tcpLayerService.parseTCPLayer(tcpHeaderBuffer);
        System.out.println(tcpHeader.toString());
    }
}
