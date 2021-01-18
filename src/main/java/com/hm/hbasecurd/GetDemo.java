//package com.hm.hbasecurd;
//
//import org.apache.hadoop.hbase.Cell;
//import org.apache.hadoop.hbase.CellScanner;
//import org.apache.hadoop.hbase.CellUtil;
//import org.apache.hadoop.hbase.KeyValue;
//import org.apache.hadoop.hbase.client.Get;
//import org.apache.hadoop.hbase.client.Result;
//import org.apache.hadoop.hbase.client.Table;
//import org.apache.hadoop.hbase.util.Bytes;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//
//import java.io.IOException;
//
//public class GetDemo {
//
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
//
//    /**
//     * ���л�ȡÿ��RPC����ֵ����һ��Get�����е����ݣ���ΪGet�����ʼ��ʱ��Ҫ�����м�����˿������Ϊһ��Get����ʹ���һ�С�
//     * һ���п��԰�������дػ��߶���е���Ϣ
//     *
//     * @throws IOException
//     */
//    @Test
//    //����get��ѯ����
//    public void testget() throws IOException {
//        Get get = new Get("rk000010".getBytes());
//        System.out.println(get);
//        //��δ�geg�����л�ȡ����
//        //Extracts certain cells from a given row.
//        Result result = table.get(get);
//        CellScanner cellScanner = result.cellScanner();
//        while (cellScanner.advance()) {
//
//            Cell cell = cellScanner.current();
//            System.out.println(new String(CellUtil.cloneRow(cell)) + "\t" + new String(CellUtil.cloneValue(cell)));
//        }
//    }
//
//
//    @Test
//    //����get��ѯ����
//    public void testget1() throws IOException {
//        Get get = new Get("rk000010".getBytes());
//
//        //��δ�geg�����л�ȡ����
//        //Extracts certain cells from a given row.
//        /**
//         * ��1��Result�����ڲ�ѯ�õ��Ľ����ÿһ�����ݻᱻ��Ϊһ��Result���󣬽����ݴ��뵽һ��Resultʵ���С�
//         * ��������Ҫ��ȡһ������ʱ����Ҫ��ȡ�����������ڵ�Result���󼴿ɡ��ö����ڲ���װ��һ��KeyValue �������顣
//         * ��0.98.4��ǰ�ı��ࡣresult���ṩ�� raw() ����ȥ��ȡ����result�����е�KeyValue���顣
//         * ��0.98.4�Ժ����ṩ��һ���µĽں� rowCells() ������ȡKeyValue���󣬲������ص���KeyValue ����������
//         */
//        Result result = table.get(get);
//        System.out.println(get.getMaxVersions());
//        //Return an cells of a Result as an array of KeyValues WARNING do not use, expensive.
//        KeyValue[] kvs = result.raw();
//        System.out.println(kvs.length);//4
//        for (KeyValue kv : kvs) {
//            System.out.println(Bytes.toString(kv.getRow()));
//            System.out.println(Bytes.toString(kv.getValue()));
//        }
//    }
//
//}
