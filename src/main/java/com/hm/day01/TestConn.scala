package com.hm.day01

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hbase.NamespaceDescriptor
import org.apache.hadoop.hbase.client.{Admin, Connection, ConnectionFactory}

object TestConn {
  private val conf = new Configuration()
  conf.set("hbase.zookeeper.quorum", "qianfeng01:2181,qianfeng02:2181,qianfeng03:2181")
  private val connection: Connection = ConnectionFactory.createConnection(conf)
  private val admin: Admin = connection.getAdmin
  private val descriptor: NamespaceDescriptor = NamespaceDescriptor.create("csdn").build()
  admin.createNamespace(descriptor)
  connection.close()
  admin.close()

}
