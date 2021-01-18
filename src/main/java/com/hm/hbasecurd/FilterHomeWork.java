//package com.hm.hbasecurd;
//
//import org.apache.hadoop.hbase.client.Result;
//import org.apache.hadoop.hbase.client.ResultScanner;
//import org.apache.hadoop.hbase.client.Scan;
//import org.apache.hadoop.hbase.client.Table;
//import org.apache.hadoop.hbase.filter.*;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//
//import java.io.IOException;
//
///**
// * select * from t_user where uname like '%l%' and sal > 2000
// *
// * 使用hbase JAVA API完成以上sql的查询效果
// */
//public class FilterHomeWork {
//    private Table table;
//
//    @Before
//    public void getAdmin() {
//        table = HbaseUtil.getTable("ns12:user");
//    }
//
//    @After
//    public void close() {
//        HbaseUtil.closeTable(table);
//    }
//
//    @Test
//    public void test() throws IOException {
//        RegexStringComparator comparator=new RegexStringComparator("l");
//        FilterList filterList=new FilterList(FilterList.Operator.MUST_PASS_ALL);
//        SingleColumnValueFilter filter1=new SingleColumnValueFilter("f1".getBytes(),"uname".getBytes(), CompareFilter.CompareOp.EQUAL,comparator);
//        BinaryComparator comparator1=new BinaryComparator("2000".getBytes());
//        SingleColumnValueFilter filter2=new SingleColumnValueFilter("f1".getBytes(),"sal".getBytes(), CompareFilter.CompareOp.GREATER,comparator1);
//        filter1.setFilterIfMissing(true);
//        filter2.setFilterIfMissing(true);
//        filterList.addFilter(filter1);
//        filterList.addFilter(filter2);
//        Scan scan=new Scan();
//        scan.setFilter(filterList);
//        ResultScanner resultScanner = table.getScanner(scan);
//        for (Result result : resultScanner) {
//            HbaseUtil.printCell(result);
//        }
//    }
//}
