package io.training.boot.druid;

import com.alibaba.druid.filter.FilterEventAdapter;
import com.alibaba.druid.proxy.jdbc.StatementProxy;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.visitor.SchemaStatVisitor;
import com.alibaba.druid.util.JdbcConstants;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class SqlReadWriteFilter extends FilterEventAdapter {

    protected void statementExecuteBefore(StatementProxy statement, String sql) {
        List<SQLStatement> statements = SQLUtils.parseStatements(sql, JdbcConstants.MYSQL);
        SQLStatement stmt = statements.get(0);
        SchemaStatVisitor statVisitor = SQLUtils.createSchemaStatVisitor(JdbcConstants.MYSQL);
        stmt.accept(statVisitor);

        int insertSum = statVisitor.getTables()
                .values()
                .parallelStream()
                .mapToInt(it -> it.getInsertCount())
                .sum();

        int updateSum = statVisitor.getTables()
                .values()
                .parallelStream()
                .mapToInt(it -> it.getUpdateCount())
                .sum();

        int deleteSum = statVisitor.getTables()
                .values()
                .parallelStream()
                .mapToInt(it -> it.getDeleteCount())
                .sum();

        int sum = insertSum + updateSum + deleteSum;

        if(sum > 0) {
            MultipleDataSource.setDataSourceKey("dataSourceOne");
//            MultipleDataSource.setDataSourceKey("dataSourceTwo");
            return;
        }
        MultipleDataSource.setDataSourceKey("dataSourceTwo");
    }

}
