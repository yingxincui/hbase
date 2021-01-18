//package com.hm.hbasecurd;
//
//import org.apache.hadoop.hbase.Cell;
//import org.apache.hadoop.hbase.CellScannable;
//import org.apache.hadoop.hbase.CellScanner;
//import org.apache.hadoop.hbase.CellUtil;
//import org.apache.hadoop.hbase.client.*;
//import org.apache.hadoop.hbase.filter.CompareFilter;
//import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Iterator;
//import java.util.List;
//
//public class TableCurd {
//    private Table table;
//
//    @Before
//    public void getAdmin() {
//        table = HbaseUtil.getTable("ns1:emp");
//    }
//
//    @After
//    public void close() {
//        HbaseUtil.closeTable(table);
//    }
//
//    //����в�������
//    //put 'ns1:emp' 'rk0001' 'f1:ename','zhangsan'
//    @Test
//    public void putData() throws IOException {
//        //
//        Put put = new Put("rk0001".getBytes());
//        //��������,�ֱ��д���,����,��ֵ
//        //cf name, column name, value .all byte[] type
//        put.addColumn("f1".getBytes(), "ename".getBytes(), "zhangsan".getBytes());
//        put.addColumn("f1".getBytes(), "age".getBytes(), "23".getBytes());
//        put.addColumn("f1".getBytes(), "gender".getBytes(), "f".getBytes());
//        put.addColumn("f1".getBytes(), "salary".getBytes(), "1324".getBytes());
//        //��쵽hbase��.
//        table.put(put);
//
//    }
//
//    //������������ ����1000������
//    @Test
//    public void putBatchData() throws IOException {
//        //��ȡһ�����϶���
//        List<Put> puts = new ArrayList<>();
//        for (int i = 2; i < 1010; i++) {
//            String rowkey = "";
//            if (i < 10) {
//                rowkey = "rk00000" + i;
//            } else if (i < 100) {
//                rowkey = "rk0000" + i;
//            } else if (i < 1000) {
//                rowkey = "rk000" + i;
//            } else {
//                rowkey = "rk00" + i;
//            }
//            Put put = new Put(rowkey.getBytes());
//            put.addColumn("f1".getBytes(), "ename".getBytes(), ("zhaoyun" + i).getBytes());
//            put.addColumn("f1".getBytes(), "age".getBytes(), (Math.random() * 100 + "").getBytes());
//            String gender;
//            if ((int) (Math.random() * 2) == 1) {
//                gender = "f";
//            } else {
//                gender = "m";
//            }
//            put.addColumn("f1".getBytes(), "gender".getBytes(), gender.getBytes());
//            put.addColumn("f1".getBytes(), "salary".getBytes(), (Math.random() * 9000 + 1000 + "").getBytes());
//            //
//            puts.add(put);
//            //ÿ300�ύһ��
//            if (puts.size() % 300 == 0) {
//                table.put(puts);
//                //��ռ���
//                puts.clear();
//            }
//        }
//        table.put(puts);
//    }
//
//    // get 'ns1:emp' 'rk0001'
//    public void getSingleData() throws IOException {
//        //ʹ��get��������һ�м�¼
//        Get get=new Get("rk000001".getBytes());
//        //ʹ��table����get���͵�hbase��
//        Result result=table.get(get);
//        //���������
//       //��ȡ������ĵ�Ԫ��ɨ����
//        CellScanner cellScanner= result.cellScanner();
//        //ʹ�õ���������Ԫ�� advance�ж��Ƿ�����һ��Ԫ��
//        while (cellScanner.advance()){
//            //ȡ����ǰ��Ԫ��
//           Cell current= cellScanner.current();
//            System.out.println(CellUtil.cloneRow(current) + "\t" + CellUtil.cloneFamily(current)
//                    + CellUtil.cloneQualifier(current) + CellUtil.cloneValue(current) + "\t");
//        }
//
//
//
//    }
//
//    public void getMultiRowdata() throws IOException {
//        List<Get> gets=new ArrayList<>();
//        gets.add(new Get("rk000001".getBytes()));
//        gets.add(new Get("rk000002".getBytes()));
//        gets.add(new Get("rk000003".getBytes()));
//        //��ѯ����
//       Result[] results= table.get(gets);
//
//
//
//    }
//
//    /**
//     * scan 'ns1:emp'
//     */
//    public void scanData() throws IOException {
//        //ʹ��Scan������Ҫ��ѯ�����ݵķ�Χ
//        //Scan(��ʼ��,������)
//        Scan scan=new Scan("rk0000010".getBytes(),"rk0000100".getBytes());
//        //����tablde��sacn����
//        ResultScanner resultScanner=table.getScanner(scan);
//        //��ȡ���ɨ�����ĵ���������
//        for (Result result : resultScanner) {
//            HbaseUtil.printCell(result);
//        }
//    }
//
//    /**
//     * ɾ��: delete 'ns1:emp', 'rk00001','base_info:ename'
//     */
//    public void deleteCell() throws IOException {
//        //ʹ��delete��������Ҫɾ�����ƶ��е�ĳһ����Ԫ��
//        //��ָ���к�
//        Delete delete=new Delete("rk0000001".getBytes());
//        //��ָ��Ҫɾ���ľ�����
//        delete.addColumn("f1".getBytes(),"ename".getBytes());
//        delete.addColumn("f1".getBytes(),"age".getBytes());
//        //submit
//        table.delete(delete);
//    }
//
//    //ɾ���������ݲ���
//    @Test
//    public void deleterow() throws IOException {
//        //ʹ��delete��������Ҫɾ�����ƶ��е�ĳһ����Ԫ��
//        //��ָ���к�
//        Delete delete=new Delete("rk0000002".getBytes());
//        //submit
//        table.delete(delete);
//    }
//
//
//
//
//
//
//
//
//
//
//}