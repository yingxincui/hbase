package com.hm.hbasecurd;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellScanner;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;

import java.io.IOException;

public class HbaseUtil {
    private static Connection conn;
    static {
        try {
            Configuration conf = new Configuration();
            conf.set("hbase.zookeeper.quorum", "qianfeng01:2181,qianfeng02:2181,qianfeng03:2181");
            conn = ConnectionFactory.createConnection(conf);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
//    public static Admin getAdmin() {
//        //  略
//    }
//    public static void closeAdmin(Admin admin) {
//        //  略
//    }
    /** 获取Table对象的方法*/
    public static Table getTable(String name) {
        Table table = null;
        try {
            // 将表名转成TableName对象
            TableName tableName = TableName.valueOf(name);
            // 通过连接对象，获取Table对象
            table = conn.getTable(tableName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return table;
    }
    /**  关闭表对象 */
    public static void closeTable(Table table) {
        try {
            if (table != null) {
                table.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void printCell(Result result) throws IOException {
        CellScanner cellScanner= result.cellScanner();
        //使用迭代器遍历元素 advance判断是否有下一个元素
        while (cellScanner.advance()){
            //取出当前单元格
            Cell current= cellScanner.current();
            System.out.printf("\n"+new String(CellUtil.cloneRow(current)) +
                    "\t"+new String(CellUtil.cloneFamily(current))+
                    "\t"+ new String(CellUtil.cloneQualifier(current))+
                    "\t"+new String( CellUtil.cloneValue(current)));
        }
    }


}