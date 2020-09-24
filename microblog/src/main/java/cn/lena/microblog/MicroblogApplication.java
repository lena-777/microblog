package cn.lena.microblog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@MapperScan("cn.lena.microblog.mapper")		//放在mybatis的配置文件中进行配置
@SpringBootApplication
public class MicroblogApplication {


	public static void main(String[] args) throws Exception {

		SpringApplication.run(MicroblogApplication.class, args);

	}

}
