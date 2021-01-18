package com.hm.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.NamespaceDescriptor;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;

import java.io.IOException;
//利用java创建namespace
//可以在hbase shell上查看结果,以确认是否创建成功
public class ConnectionDemo {
    public static void main(String[] args) throws IOException, IOException {
        // 1、获取配置对象
        //会主动加载类库中的jar包里的默认配置属性
        Configuration conf = new Configuration();
        // 2、配置zookeeper的集群信息.必须使用主机名,不能使用ip地址
        conf.set("hbase.zookeeper.quorum", "qianfeng01:2181,qianfeng02:2181,qianfeng03:2181");
        // 3、获取连接对象
        Connection connection = ConnectionFactory.createConnection(conf);
        // 4、获取一个DDL操作的hbase客户端Admin
        Admin admin = connection.getAdmin();
        //5 、创建一个namespace的描述器
        NamespaceDescriptor descriptor = NamespaceDescriptor.create("ns4").build();
        //6. 客户端进行提交创建
        admin.createNamespace(descriptor);
        //7. 关闭操作
        connection.close();
        admin.close();
    }
}