package cn.lena.microblog;

import cn.lena.microblog.service.CatagoryService;
import cn.lena.microblog.service.KeywordService;
import cn.lena.microblog.service.PassageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class testServiceImpl {

	@Autowired
	private KeywordService keywordService;

	@Autowired
	private CatagoryService catagoryService;

	@Autowired
	private PassageService passageService;

	@Autowired
	private RedisTemplate redisTemplate;

	@Test
	public void testCatagoryInsert(){
			Integer insert = catagoryService.insert(1010);
		System.out.println(insert);
	}

	@Test
	public void test(){
		System.out.println("丹杰喜欢吃屎");
	}

	//测试catagory的updatePassageNum方法
/*	@Test
	public void testCatagoryUpdatePassageNum(){
		Boolean aBoolean = passageService.updatePassageNum(1001, 1, -1);
		System.out.println(aBoolean);
	}*/

	//t测试keyword的updatePassageNum方法
/*	@Test
	public void testKeywordUpdatePassageNum(){
		Boolean aBoolean = passageService.updatePassageNum(2001, -1);
		System.out.println(aBoolean);
	}*/
}
