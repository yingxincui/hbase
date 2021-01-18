//package com.hm.hbase;
//
//import org.apache.hadoop.hbase.HColumnDescriptor;
//import org.apache.hadoop.hbase.HTableDescriptor;
//import org.apache.hadoop.hbase.TableName;
//import org.apache.hadoop.hbase.TableNotFoundException;
//import org.apache.hadoop.hbase.client.Admin;
//import org.apache.hadoop.hbase.regionserver.BloomType;
//import org.apache.hadoop.hbase.regionserver.HRegion;
//import org.apache.hadoop.hbase.regionserver.HStore;
//import org.apache.hadoop.hbase.util.Bytes;
//import org.apache.hadoop.mapred.MapTask;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//
//import java.io.IOException;
//
///**
// * table������ذ���ѧϰ
// */
//public class Tablehandler {
//    private Admin admin;
//
//    @Before
//    public void getAdmin() {
//        admin = HbaseUtil.getAdmin();
//    }
//
//    @After
//    public void close() {
//        HbaseUtil.closeAdmin(admin);
//    }
//
//    HStore
//
//
//
//
//    //����һ��table
//    public void createTable() throws IOException, IOException {
//        //��ȡTableName����ָ��Ҫ�����ı���
//        TableName tableName = TableName.valueOf("ns1:students");
//        //�½�һ����������
//        HTableDescriptor descriptor = new HTableDescriptor(tableName);
//        //�½�һ������������
//        HColumnDescriptor columnDescriptor = new HColumnDescriptor("f1");
//        //��������Ĳ�¡������Ϊ������
//        columnDescriptor.setBloomFilterType(BloomType.ROWCOL);
//        //��������֧�ֻ���
//        columnDescriptor.setInMemory(true);
//        //���������ڵĵ�Ԫ��֧��3���汾
//        columnDescriptor.setVersions(1, 3);
//        //��������ĵ�Ԫ�����ʱ��Ϊ1��
//        columnDescriptor.setTimeToLive(24 * 60 * 60);
//        //��������������ӵ�����������, ������Ӷ������������
//        descriptor.addFamily(columnDescriptor);
//        //�ύ��Hbase�����д���
//        admin.createTable(descriptor);
//    }
//
//
//    @Test
//    //�޸�table������������Ϣ
//    public void alterTable2() throws IOException {
//        //ָ������
//        TableName tableName = TableName.valueOf("ns1:students");
//        //��ȡ��������
//        HTableDescriptor tableDescriptor = admin.getTableDescriptor(tableName);
//        //��ȡָ���������������
//        HColumnDescriptor columnDescriptor = tableDescriptor.getFamily(Bytes.toBytes("f1"));
//        //�޸�����
//        columnDescriptor.setBloomFilterType(BloomType.ROWCOL);
//        columnDescriptor.setVersions(1, 2);
//        columnDescriptor.setTimeToLive(24 * 60 * 60);
//        //�ύ��Hbase
//        admin.modifyColumn(tableName, columnDescriptor);
//    }
//
//    //6.2.4 ��table������µ�����
//
////    @Test
////    public void alterTable2() throws IOException {
////        TableName tableName = TableName.valueOf("ns1:students");
////        HColumnDescriptor columnDescriptor = new HColumnDescriptor("f2");
////        columnDescriptor.setBloomFilterType(BloomType.ROWCOL);
////        columnDescriptor.setVersions(1, 2);
////        columnDescriptor.setTimeToLive(24*60*60); // ��Ϊ��λ
////        admin.addColumn(tableName, columnDescriptor);
////    }
//
//
//    //  6.2.5 ��ȡtable��������Ϣ
//    @Test
//    public void describeTable() throws IOException, Exception {
//        TableName tableName = TableName.valueOf("ns1:students");
//        HTableDescriptor descriptor = admin.getTableDescriptor(tableName);
//        HColumnDescriptor[] hcs = descriptor.getColumnFamilies();
//        for (HColumnDescriptor hc : hcs) {
//            System.out.print("NAME=>" + hc.getNameAsString() + "\t");
//            System.out.print("BLOOMFILTER=>" + hc.getBloomFilterType() + "\t");
//            System.out.print("VERSIONS=>" + hc.getMaxVersions() + "\t");
//            System.out.print("TTL=>" + hc.getTimeToLive() + "\t");
//            System.out.println("BLOCKSIZE=>" + hc.getBlocksize());
//        }
//    }
//
//
//    //6.2.6 ɾ��table
//
//    @Test
//    public void dropTable() throws TableNotFoundException, IOException {
//        TableName tableName = TableName.valueOf("ns1:students");
//        if (admin.tableExists(tableName)) {
//            if (!admin.isTableDisabled(tableName)) {
//                admin.disableTable(tableName);
//            }
//            admin.deleteTable(tableName);
//        }
//    }
//}