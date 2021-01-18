package com.hm.hbasecurd;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.filter.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

//����hbase��������һЩʾ������
//û�й������Ļ�,�����㾫ȷ��ѯ
/*
����Filter������RPC��ѯ������Filter�ַ�������RegionServer����һ���������ˣ�Server-side���Ĺ�����������Ҳ���Խ������紫���ѹ��
��Ҫ���һ�����˵Ĳ�����������Ҫ����������
һ���ǳ���Ĳ�������Hbase�ṩ��ö�����͵ı�������ʾ��Щ����Ĳ�������LESS/LESS_OR_EQUAL/EQUAL/NOT_EUQAL�ȣ�
����һ�����Ǿ���ıȽ�����Comparator�����������ıȽ��߼��������������ֽڼ��ıȽϡ��ַ������ıȽϵȡ�
�������������������ǾͿ��������Ķ���ɸѡ����������������
 */
public class FilterDemo {
    private Table table;

    @Before
    public void getAdmin() {
        table = HbaseUtil.getTable("ns1:emp");
    }

    @After
    public void close() {
        HbaseUtil.closeTable(table);
    }

    @Test
    /*
    select * from emp where ename="zhangsan"
     */
    //����ֵ������ singleColumnValueFilter
    public void testSCVF() throws IOException {
        SingleColumnValueFilter filter = new SingleColumnValueFilter(
                "f1".getBytes(), "ename".getBytes(), CompareFilter.CompareOp.EQUAL, "zhaoyun100".getBytes());
        filter.setFilterIfMissing(true);
        Scan scan = new Scan();
        //�������󶨵�scan������
        scan.setFilter(filter);
        excuteScan(scan);
    }

    /**
     * select * from emp where ename ='zhaoyun10' or age =23
     */
    @Test
    public void testFilterList() throws IOException {
        //ʹ��2������ֵ����������
        SingleColumnValueFilter enamefilter = new SingleColumnValueFilter(
                "f1".getBytes(), "ename".getBytes(), CompareFilter.CompareOp.EQUAL, "zhaoyun10".getBytes());
        SingleColumnValueFilter agefilter = new SingleColumnValueFilter(
                "f1".getBytes(), "age".getBytes(), CompareFilter.CompareOp.EQUAL, "23".getBytes());
        //�����������,Ҫ��Ȼ�õ���������Լ���Ҫ��.
        enamefilter.setFilterIfMissing(true);
        agefilter.setFilterIfMissing(true);
        //�ṹ������������
        //FilterList.Operator.MUST_PASS_ONE�൱��or,����һ���Ϳ���,��������ȱʧ�ֶ�.
        FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ONE);
        filterList.addFilter(enamefilter);
        filterList.addFilter(agefilter);
        //Scan ��ѯ
        Scan scan = new Scan();
        scan.setFilter(filterList);
        excuteScan(scan);
    }

    //ѧϰ�Ƚ���,����ģ����ѯ

    /**
     * BinaryComparator ���ֽ�����˳��Ƚ�ָ���ֽ����飬���� Bytes.compareTo(byte[])
     * <p>
     * BinaryPrefixComparator ��ǰ����ͬ��ֻ�ǱȽ���˵������Ƿ���ͬ
     * <p>
     * NullComparator �жϸ������Ƿ�Ϊ��
     * <p>
     * BitComparator ��λ�Ƚ�
     * <p>
     * RegexStringComparator �ṩһ������ıȽ�������֧�� EQUAL �ͷ� EQUAL
     * <p>
     * SubstringComparator �ж��ṩ���Ӵ��Ƿ������ value ��
     */
    @Test
    public void testCom() throws IOException {
        //�����ַ����Ƚ���
        //RegexStringComparator comparator=new RegexStringComparator("10");
        //SubstringComparator comparator=new SubstringComparator("10");
        //��ȫƥ��,�����������滻
        BinaryComparator comparator = new BinaryComparator("zhaoyun10".getBytes());
        SingleColumnValueFilter enamefilter = new SingleColumnValueFilter(
                "f1".getBytes(), "ename".getBytes(), CompareFilter.CompareOp.EQUAL, comparator);
        enamefilter.setFilterIfMissing(true);
        Scan scan = new Scan("0".getBytes(), enamefilter);
        scan.addColumn("f1".getBytes(), "ename".getBytes());
        excuteScan(scan);
    }

    //kv������
    //select ColumnFamily.* from ... ���˳�ĳ����ֵ�Ĺ�����
    @Test
    public void testFamilyFilter() throws IOException {
        RegexStringComparator comparator = new RegexStringComparator("f1");
        FamilyFilter familyFilter = new FamilyFilter(CompareFilter.CompareOp.EQUAL, comparator);
        Scan scan = new Scan();
        scan.setFilter(familyFilter);
        excuteScan(scan);
    }

    @Test
    //�й����� ename age gender salary.
    //select col1,col2... from ..
    public void testkeyValueFilter() throws IOException {
        //��ѯ��������a������
        QualifierFilter filter = new QualifierFilter(CompareFilter.CompareOp.EQUAL, new SubstringComparator("a"));
        Scan scan = new Scan();
        scan.setFilter(filter);
        excuteScan(scan);
    }

    //��ѯ��e��ͷ��.��ǰ׺������
    @Test
    public void testColPrefixFilter() throws IOException {
        ColumnPrefixFilter filter = new ColumnPrefixFilter("e".getBytes());
        Scan scan = new Scan();
        scan.setFilter(filter);
        excuteScan(scan);
    }


    @Test
    //��s������a��ͷ���в�ѯ
    //select col1, col from **
    public void testmcFilter() throws IOException {
        //getBytes() :Encodes this String into a sequence of bytes using the platform's default charset,
        // storing the result into a new byte array.
        byte[][] bytes = new byte[][]{"s".getBytes(), "a".getBytes()};
        MultipleColumnPrefixFilter filter = new MultipleColumnPrefixFilter(bytes);
        Scan scan = new Scan();
        scan.setFilter(filter);
        excuteScan(scan);
    }

    @Test
    //��a��e��ͷ�м���ĸ��
    public void testcrFilter() throws IOException {
        ColumnRangeFilter filter = new ColumnRangeFilter("a".getBytes(), true, "f".getBytes(), true);
        Scan scan = new Scan();
        scan.setFilter(filter);
        excuteScan(scan);
    }

    @Test
    //rowkey ����0010��
    //select * from where rowkey=''
    public void testRowfilter() throws IOException {
        RowFilter filter = new RowFilter(CompareFilter.CompareOp.EQUAL, new RegexStringComparator("0010"));
        Scan scan = new Scan();
        scan.setFilter(filter);
        excuteScan(scan);
    }

    @Test
    //select ÿһ�еĵ�һ����Ԫ��
    //select
    public void testfirst() throws IOException {
        FirstKeyOnlyFilter filter = new FirstKeyOnlyFilter();
        Scan scan = new Scan();
        scan.setFilter(filter);
        excuteScan(scan);

    }

    //select * from limt (page-1)*pagesize,pagesize
    //��ѯ��һҳ
    @Test
    public void testPageFilter() throws IOException {
        long pageSzie = 10;
        PageFilter filter = new PageFilter(pageSzie);
        Scan scan = new Scan();
        scan.setFilter(filter);
        excuteScan(scan);
    }

    //select * from limt (page-1)*pagesize,pagesize
    //��ѯ��2ҳ
    @Test
    public void testPageFilter1() throws IOException {
        long pageSzie = 10;
        PageFilter filter = new PageFilter(pageSzie);
        Scan scan = new Scan();
        scan.setStartRow("rk000019\001".getBytes());
        scan.setFilter(filter);
        excuteScan(scan);
    }


    /**
     * **cache��**
     *
     *  ��Ĭ������£��������Ҫ��hbase�в�ѯ���ݣ��ڻ�ȡ���ResultScannerʱ��hbase������ÿ�ε���ResultScanner.next��������ʱ�Է��ص�ÿ��Rowִ��һ��RPC������
     *  ��ʹ��ʹ��ResultScanner.next(int nbRows)ʱҲֻ���ڿͻ���ѭ������RsultScanner.next()��������������Ϊhbase��ִ�в�ѯ�����Ե�������ģʽ��ƣ���ִ��next��������ʱ�Ż�������ִ�в�ѯ����������ÿ��Row����ִ��һ��RPC������
     *  ����Զ��׼��ľͻ�������ҶԶ��Row���ز�ѯ�����ִ��һ��RPC���ã���ô�ͻ����ʵ�ʵ�ͨѶ������
     *  �������hbase�������ԡ�hbase.client.scanner.caching��������������cache������hbase�����ļ�����ʾ��̬�����ã�Ҳ�����ڳ���̬�����á�
     *  cacheֵ�����ò�����Խ��Խ�ã���Ҫ��һ��ƽ�⡣cache��ֵԽ�����ѯ�����ܾ�Խ�ߣ�
     *  �������ͬʱ��ÿһ�ε���next������������Ҫ���Ѹ�����ʱ�䣬��Ϊ��ȡ�����ݸ��ಢ�����������˴��䵽�ͻ�����Ҫ��ʱ���Խ����
     *  һ���㳬����maximum heap the client process ӵ�е�ֵ���ͻᱨoutofmemoryException�쳣��
     *  ������rows���ݵ��ͻ��˵�ʱ���������ʱ�����������׳�ScannerTimeOutException�쳣��
     *
     * **batch**��
     *    ��cache������£�����һ�����۵�����ԱȽ�С��row����ô���һ��Row�ر���ʱ��Ӧ����ô�����أ�
     *    Ҫ֪��cache��ֵ���ӣ���ô��client process ռ�õ��ڴ�ͻ�����row�����������
     *    ��hbase��ͬ��Ϊ�����������ṩ�����ƵĲ�����Batch��������ô��⣬cache�������е��Ż�����batch�������е��Ż�����
     *    ����������ÿ�ε���next��������ʱ�᷵�ض����У�����������setBatch��5������ôÿһ��Resultʵ���ͻ᷵��5�У�����������Ϊ17�Ļ�����ô�ͻ����ĸ�Resultʵ�����ֱ���5,5,5,2���С�
     * @throws IOException
     */
    @Test
    //��������,�Ӵ����setbacth��setcaching. ������̫�����ֲ�����
    public void test() throws IOException {
    Scan scan=new Scan();
    //Set the maximum number of values to return for each call to next()
    scan.setBatch(4);
    scan.setCaching(1);
    excuteScan(scan);
    }




    //��ʱ��װscan���߼�,���ٴ���
    public void excuteScan(Scan scan) throws IOException {
        //Returns a scanner on the current table as specified by the Scan object. Note that the passed Scan's start row and caching properties maybe changed.
        //����һ��ResultScanner����,������ʵ���˵����ӿ�,�Ǿͼ���.������Result����
       ResultScanner resultScanner=table.getScanner(scan);
       //Result����ΪSingle row result of a Get or Scan query.
        for (Result result : resultScanner) {
            while (result.advance()){
                //��õ���cell����,��δ�ӡ��cell�����а�����������?����cellutil�������ȽϷ���
                Cell cell=result.current();
                //CellUtil.cloneRowʵ���ϻ�ȡ����rowkey��byte����
                //
                System.out.println("\n"+new String(CellUtil.cloneRow(cell))+"\t"+ new String(CellUtil.cloneFamily(cell)) + "\t"
                        + new String(CellUtil.cloneQualifier(cell))+"\t"+new String(CellUtil.cloneValue(cell)));

            }
        }

    }

}