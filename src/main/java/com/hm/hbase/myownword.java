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
�Լ����д�Ĵ���,����������
 */
public class myownword {
    private Admin admin;

    @Before
    //�ڲ��Է���ǰִ��.����@Test�Ĵ���ǰִ��
    public void getAdmin() {
        admin = HbaseUtil.getAdmin();
    }

    @After
    //�ڲ��Է�����ִ��
    public void close() {
        HbaseUtil.closeAdmin(admin);
    }


    @Test
    public void listNamespace() throws IOException {
        //ͨ���ͻ��ˣ���ȡnamespace������������
        //List available namespace descriptors
        //�ͻ��˵�Ȼ�ɻ�ȡ��Щ
        NamespaceDescriptor[] nss = admin.listNamespaceDescriptors();
        for (NamespaceDescriptor descriptor : nss) {
            //�������������д�ӡ����
            System.out.println(descriptor.getName());
        }
    }

    //�����ز���,���Ժ�hbase shell�����ſ�

    /**
     * �൱��shell��create 'ns1:emp','f1'
     */
    @Test
    public void createTable() throws IOException {
        //������
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
        //get a table httxx object. ʹ��admin������hbase��������,���ر�����Ӧ��table������
        HTableDescriptor hTableDescriptor = admin.getTableDescriptor(TableName.valueOf("ns1:emp"));
        //��ȡcf������
        HColumnDescriptor[] hColumnDescriptors = hTableDescriptor.getColumnFamilies();
        for (HColumnDescriptor cf : hColumnDescriptors) {
            //���ʱ��
            System.out.println(cf.getTimeToLive());
            //�汾����
            System.out.println(cf.getMaxVersions());
            //�д�����
            System.out.println(cf.getNameAsString());
            //���С
            System.out.println(cf.getBlocksize());
            //�Ƿ�֧�ֿ黺��
            System.out.println(cf.isBlockCacheEnabled());
        }
    }


    //append cf in exist table
    //compare to shell ; alter 'ns1:emp','f2'
    //����д�
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
    //ɾ��CF
    //alter
    public void altertabledeletecolumnfamily() throws IOException {
        admin.deleteColumn(TableName.valueOf("ns1:emp"), "f2".getBytes());

    }

    @Test
    //�г����еı�
    public void listAllTable() throws IOException {
        //List all of the names of userspace tables.
        TableName[] tableNames = admin.listTableNames();
        for (TableName tablename : tableNames) {
            System.out.println(tablename.getNameAsString());
        }
    }

    @Test
    //ɾ���� drop 'ns1:emp'
    //�Ƚ��ò���ɾ��
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
