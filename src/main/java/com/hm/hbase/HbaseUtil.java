package com.hm.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import java.io.IOException;

/**
 * hbase�ĵ������ӿ��Ա�Table��Adminʵ���������Ӵ�����һ���������Ĳ�����
 * ����ʵ�����̰߳�ȫ�ģ��ͻ��˿��Դ���һ�Σ����Ӳ��������ͬ���̡߳�
 * Table��Admin���������Ķ��Ҳ����̰߳�ȫ�ġ�
 * ͨ������£�ÿ���ͻ���Ӧ�ó���ĵ�������ʵ����������ÿ���߳̽���ȡ���Լ��ı�ʵ����������̳߳�Table��Admin���Ƽ�ʹ�á�
 * һ��Ӧ�ã����̣���Ӧ��һ��connection��ÿ��Ӧ������߳�ͨ������conection��getTable������connectionά�����̳߳�����tableʵ����
 * ���ٷ���˵�������ַ�ʽ��õ�table���̰߳�ȫ�ġ�
 * ÿ��table��д֮��Ӧ�ð�table close�����������̽�����ʱ��Ű�connection close����
 */

public class HbaseUtil {
    private static Connection conn;
    static {
        try {
            // 1����ȡ���ö���
            Configuration conf = new Configuration();
            // 2������zookeeper�Ĳ���
            conf.set("hbase.zookeeper.quorum", "qianfeng01:2181,qianfeng02:2181,qianfeng03:2181");
            // 3����ȡ���Ӷ���
            conn = ConnectionFactory.createConnection(conf);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
    /** ��ȡDDL�����Ŀͻ���Admin */
    public static Admin getAdmin() {
        Admin admin = null;
        try {
            admin = conn.getAdmin();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return admin;
    }
    /** �رտͻ���Admin */
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