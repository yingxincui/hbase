package com.hm.hbase;

import org.apache.hadoop.hbase.NamespaceDescriptor;
import org.apache.hadoop.hbase.client.Admin;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;

//namespace相关操作

public class NamespaceHandler {
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
    public void createNamespace() throws IOException {
        try {
            //获取namespace描述器对象
            NamespaceDescriptor descriptor = NamespaceDescriptor.create("ns1").build();
            //提交到hbase中，进行创建
            admin.createNamespace(descriptor);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    //列出所有的namespace
    public void listNamespace() throws IOException {
        //通过客户端，获取namespace的描述器数组
        NamespaceDescriptor[] nss = admin.listNamespaceDescriptors();
        for (NamespaceDescriptor descriptor : nss) {
            //遍历描述器进行打印名称
            System.out.println(descriptor.getName());
        }
    }



    @Test
    //修改ns属性
    public void alterNamespace() throws IOException {
        //获取指定namespace的描述器
        NamespaceDescriptor ns2 = admin.getNamespaceDescriptor("ns2");
        //设置属性
        ns2.setConfiguration("author", "michael");
        //进行提交修改
        admin.modifyNamespace(ns2);
    }

    @Test
    //删除指定的namespace
    /*
    相当于drop_namespace 'ns1'
     */
    public void deleteNamespace() throws IOException {
        //删除指定的namespace
        admin.deleteNamespace("ns1");
    }



}