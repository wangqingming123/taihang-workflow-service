package com.allintechinc.taihang.workflow.initialization;

import liquibase.exception.LiquibaseException;
import liquibase.integration.spring.SpringLiquibase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 * Title:
 * Build: 2020-06-13 14:07:05
 *
 * @author Chang Yuxin
 */
@Slf4j
@Component
public class WorkflowTableInitialization implements CommandLineRunner {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private Environment environment;

    /**
     * 初始化数据库结构
     *
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {

        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setChangeLog(environment.getProperty("spring.liquibase.change-log"));
        liquibase.setShouldRun(true);
        liquibase.setResourceLoader(new DefaultResourceLoader());
        liquibase.setDatabaseChangeLogLockTable(environment.getProperty("spring.liquibase.database-change-log-lock-table"));
        liquibase.setDatabaseChangeLogTable(environment.getProperty("spring.liquibase.database-change-log-table"));
        liquibase.setDataSource(dataSource);
        try {
            liquibase.afterPropertiesSet();
        } catch (LiquibaseException e) {
            log.error("init database structure error!", e);
        }
    }
}
