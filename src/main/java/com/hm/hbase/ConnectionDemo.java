package com.hm.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.NamespaceDescriptor;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;

import java.io.IOException;
//����java����namespace
//������hbase shell�ϲ鿴���,��ȷ���Ƿ񴴽��ɹ�
public class ConnectionDemo {
    public static void main(String[] args) throws IOException, IOException {
        // 1����ȡ���ö���
        //��������������е�jar�����Ĭ����������
        Configuration conf = new Configuration();
        // 2������zookeeper�ļ�Ⱥ��Ϣ.����ʹ��������,����ʹ��ip��ַ
        conf.set("hbase.zookeeper.quorum", "qianfeng01:2181,qianfeng02:2181,qianfeng03:2181");
        // 3����ȡ���Ӷ���
        Connection connection = ConnectionFactory.createConnection(conf);
        // 4����ȡһ��DDL������hbase�ͻ���Admin
        Admin admin = connection.getAdmin();
        //5 ������һ��namespace��������
        NamespaceDescriptor descriptor = NamespaceDescriptor.create("ns4").build();
        //6. �ͻ��˽����ύ����
        admin.createNamespace(descriptor);
        //7. �رղ���
        connection.close();
        admin.close();
    }
}