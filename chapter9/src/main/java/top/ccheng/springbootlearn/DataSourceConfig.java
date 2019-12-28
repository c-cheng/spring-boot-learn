package top.ccheng.springbootlearn;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * @author ccheng
 * @date 2018/9/9
 */
@Configuration
public class DataSourceConfig {

    /**
     * 使用主数据源配置信息创建主数据源
     * @param dataSourceProperties 主数据源配置信息
     * @return 主数据源
     */
    @Bean(name = "primaryDataSource")
    @Primary
    public DataSource primaryDataSource(@Qualifier("primaryDataSourceProperties") DataSourceProperties dataSourceProperties) {
        return DataSourceBuilder.create()
                .driverClassName(dataSourceProperties.getDriverClassName())
                .url(dataSourceProperties.getUrl())
                .username(dataSourceProperties.getUsername())
                .password(dataSourceProperties.getPassword())
                .build();
    }

    /**
     * 使用次数据源配置信息创建次数据源
     * @param dataSourceProperties 次数据源配置信息
     * @return 次数据源
     */
    @Bean(name = "secondaryDataSource")
    public DataSource secondaryDataSource(@Qualifier("secondDataSourceProperties") DataSourceProperties dataSourceProperties) {
        return DataSourceBuilder.create()
                .driverClassName(dataSourceProperties.getDriverClassName())
                .url(dataSourceProperties.getUrl())
                .username(dataSourceProperties.getUsername())
                .password(dataSourceProperties.getPassword())
                .build();
    }

    /**
     * 读取主数据源配置信息
     */
    @Bean(name = "primaryDataSourceProperties")
    @ConfigurationProperties(prefix="spring.datasource.primary")
    public DataSourceProperties primaryDataSourceProperties(){
        return new DataSourceProperties();
    }

    /**
     * 读取次数据源配置信息
     */
    @Bean(name = "secondDataSourceProperties")
    @ConfigurationProperties(prefix="spring.datasource.secondary")
    public DataSourceProperties secondDataSourceProperties(){
        return new DataSourceProperties();
    }

}

class DataSourceProperties{
    private String driverClassName;
    private String url;
    private String username;
    private String password;

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
