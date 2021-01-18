package com.hm.hbase;

import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.NamespaceDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.regionserver.BloomType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/*
自己随便写的代码,仅供测试用
 */
public class myownword {
    private Admin admin;

    @Before
    //在测试方法前执行.就是@Test的代码前执行
    public void getAdmin() {
        admin = HbaseUtil.getAdmin();
    }

    @After
    //在测试方法后执行
    public void close() {
        HbaseUtil.closeAdmin(admin);
    }


    @Test
    public void listNamespace() throws IOException {
        //通过客户端，获取namespace的描述器数组
        //List available namespace descriptors
        //客户端当然可获取这些
        NamespaceDescriptor[] nss = admin.listNamespaceDescriptors();
        for (NamespaceDescriptor descriptor : nss) {
            //遍历描述器进行打印名称
            System.out.println(descriptor.getName());
        }
    }

    //表格相关操作,可以和hbase shell对照着看

    /**
     * 相当于shell的create 'ns1:emp','f1'
     */
    @Test
    public void createTable() throws IOException {
        //传表名
        TableName tableName = TableName.valueOf("ns1:emp");
        //create table
        HTableDescriptor hTableDescriptor = new HTableDescriptor(tableName);
        //create cf
        HColumnDescriptor hColumnDescriptor = new HColumnDescriptor("f1");
        hColumnDescriptor.setBloomFilterType(BloomType.ROWCOL);
        //set f1 set time to 7 days
        //Time-to-live of cell contents, in seconds.
        hColumnDescriptor.setTimeToLive(3600 * 24 * 7);
        //set cf versions;
        //Set minimum and maximum versions to keep
        hColumnDescriptor.setVersions(1, 5);
        //link table with cf
        //Adds a column family. For the updating purpose please use modifyFamily(HColumnDescriptor) instead.
        hTableDescriptor.addFamily(hColumnDescriptor);
        //commit task
        //Creates a new table. Synchronous operation.
        admin.createTable(hTableDescriptor);

    }

    @Test
    public void desciribletable() throws IOException {
        //get a table httxx object. 使用admin对象向hbase发送请求,返回表名对应的table描述器
        HTableDescriptor hTableDescriptor = admin.getTableDescriptor(TableName.valueOf("ns1:emp"));
        //获取cf描述器
        HColumnDescriptor[] hColumnDescriptors = hTableDescriptor.getColumnFamilies();
        for (HColumnDescriptor cf : hColumnDescriptors) {
            //存活时间
            System.out.println(cf.getTimeToLive());
            //版本数量
            System.out.println(cf.getMaxVersions());
            //列簇名称
            System.out.println(cf.getNameAsString());
            //块大小
            System.out.println(cf.getBlocksize());
            //是否支持块缓存
            System.out.println(cf.isBlockCacheEnabled());
        }
    }


    //append cf in exist table
    //compare to shell ; alter 'ns1:emp','f2'
    //添加列簇
    @Test
    public void altertableappendColumnFamily() throws IOException {
//       TableName tableName=TableName.valueOf("ns1:emp");
//       HTableDescriptor hTableDescriptor=new HTableDescriptor(tableName);
//       HColumnDescriptor hColumnDescriptor=new HColumnDescriptor("f2");
//       hTableDescriptor.addFamily(hColumnDescriptor);
//       admin.modifyTable(tableName,hTableDescriptor);
//
        //another method
        TableName tableName = TableName.valueOf("ns1:emp");
        HColumnDescriptor columnDescriptor = new HColumnDescriptor("f2");
        admin.addColumn(tableName, columnDescriptor);

    }

    @Test
    public void alterTableApend() throws IOException {
        //
        HColumnDescriptor columnDescriptor = new HColumnDescriptor("f1");
        columnDescriptor.setVersions(1, 3);
        columnDescriptor.setBloomFilterType(BloomType.ROW);
        //Modify an existing column family on a table. Asynchronous operation.
        admin.modifyColumn(TableName.valueOf("ns1:emp"), columnDescriptor);

    }

    @Test
    //删除CF
    //alter
    public void altertabledeletecolumnfamily() throws IOException {
        admin.deleteColumn(TableName.valueOf("ns1:emp"), "f2".getBytes());

    }

    @Test
    //列出所有的表
    public void listAllTable() throws IOException {
        //List all of the names of userspace tables.
        TableName[] tableNames = admin.listTableNames();
        for (TableName tablename : tableNames) {
            System.out.println(tablename.getNameAsString());
        }
    }

    @Test
    //删除表 drop 'ns1:emp'
    //先禁用才能删除
    public void droptable() throws IOException {
        TableName t = TableName.valueOf("ns1:emp");
        if (admin.tableExists(t)) {
            if (admin.isTableEnabled(t)) {
                admin.disableTable(t);
            }

            admin.deleteTable(t);
        }
    }


}
