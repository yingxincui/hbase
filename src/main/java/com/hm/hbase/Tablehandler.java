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
// * table操作相关案例学习
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
//    //创建一个table
//    public void createTable() throws IOException, IOException {
//        //获取TableName对象，指定要创建的表名
//        TableName tableName = TableName.valueOf("ns1:students");
//        //新建一个表描述器
//        HTableDescriptor descriptor = new HTableDescriptor(tableName);
//        //新建一个列族描述器
//        HColumnDescriptor columnDescriptor = new HColumnDescriptor("f1");
//        //设置列族的布隆过滤器为列类型
//        columnDescriptor.setBloomFilterType(BloomType.ROWCOL);
//        //设置列族支持缓存
//        columnDescriptor.setInMemory(true);
//        //设置列族内的单元格支持3个版本
//        columnDescriptor.setVersions(1, 3);
//        //设置列族的单元格过期时间为1天
//        columnDescriptor.setTimeToLive(24 * 60 * 60);
//        //将列族描述器添加到表描述器中, 可以添加多个列族描述器
//        descriptor.addFamily(columnDescriptor);
//        //提交到Hbase，进行创建
//        admin.createTable(descriptor);
//    }
//
//
//    @Test
//    //修改table的列族属性信息
//    public void alterTable2() throws IOException {
//        //指定表名
//        TableName tableName = TableName.valueOf("ns1:students");
//        //获取表描述器
//        HTableDescriptor tableDescriptor = admin.getTableDescriptor(tableName);
//        //获取指定的列族的描述器
//        HColumnDescriptor columnDescriptor = tableDescriptor.getFamily(Bytes.toBytes("f1"));
//        //修改属性
//        columnDescriptor.setBloomFilterType(BloomType.ROWCOL);
//        columnDescriptor.setVersions(1, 2);
//        columnDescriptor.setTimeToLive(24 * 60 * 60);
//        //提交到Hbase
//        admin.modifyColumn(tableName, columnDescriptor);
//    }
//
//    //6.2.4 向table中添加新的列族
//
////    @Test
////    public void alterTable2() throws IOException {
////        TableName tableName = TableName.valueOf("ns1:students");
////        HColumnDescriptor columnDescriptor = new HColumnDescriptor("f2");
////        columnDescriptor.setBloomFilterType(BloomType.ROWCOL);
////        columnDescriptor.setVersions(1, 2);
////        columnDescriptor.setTimeToLive(24*60*60); // 秒为单位
////        admin.addColumn(tableName, columnDescriptor);
////    }
//
//
//    //  6.2.5 获取table的描述信息
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
//    //6.2.6 删除table
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