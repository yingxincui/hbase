package com.hm.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import java.io.IOException;

/**
 * hbase的单个连接可以被Table和Admin实例共享，连接创建是一个重量级的操作。
 * 连接实现是线程安全的，客户端可以创建一次，连接并分享给不同的线程。
 * Table和Admin是轻量级的而且不是线程安全的。
 * 通常情况下，每个客户端应用程序的单个连接实例化，并且每个线程将获取它自己的表实例。缓存或线程池Table和Admin不推荐使用。
 * 一个应用（进程）对应着一个connection，每个应用里的线程通过调用conection的getTable方法从connection维护的线程池里获得table实例，
 * 按官方的说法，这种方式获得的table是线程安全的。
 * 每次table读写之后应该把table close掉，整个进程结束的时候才把connection close掉。
 */

public class HbaseUtil {
    private static Connection conn;
    static {
        try {
            // 1、获取配置对象
            Configuration conf = new Configuration();
            // 2、配置zookeeper的参数
            conf.set("hbase.zookeeper.quorum", "qianfeng01:2181,qianfeng02:2181,qianfeng03:2181");
            // 3、获取连接对象
            conn = ConnectionFactory.createConnection(conf);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
    /** 获取DDL操作的客户端Admin */
    public static Admin getAdmin() {
        Admin admin = null;
        try {
            admin = conn.getAdmin();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return admin;
    }
    /** 关闭客户端Admin */
    public static void closeAdmin(Admin admin) {
        try {
            if (admin != null) {
                admin.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}