//package com.hm.day01;
//
//import com.hm.hbasecurd.HbaseUtil;
//import org.apache.hadoop.hbase.Cell;
//import org.apache.hadoop.hbase.CellUtil;
//import org.apache.hadoop.hbase.client.Durability;
//import org.apache.hadoop.hbase.client.Put;
//import org.apache.hadoop.hbase.client.Table;
//import org.apache.hadoop.hbase.coprocessor.BaseRegionObserver;
//import org.apache.hadoop.hbase.coprocessor.ObserverContext;
//import org.apache.hadoop.hbase.coprocessor.RegionCoprocessorEnvironment;
//import org.apache.hadoop.hbase.regionserver.wal.WALEdit;
//import java.io.IOException;
//import java.util.List;
//
///*
//当我们向表hello中插入单元格时，如果name单元格的value值是michael,那么就在另外一个单元格message里面插入hello michael。  列族为base.
//put 'hello','rk1001','base:name','michael'
//                      base:message','hello michael'
// */
//public class HomeWorkC extends BaseRegionObserver {
//
//    @Override
//    public void prePut(ObserverContext<RegionCoprocessorEnvironment> e, Put put, WALEdit edit, Durability durability) throws IOException {
//        //Returns a list of all KeyValue objects with matching column family and qualifier.
//        List<Cell> cells = put.get("base".getBytes(), "name".getBytes());
//        if(cells==null || cells.size()==0){
//            return;
//        }
//        //Returns the element at the specified position in this list.
//        Cell cell=cells.get(0);
//        if("michael".equals(new String(CellUtil.cloneValue(cell)))){
//            Put newput = new Put("rk1001".getBytes());
//            newput.addColumn("base".getBytes(), "message".getBytes(), "hello michael".getBytes());
//            Table table = HbaseUtil.getTable("ns1:hello");
//            table.put(newput);
//            HbaseUtil.closeTable(table);
//
//        }
//    }
//}
