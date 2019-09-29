package net.jplugin.core.das.route.impl.conn;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import com.mysql.jdbc.PreparedStatement;

import net.jplugin.core.das.api.DataSourceFactory;
import net.jplugin.core.das.route.api.SqlHandleService;
import net.jplugin.core.das.route.api.TablesplitException;
import net.jplugin.core.das.route.impl.autocreate.TableExistsMaintainer;
import net.jplugin.core.das.route.impl.autocreate.TableExistsMaintainer.MaintainReturn;
import net.jplugin.core.das.route.impl.conn.mulqry.CombineStatementFactory;
import net.jplugin.core.das.route.impl.conn.mulqry.CombinedSqlParser;
import net.jplugin.core.das.route.impl.conn.nrs.NoneResultStatement;

public class RouterStatement extends EmptyStatement {
	protected RouterConnection connection;
	protected ExecuteResult executeResult = new ExecuteResult();

	public static Statement create(RouterConnection conn) {
		RouterStatement cs = new RouterStatement();
		cs.connection = conn;
		return cs;
	}

	@Override
	public final ResultSet executeQuery(String sql) throws SQLException {
		Result r = genTargetNotPreparedStatement(connection, sql);
		Statement stmt = r.statement;
		sql = r.resultSql;
		ResultSet rs = stmt.executeQuery(sql);
		
		return rs;
	}

	@Override
	public final int executeUpdate(String sql) throws SQLException {
		Result r = genTargetNotPreparedStatement(connection, sql);
		Statement stmt = r.statement;
		sql = r.resultSql;
		int cnt = stmt.executeUpdate(sql);
		
		return cnt;
	}

	@Override
	public final  boolean execute(String sql) throws SQLException {
		Result rr = genTargetNotPreparedStatement(connection, sql);
		Statement stmt = rr.statement;
		sql = rr.resultSql;
		boolean r = stmt.execute(sql);
		
		return r;
	}

	@Override
	public final  ResultSet getResultSet() throws SQLException {
		return executeResult.getResult();
	}

	@Override
	public final  int getUpdateCount() throws SQLException {
		return executeResult.getUpdateCount();
	}

	@Override
	public final  Connection getConnection() throws SQLException {
		return connection;
	}

	@Override
	public final  int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
		Result r = genTargetNotPreparedStatement(connection, sql);
		Statement stmt = r.statement;
		sql = r.resultSql;
		int cnt = stmt.executeUpdate(sql, autoGeneratedKeys);
		
		return cnt;
	}

	@Override
	public  final int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
		Result r = genTargetNotPreparedStatement(connection, sql);
		Statement stmt = r.statement;
		sql = r.resultSql;
		int cnt = stmt.executeUpdate(sql, columnIndexes);
		
		return cnt;
	}

	@Override
	public final  int executeUpdate(String sql, String[] columnNames) throws SQLException {
		Result r = genTargetNotPreparedStatement(connection, sql);
		Statement stmt = r.statement;
		sql = r.resultSql;
		int cnt = stmt.executeUpdate(sql, columnNames);
		
		return cnt;
	}

	@Override
	public  final boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
		Result r = genTargetNotPreparedStatement(connection, sql);
		Statement stmt = r.statement;
		sql = r.resultSql;
		boolean b = stmt.execute(sql, autoGeneratedKeys);
		return b;
	}

	@Override
	public  final boolean execute(String sql, int[] columnIndexes) throws SQLException {
		Result r = genTargetNotPreparedStatement(connection, sql);
		Statement stmt = r.statement;
		sql = r.resultSql;
		boolean b = stmt.execute(sql, columnIndexes);
		return b;
	}

	@Override
	public final  boolean execute(String sql, String[] columnNames) throws SQLException {
		Result r = genTargetNotPreparedStatement(connection, sql);
		Statement stmt = r.statement;
		sql = r.resultSql;
		boolean b = stmt.execute(sql, columnNames);
		return b;
	}

	@Override
	public final  boolean isClosed() throws SQLException {
		return false;
	}

	@Override
	public  final void close() throws SQLException {
		this.executeResult.clear();
	}
	
	@Override
	public  final boolean getMoreResults() throws SQLException {
		return this.executeResult.getMoreResults();
	}

	/**
	 * 这个返回用来执行带sql参数的情况
	 * @return
	 * @throws SQLException
	 */
	private  Result genTargetNotPreparedStatement(RouterConnection conn,String sql) throws SQLException {
		if (sql==null) throw new TablesplitException("No sql found");
		SqlHandleResult shr = SqlHandleService.INSTANCE.handle(conn,sql);
		
		LogUtil.instance.log(shr);
		
		//处理无存在的表的特殊情况
		String  dsForMakeDymmy = shr.getDataSourceInfos()[0].getDsName();
		MaintainReturn maintainResult = TableExistsMaintainer.maintainAndCheckNoneResult(shr);
		if (maintainResult.isSpecialCondition()){
			Result temp = SpecialReturnHandler.hanleSpecialConditionForStatement(maintainResult,this.connection,DataSourceFactory.getDataSource(dsForMakeDymmy).getConnection());
			this.executeResult.set(temp.statement);
			return temp;
		}
		
		Statement stmt;
		//根据不同状态获取不同的statement
		if (!shr.singleTable()){
			stmt = CombineStatementFactory.create(connection);
		}else{
			String dsname = shr.getDataSourceInfos()[0].getDsName();
			DataSource tds = DataSourceFactory.getDataSource(dsname);
			if (tds==null) 
				throw new TablesplitException("Can't find target datasource."+dsname);
			stmt = tds.getConnection().createStatement();
		}
		Result result = new Result();
		result.statement = stmt;
		this.executeResult.set(result.statement);
		
		//根据不同状态使用不同的sql
		if (shr.singleTable())
			result.resultSql = shr.resultSql;
		else
			result.resultSql = shr.getEncodedSql();
		return result;
	}
	
	public static class Result{
		Statement statement;
		String resultSql;
	}
}
