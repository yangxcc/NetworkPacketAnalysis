package code.test;

import code.structure.*;
import code.PcapDecoder;
import code.utils.DataUtils;
import org.junit.Test;

import java.io.*;

public class TestGlobalHeader {

    @Test
    public void testReadFile() throws IOException {
        String filePath = "E:\\idea-workspace\\network\\src\\file\\Line2.pcap";
        PcapDecoder pcapDecoder = new PcapDecoder(filePath);
        GlobalHeader globalHeader = pcapDecoder.parseFileHeader();
        // D4C3B2A1 小端模式
        System.out.println("小端模式：" + globalHeader.getMagic());
//        System.out.println(globalHeader.getMagic());

        System.out.println("文件的主版本号：" + DataUtils.convertFromIntToHexa(globalHeader.getMajor()));

        System.out.println("文件的次版本号：" + DataUtils.convertFromIntToHexa(globalHeader.getMinor()));

        System.out.println("当地标准时间，如果是GMT则全为0：" + globalHeader.getThisZone());

        System.out.println("时间戳精度：" + globalHeader.getSigFigs());

        System.out.println("最大存储长度：" + globalHeader.getSnapLen());

        System.out.println("链路类型：" + DataUtils.convertFromIntToHexa(globalHeader.getLinkType()));
    }


    @Test
    public void testPacketHeader() {
        String filePath = "E:\\idea-workspace\\network\\src\\file\\Line2.pcap";
        PcapDecoder pcapDecoder = new PcapDecoder(filePath);
        PacketHeader packetHeader = pcapDecoder.parseFilePacketHeader();

        System.out.println(packetHeader.toString());
        //
        System.out.println(DataUtils.unixTimeStampToDate(String.valueOf(packetHeader.getTimestampHigh())));
//        System.out.println(DataUtils.unixTimeStampToDate(packetHeader.getTimestampLow()));
//        System.out.println(DataUtils.unixTimeStampToDate(String.valueOf(packetHeader.getTimestampLow())));
    }

    @Test
    public void testDataLinkedLayer() {
        String filePath = "E:\\idea-workspace\\network\\src\\file\\Line2.pcap";
        PcapDecoder pcapDecoder = new PcapDecoder(filePath);
        DataLinkLayer dataLinkLayer = pcapDecoder.parseDataLinkLayer();

        System.out.println(dataLinkLayer.toString());
        System.out.println(DataUtils.convertFromIntToHexa(dataLinkLayer.getSourceMac()));
        System.out.println(DataUtils.convertFromIntToHexa(dataLinkLayer.getDstMac()));
        System.out.println("***********");
    }


    @Test
    public void testIPLayer() {
        String filePath = "E:\\idea-workspace\\network\\src\\file\\Line2.pcap";
        PcapDecoder pcapDecoder = new PcapDecoder(filePath);
        IPHeader ipHeader = pcapDecoder.parseIPLayer();
        System.out.println(ipHeader.toString());
    }


    @Test
    public void testTCPLayer() {
        String filePath = "E:\\idea-workspace\\network\\src\\file\\Line2.pcap";
        PcapDecoder pcapDecoder = new PcapDecoder(filePath);
        TCPHeader tcpHeader = pcapDecoder.parseTCPLayer();
        System.out.println(tcpHeader.toString());
        System.out.println(DataUtils.unixTimeStampToDate(String.valueOf(tcpHeader.getTcpHeaderOptions().getTimeStamp())));
    }
}
