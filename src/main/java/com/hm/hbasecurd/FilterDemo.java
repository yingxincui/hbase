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

//关于hbase过滤器的一些示例代码
//没有过滤器的话,不方便精确查询
/*
带有Filter条件的RPC查询请求会把Filter分发到各个RegionServer，是一个服务器端（Server-side）的过滤器，这样也可以降低网络传输的压力
　要完成一个过滤的操作，至少需要两个参数。
一个是抽象的操作符，Hbase提供了枚举类型的变量来表示这些抽象的操作符：LESS/LESS_OR_EQUAL/EQUAL/NOT_EUQAL等；
另外一个就是具体的比较器（Comparator），代表具体的比较逻辑，如果可以提高字节级的比较、字符串级的比较等。
有了这两个参数，我们就可以清晰的定义筛选的条件，过滤数据
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
    //单列值过滤器 singleColumnValueFilter
    public void testSCVF() throws IOException {
        SingleColumnValueFilter filter = new SingleColumnValueFilter(
                "f1".getBytes(), "ename".getBytes(), CompareFilter.CompareOp.EQUAL, "zhaoyun100".getBytes());
        filter.setFilterIfMissing(true);
        Scan scan = new Scan();
        //过滤器绑定到scan对象上
        scan.setFilter(filter);
        excuteScan(scan);
    }

    /**
     * select * from emp where ename ='zhaoyun10' or age =23
     */
    @Test
    public void testFilterList() throws IOException {
        //使用2个单列值过滤器设置
        SingleColumnValueFilter enamefilter = new SingleColumnValueFilter(
                "f1".getBytes(), "ename".getBytes(), CompareFilter.CompareOp.EQUAL, "zhaoyun10".getBytes());
        SingleColumnValueFilter agefilter = new SingleColumnValueFilter(
                "f1".getBytes(), "age".getBytes(), CompareFilter.CompareOp.EQUAL, "23".getBytes());
        //必须设置这个,要不然得到结果不是自己想要的.
        enamefilter.setFilterIfMissing(true);
        agefilter.setFilterIfMissing(true);
        //结构过滤器绑定条件
        //FilterList.Operator.MUST_PASS_ONE相当于or,满足一个就可以,必须设置缺失字段.
        FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ONE);
        filterList.addFilter(enamefilter);
        filterList.addFilter(agefilter);
        //Scan 查询
        Scan scan = new Scan();
        scan.setFilter(filterList);
        excuteScan(scan);
    }

    //学习比较器,可以模糊查询

    /**
     * BinaryComparator 按字节索引顺序比较指定字节数组，采用 Bytes.compareTo(byte[])
     * <p>
     * BinaryPrefixComparator 跟前面相同，只是比较左端的数据是否相同
     * <p>
     * NullComparator 判断给定的是否为空
     * <p>
     * BitComparator 按位比较
     * <p>
     * RegexStringComparator 提供一个正则的比较器，仅支持 EQUAL 和非 EQUAL
     * <p>
     * SubstringComparator 判断提供的子串是否出现在 value 中
     */
    @Test
    public void testCom() throws IOException {
        //正则字符串比较器
        //RegexStringComparator comparator=new RegexStringComparator("10");
        //SubstringComparator comparator=new SubstringComparator("10");
        //完全匹配,可以用正则替换
        BinaryComparator comparator = new BinaryComparator("zhaoyun10".getBytes());
        SingleColumnValueFilter enamefilter = new SingleColumnValueFilter(
                "f1".getBytes(), "ename".getBytes(), CompareFilter.CompareOp.EQUAL, comparator);
        enamefilter.setFilterIfMissing(true);
        Scan scan = new Scan("0".getBytes(), enamefilter);
        scan.addColumn("f1".getBytes(), "ename".getBytes());
        excuteScan(scan);
    }

    //kv过滤器
    //select ColumnFamily.* from ... 过滤出某个列值的过滤器
    @Test
    public void testFamilyFilter() throws IOException {
        RegexStringComparator comparator = new RegexStringComparator("f1");
        FamilyFilter familyFilter = new FamilyFilter(CompareFilter.CompareOp.EQUAL, comparator);
        Scan scan = new Scan();
        scan.setFilter(familyFilter);
        excuteScan(scan);
    }

    @Test
    //列过滤器 ename age gender salary.
    //select col1,col2... from ..
    public void testkeyValueFilter() throws IOException {
        //查询列名含有a的内容
        QualifierFilter filter = new QualifierFilter(CompareFilter.CompareOp.EQUAL, new SubstringComparator("a"));
        Scan scan = new Scan();
        scan.setFilter(filter);
        excuteScan(scan);
    }

    //查询以e开头的.列前缀过滤器
    @Test
    public void testColPrefixFilter() throws IOException {
        ColumnPrefixFilter filter = new ColumnPrefixFilter("e".getBytes());
        Scan scan = new Scan();
        scan.setFilter(filter);
        excuteScan(scan);
    }


    @Test
    //以s或者以a开头的列查询
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
    //以a或e开头中间字母的
    public void testcrFilter() throws IOException {
        ColumnRangeFilter filter = new ColumnRangeFilter("a".getBytes(), true, "f".getBytes(), true);
        Scan scan = new Scan();
        scan.setFilter(filter);
        excuteScan(scan);
    }

    @Test
    //rowkey 含有0010的
    //select * from where rowkey=''
    public void testRowfilter() throws IOException {
        RowFilter filter = new RowFilter(CompareFilter.CompareOp.EQUAL, new RegexStringComparator("0010"));
        Scan scan = new Scan();
        scan.setFilter(filter);
        excuteScan(scan);
    }

    @Test
    //select 每一行的第一个单元格
    //select
    public void testfirst() throws IOException {
        FirstKeyOnlyFilter filter = new FirstKeyOnlyFilter();
        Scan scan = new Scan();
        scan.setFilter(filter);
        excuteScan(scan);

    }

    //select * from limt (page-1)*pagesize,pagesize
    //查询第一页
    @Test
    public void testPageFilter() throws IOException {
        long pageSzie = 10;
        PageFilter filter = new PageFilter(pageSzie);
        Scan scan = new Scan();
        scan.setFilter(filter);
        excuteScan(scan);
    }

    //select * from limt (page-1)*pagesize,pagesize
    //查询第2页
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
     * **cache：**
     *
     *  在默认情况下，如果你需要从hbase中查询数据，在获取结果ResultScanner时，hbase会在你每次调用ResultScanner.next（）操作时对返回的每个Row执行一次RPC操作。
     *  即使你使用ResultScanner.next(int nbRows)时也只是在客户端循环调用RsultScanner.next()操作，你可以理解为hbase将执行查询请求以迭代器的模式设计，在执行next（）操作时才会真正的执行查询操作，而对每个Row都会执行一次RPC操作。
     *  因此显而易见的就会想如果我对多个Row返回查询结果才执行一次RPC调用，那么就会减少实际的通讯开销。
     *  这个就是hbase配置属性“hbase.client.scanner.caching”的由来，设置cache可以在hbase配置文件中显示静态的配置，也可以在程序动态的设置。
     *  cache值得设置并不是越大越好，需要做一个平衡。cache的值越大，则查询的性能就越高，
     *  但是与此同时，每一次调用next（）操作都需要花费更长的时间，因为获取的数据更多并且数据量大了传输到客户端需要的时间就越长，
     *  一旦你超过了maximum heap the client process 拥有的值，就会报outofmemoryException异常。
     *  当传输rows数据到客户端的时候，如果花费时间过长，则会抛出ScannerTimeOutException异常。
     *
     * **batch**：
     *    在cache的情况下，我们一般讨论的是相对比较小的row，那么如果一个Row特别大的时候应该怎么处理呢？
     *    要知道cache的值增加，那么在client process 占用的内存就会随着row的增大而增大。
     *    在hbase中同样为解决这种情况提供了类似的操作：Batch。可以这么理解，cache是面向行的优化处理，batch是面向列的优化处理。
     *    它用来控制每次调用next（）操作时会返回多少列，比如你设置setBatch（5），那么每一个Result实例就会返回5列，如果你的列数为17的话，那么就会获得四个Result实例，分别含有5,5,5,2个列。
     * @throws IOException
     */
    @Test
    //测试性能,捎带理解setbacth和setcaching. 数据量太少体现不处理
    public void test() throws IOException {
    Scan scan=new Scan();
    //Set the maximum number of values to return for each call to next()
    scan.setBatch(4);
    scan.setCaching(1);
    excuteScan(scan);
    }




    //临时封装scan的逻辑,减少代码
    public void excuteScan(Scan scan) throws IOException {
        //Returns a scanner on the current table as specified by the Scan object. Note that the passed Scan's start row and caching properties maybe changed.
        //返回一个ResultScanner对象,看帮助实现了迭代接口,那就简单了.迭代出Result对象
       ResultScanner resultScanner=table.getScanner(scan);
       //Result对象为Single row result of a Get or Scan query.
        for (Result result : resultScanner) {
            while (result.advance()){
                //获得的是cell对象,如何打印出cell对象中包含的属性呢?利用cellutil工具类会比较方便
                Cell cell=result.current();
                //CellUtil.cloneRow实际上获取的是rowkey的byte数组
                //
                System.out.println("\n"+new String(CellUtil.cloneRow(cell))+"\t"+ new String(CellUtil.cloneFamily(cell)) + "\t"
                        + new String(CellUtil.cloneQualifier(cell))+"\t"+new String(CellUtil.cloneValue(cell)));

            }
        }

    }

}