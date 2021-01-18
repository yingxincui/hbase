//package com.hm.com;
//
//
//import com.hm.hbasecurd.HbaseUtil;
//import org.apache.hadoop.hbase.client.Durability;
//import org.apache.hadoop.hbase.client.Put;
//import org.apache.hadoop.hbase.client.Table;
//import org.apache.hadoop.hbase.coprocessor.BaseRegionObserver;
//import org.apache.hadoop.hbase.coprocessor.ObserverContext;
//import org.apache.hadoop.hbase.coprocessor.RegionCoprocessorEnvironment;
//import org.apache.hadoop.hbase.regionserver.wal.WALEdit;
//
//
//import java.io.IOException;
//
///**
// * 1. �̳�BaseRegionObserver
// * 2. ��дprePut����
// */
//public class Fensi extends BaseRegionObserver {
//    /**
//     *
//     * ��put��������regionʱ���ᱻ�˷����������أ��˷�����put�βξ��Ǳ����ص���put����
//     *
//     *    rowkey          f1:from      f1:to
//     *    С��-����ʦ       С��         ����ʦ
//     *
//     *
//     *    ���ǵ�Ŀ���ǽ���put���󣬽����������ĳ�ȥ ƴ�ɷ�˿������Ҫ������
//     *    rowkey          f1:from
//     *    ����ʦ-С��        С��
//     * @param e
//     * @param put
//     * @param edit
//     * @param durability
//     * @throws IOException
//     */
//    @Override
//    public void prePut(ObserverContext<RegionCoprocessorEnvironment> e, Put put, WALEdit edit, Durability durability) throws IOException {
//        //����Ҫ������rowkey���д��ڣ����ֻ��Ҫ��ȡrowkey����
//        byte[] row = put.getRow();
//        //ת�����ַ���
//        String rowStr = new String(row);   //С��-����ʦ
//        //����������
//        String[] arr = rowStr.split("-");
//
//        //Ϊ��˿��ƴ��rowkey
//        String fensirowkey = arr[1]+"-"+arr[0]; //  "to-����ʦ"
//        //���˿���������,���Ȼ�ȡ��˿���Ӧ��table����
//        Table table = HbaseUtil.getTable("fensi");
//        //ά��һ���µ�put����
//        Put newPut = new Put(fensirowkey.getBytes());
//        newPut.addColumn("f1".getBytes(),"from".getBytes(),arr[0].getBytes());
//        //�ύ����˿����
//        table.put(newPut);
//        HbaseUtil.closeTable(table);
//    }
//}
