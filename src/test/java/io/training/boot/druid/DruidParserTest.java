package io.training.boot.druid;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.visitor.SchemaStatVisitor;
import com.alibaba.druid.stat.TableStat;
import com.alibaba.druid.util.JdbcConstants;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.List;
import java.util.Map;

@Slf4j
public class DruidParserTest {

    @Test
    public void testParser() {

        String sql = "select * from user where role_id in(select id from role)";

        sql = "insert into user('id', 'name') select * from user_temp";

        //sql = "select c1.* from category c1 left join category c2 on c1.id = c2.parent_id";

        List<SQLStatement> statements = SQLUtils.parseStatements(sql, JdbcConstants.MYSQL);

        SQLStatement stmt = statements.get(0);

        SchemaStatVisitor statVisitor = SQLUtils.createSchemaStatVisitor(JdbcConstants.MYSQL);

        stmt.accept(statVisitor);

        Map<TableStat.Name, TableStat> tables = statVisitor.getTables();

        tables.values();


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

        }


        tables.forEach((k, v) -> {
            log.info("==========k:{}, v:{}", k, v);
        });


        log.info("================={}", statVisitor.getTables());
    }
}
