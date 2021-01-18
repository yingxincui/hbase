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
//     * 单行获取每次RPC请求值发送一个Get对象中的数据，因为Get对象初始化时需要输入行键，因此可以理解为一个Get对象就代表一行。
//     * 一行中可以包含多个列簇或者多个列等信息
//     *
//     * @throws IOException
//     */
//    @Test
//    //利用get查询数据
//    public void testget() throws IOException {
//        Get get = new Get("rk000010".getBytes());
//        System.out.println(get);
//        //如何从geg对象中获取数据
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
//    //利用get查询数据
//    public void testget1() throws IOException {
//        Get get = new Get("rk000010".getBytes());
//
//        //如何从geg对象中获取数据
//        //Extracts certain cells from a given row.
//        /**
//         * （1）Result对象，在查询得到的结果，每一行数据会被作为一个Result对象，将数据存入到一个Result实例中。
//         * 当我们需要获取一行数据时则需要获取该行数据所在的Result对象即可。该对象内部封装了一个KeyValue 对象数组。
//         * 在0.98.4以前的本班。result类提供了 raw() 方法去获取整个result对象中的KeyValue数组。
//         * 在0.98.4以后，则提供了一个新的节后： rowCells() 方法获取KeyValue对象，不过返回的是KeyValue 对象父类引用
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
