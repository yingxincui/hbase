//package com.hm.hbasecurd;
//
//import com.hm.hbase.HbaseUtil;
//import org.apache.hadoop.hbase.HColumnDescriptor;
//import org.apache.hadoop.hbase.HTableDescriptor;
//import org.apache.hadoop.hbase.NamespaceDescriptor;
//import org.apache.hadoop.hbase.TableName;
//import org.apache.hadoop.hbase.client.*;
//import org.apache.hadoop.hbase.regionserver.BloomType;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//
//import java.io.IOException;
//import java.util.Map;
//
///*
//1. 创建命名空间ns1，要求添加属性author，company，comment. 属性值自定义
//2. 输出命名空间ns1的属性信息
//3. 在命名空间ns1下创建表emp表，提供以下列族
//	f1:    布隆过滤器row		版本2个	存活时间7天
//	f2:    布隆过滤器rowcol	版本5个	存活时间2天
//	f3:			  属性值都是默认的。
//4. 将emp表中的14行记录，插入5行记录即可。(思考，谁应该作为rowkey)
//5. 查询一个员工的所有单元格
// */
//public class homework {
//    private Admin admin;
//    @Before
//    //在测试方法前执行.就是@Test的代码前执行
//    public void getAdmin() {
//        admin = com.hm.hbase.HbaseUtil.getAdmin();
//    }
//    @After
//    //在测试方法后执行
//    public void close() {
//        HbaseUtil.closeAdmin(admin);
//    }
//
//
//
//    @Test
//    //创建命名空间ns1，要求添加属性author，company，comment. 属性值自定义
//    public  void createNS() throws IOException {
//        NamespaceDescriptor descriptor=NamespaceDescriptor.create("ns11").build();
//        descriptor.setConfiguration("author","libai");
//        descriptor.setConfiguration("company","jd");
//        descriptor.setConfiguration("comment","ok");
//        admin.createNamespace(descriptor);
//    }
//
//    @Test
//    //输出命名空间ns1的属性信息
//    public  void getNSinfo() throws IOException {
//        NamespaceDescriptor ns1=admin.getNamespaceDescriptor("ns11");
//        Map<String,String> map=ns1.getConfiguration();
//        System.out.println(map);
//    }
//
//
//    /*
//    3. 在命名空间ns1下创建表emp表，提供以下列族
//	f1:    布隆过滤器row		版本2个	存活时间7天
//	f2:    布隆过滤器rowcol	版本5个	存活时间2天
//	f3:			  属性值都是默认的。
//     */
//    @Test
//    public  void createTab() throws IOException {
//        TableName tableName=TableName.valueOf("ns11:emp");
//        HTableDescriptor tableDescriptor=new HTableDescriptor(tableName);
//        HColumnDescriptor f1=new HColumnDescriptor("f1");
//        f1.setBloomFilterType(BloomType.ROW);
//        f1.setVersions(1,2);
//        f1.setTimeToLive(604800);
//        tableDescriptor.addFamily(f1);
//
//        HColumnDescriptor f2=new HColumnDescriptor("f2");
//        f2.setBloomFilterType(BloomType.ROWCOL);
//        f2.setVersions(1,5);
//        f2.setTimeToLive(2*24*3600);
//        tableDescriptor.addFamily(f2);
//
//        HColumnDescriptor f3=new HColumnDescriptor("f3");
//        tableDescriptor.addFamily(f3);
//
//        admin.createTable(tableDescriptor);
//    }
//
//    //将emp表中的14行记录，插入5行记录即可。(思考，谁应该作为rowkey)
//    @Test
//    public void putEmpData() throws IOException {
//        Table table= com.hm.hbasecurd.HbaseUtil.getTable("ns11:emp");
//
//        Put put1=new Put("7369".getBytes());
//        put1.addColumn("f1".getBytes(),"ename".getBytes(),"smith".getBytes());
//        put1.addColumn("f1".getBytes(),"ejob".getBytes(),"clerk".getBytes());
//        put1.addColumn("f1".getBytes(),"emgr".getBytes(),"7902".getBytes());
//        put1.addColumn("f1".getBytes(),"ebirth".getBytes(),"1980-02-20".getBytes());
//        put1.addColumn("f1".getBytes(),"esal".getBytes(),"800".getBytes());
//        put1.addColumn("f1".getBytes(),"deptno".getBytes(),"20".getBytes());
//
//        table.put(put1);
//
//
//        Put put2=new Put("7499".getBytes());
//        put2.addColumn("f1".getBytes(),"ename".getBytes(),"allen".getBytes());
//        put2.addColumn("f1".getBytes(),"ejob".getBytes(),"salesman".getBytes());
//        put2.addColumn("f1".getBytes(),"emgr".getBytes(),"7698".getBytes());
//        put2.addColumn("f1".getBytes(),"ebirth".getBytes(),"1981-02-20".getBytes());
//        put2.addColumn("f1".getBytes(),"esal".getBytes(),"1600".getBytes());
//        put2.addColumn("f1".getBytes(),"comm".getBytes(),"300".getBytes());
//        put2.addColumn("f1".getBytes(),"deptno".getBytes(),"30".getBytes());
//
//        table.put(put2);
//
//        Put put3=new Put("7521".getBytes());
//        put3.addColumn("f1".getBytes(),"ename".getBytes(),"ward".getBytes());
//        put3.addColumn("f1".getBytes(),"ejob".getBytes(),"salesman".getBytes());
//        put3.addColumn("f1".getBytes(),"emgr".getBytes(),"7698".getBytes());
//        put3.addColumn("f1".getBytes(),"ebirth".getBytes(),"1981-02-21".getBytes());
//        put3.addColumn("f1".getBytes(),"esal".getBytes(),"1250".getBytes());
//        put3.addColumn("f1".getBytes(),"comm".getBytes(),"500".getBytes());
//        put3.addColumn("f1".getBytes(),"deptno".getBytes(),"30".getBytes());
//
//        table.put(put3);
//
//        Put put4=new Put("7566".getBytes());
//        put4.addColumn("f1".getBytes(),"ename".getBytes(),"jones".getBytes());
//        put4.addColumn("f1".getBytes(),"ejob".getBytes(),"manager".getBytes());
//        put4.addColumn("f1".getBytes(),"emgr".getBytes(),"7839".getBytes());
//        put4.addColumn("f1".getBytes(),"ebirth".getBytes(),"1981-04-02".getBytes());
//        put4.addColumn("f1".getBytes(),"esal".getBytes(),"2975".getBytes());
//        put4.addColumn("f1".getBytes(),"deptno".getBytes(),"20".getBytes());
//
//        table.put(put4);
//
//        Put put5=new Put("7654".getBytes());
//        put5.addColumn("f1".getBytes(),"ename".getBytes(),"martin".getBytes());
//        put5.addColumn("f1".getBytes(),"ejob".getBytes(),"salesman".getBytes());
//        put5.addColumn("f1".getBytes(),"emgr".getBytes(),"7698".getBytes());
//        put5.addColumn("f1".getBytes(),"ebirth".getBytes(),"1981-09-28".getBytes());
//        put5.addColumn("f1".getBytes(),"esal".getBytes(),"125".getBytes());
//        put5.addColumn("f1".getBytes(),"comm".getBytes(),"1400".getBytes());
//        put5.addColumn("f1".getBytes(),"deptno".getBytes(),"30".getBytes());
//
//        table.put(put5);
//    }
//
//    //查询一个员工的所有单元格
//    @Test
//    public void scanEmpData() throws IOException {
//        Table table= com.hm.hbasecurd.HbaseUtil.getTable("ns11:emp");
//        Scan scan=new Scan("7369".getBytes());
//        ResultScanner results=table.getScanner(scan);
//        for(Result result:results){
//            com.hm.hbasecurd.HbaseUtil.printCell(result);
//        }
//    }
//}
