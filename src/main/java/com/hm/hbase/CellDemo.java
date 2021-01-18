package com.hm.hbase;

import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.junit.After;
import org.junit.Before;

public class CellDemo {
    private Admin admin;

    @Before
    public void getAdmin() {
        admin = HbaseUtil.getAdmin();
    }

    @After
    public void close() {
        HbaseUtil.closeAdmin(admin);
    }

    public void getCell(){
        Table table= com.hm.hbasecurd.HbaseUtil.getTable("ns1:emp");
        Put newput = new Put("rk000001".getBytes());
        newput.addColumn("base".getBytes(), "message".getBytes(), "hello michael".getBytes());
    }
}
