package cn.lena.microblog.config;

import cn.lena.microblog.service.KeywordService;
import cn.lena.microblog.service.PassageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class InitRedisData implements CommandLineRunner {

	@Override
	public void run(String... args) throws Exception {
		initKeyword();
		initPassages();
	}

	@Autowired
	private KeywordService keywordService;

	public void initKeyword(){
		System.out.println("初始化:keyword");
		keywordService.findTopTenToRedis();
	}

	@Autowired
	private PassageService passageService;

	public void initPassages(){
		System.out.println("初始化:passages");
		passageService.findAllToRedis();
	}
}
