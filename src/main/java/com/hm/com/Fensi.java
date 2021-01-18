//package com.hm.com;
//
//
//import com.hm.hbasecurd.HbaseUtil;
//import org.apache.hadoop.hbase.client.Durability;
//import org.apache.hadoop.hbase.client.Put;
//import org.apache.hadoop.hbase.client.Table;
//import org.apache.hadoop.hbase.coprocessor.BaseRegionObserver;
//import org.apache.hadoop.hbase.coprocessor.ObserverContext;
//import org.apache.hadoop.hbase.coprocessor.RegionCoprocessorEnvironment;
//import org.apache.hadoop.hbase.regionserver.wal.WALEdit;
//
//
//import java.io.IOException;
//
///**
// * 1. 继承BaseRegionObserver
// * 2. 重写prePut方法
// */
//public class Fensi extends BaseRegionObserver {
//    /**
//     *
//     * 当put操作经过region时，会被此方法进行拦截，此方法的put形参就是被拦截到的put对象
//     *
//     *    rowkey          f1:from      f1:to
//     *    小李-苍老师       小李         苍老师
//     *
//     *
//     *    我们的目的是解析put对象，将解析出来的出去 拼成粉丝表所需要的数据
//     *    rowkey          f1:from
//     *    苍老师-小李        小李
//     * @param e
//     * @param put
//     * @param edit
//     * @param durability
//     * @throws IOException
//     */
//    @Override
//    public void prePut(ObserverContext<RegionCoprocessorEnvironment> e, Put put, WALEdit edit, Durability durability) throws IOException {
//        //所需要的数据rowkey就有存在，因此只需要获取rowkey即可
//        byte[] row = put.getRow();
//        //转换成字符串
//        String rowStr = new String(row);   //小李-苍老师
//        //解析成数组
//        String[] arr = rowStr.split("-");
//
//        //为粉丝表拼接rowkey
//        String fensirowkey = arr[1]+"-"+arr[0]; //  "to-苍老师"
//        //向粉丝表插入数据,首先获取粉丝表对应的table对象
//        Table table = HbaseUtil.getTable("fensi");
//        //维护一个新的put对象
//        Put newPut = new Put(fensirowkey.getBytes());
//        newPut.addColumn("f1".getBytes(),"from".getBytes(),arr[0].getBytes());
//        //提交到粉丝表中
//        table.put(newPut);
//        HbaseUtil.closeTable(table);
//    }
//}
