package io.training.boot.druid;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class DataSourceConfig {

    private MultipleDataSource multipleDataSource;

    @Bean
    @ConfigurationProperties("spring.datasource.druid.one")
    public DataSource dataSourceOne(SqlReadWriteFilter sqlReadWriteFilter){
        DruidDataSource druidDataSource = DruidDataSourceBuilder.create().build();
        List<Filter> filters = druidDataSource.getProxyFilters();
        filters.add(sqlReadWriteFilter);
        return druidDataSource;
    }

    @Bean
    @ConfigurationProperties("spring.datasource.druid.two")
    public DataSource dataSourceTwo(SqlReadWriteFilter sqlReadWriteFilter){
        DruidDataSource druidDataSource = DruidDataSourceBuilder.create().build();
        List<Filter> filters = druidDataSource.getProxyFilters();
        filters.add(sqlReadWriteFilter);
        return druidDataSource;
    }

    @Bean
    public SqlReadWriteFilter sqlReadWriteFilter() {
        return new SqlReadWriteFilter();
    }

    @Bean
    @Primary
    public DataSource dataSource(@Autowired DataSource dataSourceOne,
                                 @Autowired DataSource dataSourceTwo){
        MultipleDataSource multipleDataSource = new MultipleDataSource();
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put("dataSourceOne", dataSourceOne);
        targetDataSources.put("dataSourceTwo", dataSourceTwo);
        multipleDataSource.setTargetDataSources(targetDataSources);
        multipleDataSource.setDefaultTargetDataSource(dataSourceOne);

        this.multipleDataSource = multipleDataSource;
        return multipleDataSource;
    }

}
