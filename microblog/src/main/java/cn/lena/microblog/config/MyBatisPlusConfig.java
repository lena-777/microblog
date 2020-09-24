package cn.lena.microblog.config;

import com.baomidou.mybatisplus.core.parser.ISqlParser;
import com.baomidou.mybatisplus.extension.parsers.BlockAttackSqlParser;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.SqlExplainInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;


@Configuration
//@EnableTransactionManagement
@MapperScan("cn.lena.microblog.mapper")		//配置mapper扫描的包
public class MyBatisPlusConfig {

	private final static Logger logger = LoggerFactory.getLogger(MyBatisPlusConfig.class);

	/**
	 * 配置分页插件
	 */
	@Bean
	public PaginationInterceptor paginationInterceptor() {
		logger.debug("注册分页插件");
		return new PaginationInterceptor();
	}


	/**
	 * SQL执行效率插件 性能分析插件 (会打印sql语句)
	 */
/*	@Bean
	@Profile({"dev","test"})// 设置 dev test 环境开启
	public PerformanceInterceptor performanceInterceptor() {
		PerformanceInterceptor performanceInterceptor =  new PerformanceInterceptor();
		performanceInterceptor.setFormat(true);//格式化语句
		//performanceInterceptor.setMaxTime(100);//执行时间超过多少秒会抛出异常
		return  performanceInterceptor;
	}*/


	/**
	 * 逻辑删除用，3.1.1之后的版本可不需要配置该bean
	 */
/*	@Bean
	public ISqlInjector sqlInjector() {
		return new LogicSqlInjector();
	}*/

	/**
	 * 执行分析插件
	 * 可以阻断全表更新、删除的操作，避免误操作。
	 */
	@Bean
	public SqlExplainInterceptor sqlExplainInterceptor(){
		SqlExplainInterceptor sqlExplainInterceptor=new SqlExplainInterceptor();
		List<ISqlParser> sqlParserList=new ArrayList<>();
		sqlParserList.add(new BlockAttackSqlParser());
		sqlExplainInterceptor.setSqlParserList(sqlParserList);
		return sqlExplainInterceptor;
	}

}
