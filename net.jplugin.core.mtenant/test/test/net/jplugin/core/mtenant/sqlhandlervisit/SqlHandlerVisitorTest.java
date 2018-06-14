package test.net.jplugin.core.mtenant.sqlhandlervisit;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.core.mtenant.handler2.SqlHandlerVisitorForMixed;
import net.sf.jsqlparser.JSQLParserException;

public class SqlHandlerVisitorTest {
//	static String cloumnName = "column1";
	static String cloumnName = "";
	
	public static void test() throws JSQLParserException {
		testSelect();
		testInsert();
		testUpdate();
		testDelete();
//		testPerform();
		testReplace();
//		testReplaceMulValue();
		testUnionAll();
	}

	private static void testReplaceMulValue() throws JSQLParserException{
		SqlHandlerVisitorForMixed v = new SqlHandlerVisitorForMixed("sssss","1001");
		String from,to;
		
//		from = "replace into customer (customer_id,customer_name,city,tfield) "+
//				"values ('aaa','bbb','ccc','ddd'),('aaa1','bbb1','ccc1','ddd1')";
		from = "replace into customer (customer_id,customer_name,city,tfield) "+
				"values ('aaa','bbb','ccc','ddd'),('aaa1','bbb1','ccc1','ddd1');";
		
		to = v.handle(from);
		System.out.println();
		System.out.println(from);
		System.out.println(to);
	}
	
	private static void testUnionAll() throws JSQLParserException{
		SqlHandlerVisitorForMixed v = new SqlHandlerVisitorForMixed("sssss","1001");
		String from,to;

		from = "select  customer_id,customer_name,city,tfield from tb1  "+
				"union all "+
				"select  customer_id,customer_name,city,tfield from tb1 ";
		
		to = v.handle(from);
		System.out.println(from);
		System.out.println(to);
		
		v = new SqlHandlerVisitorForMixed("sssss");
//		from = "select  customer_id,customer_name,city,tfield from tb1  "+
//				"union all "+
//				"select  customer_id,customer_name,city,tfield from tb1 ";
//		
//		to = v.handle(from);
//		System.out.println(from);
//		System.out.println(to);
//		
		
		from = "SELECT b.msg_type AS msgType, b.msg_content AS msgContent, b.content_type AS contentType FROM wx_keyword a JOIN wx_msg b ON a.msg_id = b.id "+
				"WHERE a.name LIKE CONCAT('%', 1, '%') AND a.match_type = 2 AND b.state = 1 AND b.public_number_id = 8 "+
				"UNION ALL SELECT b.msg_type AS msgType, b.msg_content AS msgContent, b.content_type AS contentType  "+
				"FROM wx_keyword a JOIN wx_msg b ON a.msg_id = b.id  "+
				"WHERE a.name = 1 AND a.match_type = 1 AND b.state = 1 AND b.public_number_id = 8 ";
		to = v.handle(from);
		System.out.println(from);
		System.out.println(to);
	}
	
	private static void testReplace() throws JSQLParserException{
		
		
		SqlHandlerVisitorForMixed v = new SqlHandlerVisitorForMixed("sssss","1001");
		String from,to;
		
		from = "replace into customer (customer_id,customer_name,city,tfield) "+
				"values ('aaa','bbb','ccc','ddd','eee')";
		
		to = v.handle(from);
		System.out.println();
		System.out.println(from);
		System.out.println(to);
		
		from = "replace into customer (customer_id,customer_name,city,tfield) "+
				"select customer_id,CONCAT(customer_name,'a'),city,tfield from customer;";
		
		to = v.handle(from);
		System.out.println();
		System.out.println(from);
		System.out.println(to);
	}
	
	private static void testPerform() throws JSQLParserException {
		SqlHandlerVisitorForMixed v = new SqlHandlerVisitorForMixed("sssss","1001");
		String from,to;
		
		long start = System.currentTimeMillis();
		for (int i=0;i<10000;i++){
			from = "select * from table1,table2 left join t3 f WHERE A=1 OR C=3 and f1 in (select * from t3,t4 where t3.x = t4.y)";
			to = v.handle(from);
		}
		System.out.println("timeis :"+(System.currentTimeMillis() - start));
	}

	private static void testDelete() throws JSQLParserException {
		SqlHandlerVisitorForMixed v = new SqlHandlerVisitorForMixed("sssss","1001");
		String from,to;
		
		from = "delete from t1 ";
		to = v.handle(from);
		System.out.println();
		System.out.println(from);
		System.out.println(to);
		
		from = "delete from t1  where f1=1 and f2=2";
		to = v.handle(from);
		System.out.println();
		System.out.println(from);
		System.out.println(to);
	}

	static void testUpdate() throws JSQLParserException{
		SqlHandlerVisitorForMixed v = new SqlHandlerVisitorForMixed("sssss","1001");
		String from,to;
		
		from = "update t1 set f1=1,f2=2";
		to = v.handle(from);
		System.out.println();
		System.out.println(from);
		System.out.println(to);
		
		from = "update t1 set f1=1,f2=2 where 1=2";
		to = v.handle(from);
		System.out.println();
		System.out.println(from);
		System.out.println(to);
	}

	private static void testInsert() throws JSQLParserException {
		SqlHandlerVisitorForMixed v = new SqlHandlerVisitorForMixed("sssss","1001");
		String from,to;
		
		from = "insert into t1 (f1,f2) values (?,'a')";
		to = v.handle(from);
//		AssertKit.assertEqual("INSERT INTO T1 (f1, f2, column1) VALUES (?, 'a', '1001')", to);
		System.out.println();
		System.out.println(from);
		System.out.println(to);
		
		from = "insert into t1 (f1,f2) values (?,'a'),(?,'b')";
		to = v.handle(from);
//		AssertKit.assertEqual("INSERT INTO T1 (f1, f2, column1) VALUES (?, 'a', '1001')", to);
		System.out.println();
		System.out.println(from);
		System.out.println(to);
		
		from = "insert into t1 (f1,f2) select a,b from (select * from t5 where 1=2)";
		to = v.handle(from);
		System.out.println();
		System.out.println(from);
		System.out.println(to);
	}

	private static void testSelect() throws JSQLParserException {
		SqlHandlerVisitorForMixed v = new SqlHandlerVisitorForMixed("sssss","1001");
		String from,to;
		
		from = "select * from t1 t where f1=1 and f2=2 or f3=3 and f4=4";
		to = v.handle(from);
//		AssertKit.assertEqual("SELECT * FROM sssss.table1, sssss.table2 LEFT JOIN sssss.t3 AS f ON f.column1 = '1001' WHERE sssss.table2.column1 = '1001' AND sssss.table1.column1 = '1001' AND (A = 1 OR C = 3 AND f1 IN (SELECT * FROM sssss.t3, sssss.t4 WHERE sssss.t4.column1 = '1001' AND sssss.t3.column1 = '1001' AND (t3.x = t4.y)))", to);
		System.out.println();
		System.out.println(from);
		System.out.println(to);
		
		
		from = "select * from table1,table2 left join t3 f WHERE A=1 OR C=3 and f1 in (select * from t3,t4 where t3.x = t4.y)";
		to = v.handle(from);
//		AssertKit.assertEqual("SELECT * FROM sssss.table1, sssss.table2 LEFT JOIN sssss.t3 AS f ON f.column1 = '1001' WHERE sssss.table2.column1 = '1001' AND sssss.table1.column1 = '1001' AND (A = 1 OR C = 3 AND f1 IN (SELECT * FROM sssss.t3, sssss.t4 WHERE sssss.t4.column1 = '1001' AND sssss.t3.column1 = '1001' AND (t3.x = t4.y)))", to);
		System.out.println();
		System.out.println(from);
		System.out.println(to);
		
		
		from = "SELECT aa.ScoreRuleID, aa.SectionScore, aa.MinScoreMoney, aa.consumeMoney, aa.Score, CASE WHEN gs.goodscode = '013571' THEN 1 WHEN (gys.suppliercode IN (SELECT suppliercode FROM tbGoodsSupp WHERE goodscode = '013571' AND NodeCode = '0316') AND '0207010101' LIKE CONCAT(fl.categorycode, '%') AND pp.brandcode = '02070213') THEN 2 WHEN (gys.suppliercode IN (SELECT suppliercode FROM tbGoodsSupp WHERE goodscode = '013571' AND NodeCode = '0316') AND pp.brandcode = '02070213') THEN 3 WHEN (gys.suppliercode IN (SELECT suppliercode FROM tbGoodsSupp WHERE goodscode = '013571' AND NodeCode = '0316') AND '0207010101' LIKE CONCAT(fl.categorycode, '%')) THEN 4 WHEN (pp.brandcode = '02070213') THEN 5 WHEN (gys.suppliercode IN (SELECT suppliercode FROM tbGoodsSupp WHERE goodscode = '013571' AND NodeCode = '0316')) THEN 6 WHEN ('0207010101' LIKE CONCAT(fl.categorycode, '%')) THEN 7 + 10 - length(coalesce(fl.categorycode, '')) WHEN ('0207010101' LIKE CONCAT(fl.categorycode, '%')) THEN 8 + 10 - length(coalesce(fl.categorycode, '')) ELSE 19 END slevel FROM tbScoreRules aa LEFT JOIN tbScoreRule_Goods gs ON aa.scoreruleid = gs.scoreruleid LEFT JOIN tbScoreRule_GoodsCate fl ON aa.scoreruleid = fl.scoreruleid LEFT JOIN tbScoreRule_Brands pp ON aa.scoreruleid = pp.scoreruleid LEFT JOIN tbScoreRule_supps gys ON aa.scoreruleid = gys.scoreruleid LEFT JOIN tbScoreRules_CardLevel cl ON aa.scoreruleid = cl.scoreruleid LEFT JOIN tbScoreRule_Counter co ON aa.scoreruleid = co.scoreruleid AND co.DeptCode = '0316' WHERE (goodscode IS NULL OR goodscode = '013571') AND (fl.categorycode IS NULL OR '0207010101' LIKE CONCAT(fl.categorycode, '%')) AND (pp.brandcode IS NULL OR pp.brandcode = '02070213') AND (gys.suppliercode IS NULL OR gys.suppliercode IN (SELECT suppliercode FROM tbGoodsSupp WHERE goodscode = '013571' AND NodeCode = '0316')) AND (co.CounterCode IS NULL OR co.CounterCode = '') AND (CardTypeID = -1 OR CardTypeID = 1) AND (cl.levelid <= 1 OR cl.LevelID IS NULL) AND NOT (goodscode IS NULL AND fl.categorycode IS NULL AND pp.brandcode IS NULL AND gys.suppliercode IS NULL AND co.CounterCode IS NULL AND cl.LevelID IS NULL) AND NOT EXISTS (SELECT 1 FROM tbScoreExcGoods WHERE GoodsCode = '013571' AND NodeCode = '0316') AND NOT EXISTS (SELECT 1 FROM tbScoreExcBrands WHERE BrandCode = '02070213' AND NodeCode = '0316') AND NOT EXISTS (SELECT 1 FROM tbScoreExcGoodsCate WHERE '0207010101' LIKE CONCAT(CategoryCode, '%') AND NodeCode = '0316') AND NOT EXISTS (SELECT 1 FROM tbScoreExcSupps WHERE SupplierCode IN (SELECT suppliercode FROM tbGoodsSupp WHERE goodscode = '013571' AND NodeCode = '0316')) AND (aa.NodeCode IS NULL OR aa.NodeCode = '0316') AND (gs.NodeCode IS NULL OR gs.NodeCode = '0316') AND (fl.NodeCode IS NULL OR fl.NodeCode = '0316') AND (pp.NodeCode IS NULL OR pp.NodeCode = '0316') AND (gys.NodeCode IS NULL OR gys.NodeCode = '0316') AND (cl.NodeCode IS NULL OR cl.NodeCode = '0316') AND (co.NodeCode IS NULL OR co.NodeCode = '0316') ORDER BY slevel, Score / ConsumeMoney DESC";
		to = v.handle(from);
//		AssertKit.assertEqual("SELECT * FROM sssss.table1, sssss.table2 LEFT JOIN sssss.t3 AS f ON f.column1 = '1001' WHERE sssss.table2.column1 = '1001' AND sssss.table1.column1 = '1001' AND (A = 1 OR C = 3 AND f1 IN (SELECT * FROM sssss.t3, sssss.t4 WHERE sssss.t4.column1 = '1001' AND sssss.t3.column1 = '1001' AND (t3.x = t4.y)))", to);
		System.out.println();
		System.out.println(from);
		System.out.println(to);
	}
}
