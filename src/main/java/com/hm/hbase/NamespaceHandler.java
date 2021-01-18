package com.hm.hbase;

import org.apache.hadoop.hbase.NamespaceDescriptor;
import org.apache.hadoop.hbase.client.Admin;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;

//namespace��ز���

public class NamespaceHandler {
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
    public void createNamespace() throws IOException {
        try {
            //��ȡnamespace����������
            NamespaceDescriptor descriptor = NamespaceDescriptor.create("ns1").build();
            //�ύ��hbase�У����д���
            admin.createNamespace(descriptor);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    //�г����е�namespace
    public void listNamespace() throws IOException {
        //ͨ���ͻ��ˣ���ȡnamespace������������
        NamespaceDescriptor[] nss = admin.listNamespaceDescriptors();
        for (NamespaceDescriptor descriptor : nss) {
            //�������������д�ӡ����
            System.out.println(descriptor.getName());
        }
    }



    @Test
    //�޸�ns����
    public void alterNamespace() throws IOException {
        //��ȡָ��namespace��������
        NamespaceDescriptor ns2 = admin.getNamespaceDescriptor("ns2");
        //��������
        ns2.setConfiguration("author", "michael");
        //�����ύ�޸�
        admin.modifyNamespace(ns2);
    }

    @Test
    //ɾ��ָ����namespace
    /*
    �൱��drop_namespace 'ns1'
     */
    public void deleteNamespace() throws IOException {
        //ɾ��ָ����namespace
        admin.deleteNamespace("ns1");
    }



}