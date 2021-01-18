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
//    //向表中插入数据
//    //put 'ns1:emp' 'rk0001' 'f1:ename','zhangsan'
//    @Test
//    public void putData() throws IOException {
//        //
//        Put put = new Put("rk0001".getBytes());
//        //三个参数,分别列粗名,列名,和值
//        //cf name, column name, value .all byte[] type
//        put.addColumn("f1".getBytes(), "ename".getBytes(), "zhangsan".getBytes());
//        put.addColumn("f1".getBytes(), "age".getBytes(), "23".getBytes());
//        put.addColumn("f1".getBytes(), "gender".getBytes(), "f".getBytes());
//        put.addColumn("f1".getBytes(), "salary".getBytes(), "1324".getBytes());
//        //体检到hbase上.
//        table.put(put);
//
//    }
//
//    //批量插入数据 比如1000条数据
//    @Test
//    public void putBatchData() throws IOException {
//        //获取一个集合对象
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
//            //每300提交一次
//            if (puts.size() % 300 == 0) {
//                table.put(puts);
//                //清空集合
//                puts.clear();
//            }
//        }
//        table.put(puts);
//    }
//
//    // get 'ns1:emp' 'rk0001'
//    public void getSingleData() throws IOException {
//        //使用get对象描述一行记录
//        Get get=new Get("rk000001".getBytes());
//        //使用table对象将get发送到hbase上
//        Result result=table.get(get);
//        //遍历结果集
//       //获取结果集的单元格扫描器
//        CellScanner cellScanner= result.cellScanner();
//        //使用迭代器遍历元素 advance判断是否有下一个元素
//        while (cellScanner.advance()){
//            //取出当前单元格
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
//        //查询数据
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
//        //使用Scan来描述要查询的数据的范围
//        //Scan(开始行,结束行)
//        Scan scan=new Scan("rk0000010".getBytes(),"rk0000100".getBytes());
//        //调用tablde的sacn方法
//        ResultScanner resultScanner=table.getScanner(scan);
//        //获取结果扫描器的迭代器类型
//        for (Result result : resultScanner) {
//            HbaseUtil.printCell(result);
//        }
//    }
//
//    /**
//     * 删除: delete 'ns1:emp', 'rk00001','base_info:ename'
//     */
//    public void deleteCell() throws IOException {
//        //使用delete对象描述要删除的制定行的某一个单元格
//        //先指定行号
//        Delete delete=new Delete("rk0000001".getBytes());
//        //再指定要删除的具体列
//        delete.addColumn("f1".getBytes(),"ename".getBytes());
//        delete.addColumn("f1".getBytes(),"age".getBytes());
//        //submit
//        table.delete(delete);
//    }
//
//    //删除整行数据测试
//    @Test
//    public void deleterow() throws IOException {
//        //使用delete对象描述要删除的制定行的某一个单元格
//        //先指定行号
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