package code;

import code.service.PCAPHeaderService;
import code.service.PacketHeaderService;
import code.service.ParsePacketDataService;
import code.structure.GlobalHeader;
import code.structure.PacketHeader;
import code.utils.DataUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class PCAPFileParse {

//    private byte[] data = null;
    // 小端模式
    private boolean isBigEndian = true;


    private PCAPHeaderService pcapHeaderService;              // 处理global header
    private PacketHeaderService packetHeaderService;          // 处理packet header
    private ParsePacketDataService parsePacketDataService;    // 处理packet data

    public PCAPFileParse() {
        pcapHeaderService = new PCAPHeaderService();
        packetHeaderService = new PacketHeaderService();
        parsePacketDataService = new ParsePacketDataService();

//        if (inputFilePath != null) {
//            try {
//                this.data = Files.readAllBytes(Paths.get(inputFilePath));
////                System.out.println("数据文件长度（单位/字节）" + data.length);
////                System.out.println(Arrays.toString(data));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
    }

    public void parsePCAP(File pcapFile) {
        // pcap global header: 24 bytes
        byte[] globalHeaderBuffer = new byte[24];
        // pcap packet header: 16 bytes
        byte[] packetHeaderBuffer = new byte[16];
        byte[] packetDataBuffer;
        byte[] protocolBuffer = new byte[14];

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(pcapFile);
            if (fis.read(globalHeaderBuffer) != 24) {
                System.out.println("The Pcap file is broken!");
                return;
            }
            // 解析global header
            GlobalHeader globalHeader = pcapHeaderService.parseFileHeader(globalHeaderBuffer);
            if (globalHeader.getMagic().equals("D4C3B2A1")) {
                // 小端模式
                isBigEndian = false;
            }
            System.out.println(globalHeader.toString());
//            if (globalHeader.getLinkType() != GlobalHeader.LINK_TYPE_ETHERNET) {
//                System.out.println("Link type is not ethernet!");
//                return;
//            }
//            if (fis.read(packetHeaderBuffer) != 16) {
//                return;
//            }
//            PacketHeader packetHeader = packetHeaderService.parseFilePacketHeader(packetHeaderBuffer, isBigEndian);
//            System.out.println(packetHeader.toString());
            int index = 1;
            while (fis.read(packetHeaderBuffer) > 0) {
                // 解析 Packet Header，一共16个字节
                PacketHeader packetHeader = packetHeaderService.parseFilePacketHeader(packetHeaderBuffer, isBigEndian, index++);
                System.out.println(packetHeader.toString());
                packetDataBuffer = new byte[packetHeader.getCapLen()];
                // 解析数据链路层的头部，共有14个字节

//                if (fis.read(packetDataBuffer) != packetHeader.getCapLen()) {
//                    System.out.println("The Pcap file is broken!");
//                    return;
//                }
                if (fis.read(packetDataBuffer) != packetHeader.getCapLen()) {
                    System.out.println("The Pcap file is broken!");
                    return;
                }

                parsePacketDataService.parsePacketData(packetDataBuffer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
