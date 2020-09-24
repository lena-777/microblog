package cn.lena.microblog;

import cn.lena.microblog.domain.entity.Keyword;
import cn.lena.microblog.domain.utils.CacheKey;
import cn.lena.microblog.service.KeywordService;
import cn.lena.microblog.service.PassageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class testRedis {

	@Autowired
	private KeywordService keywordService;

	@Autowired
	private PassageService passageService;

	@Autowired
	private RedisTemplate redisTemplate;

	@Test	//测试存储后取出list数据
	public void testBoundsValue(){
		redisTemplate.delete("test");
		List<Integer> list=new ArrayList<>();
		list.add(5);
		list.add(4);
		list.add(3);
		list.add(2);
		list.add(1);
		redisTemplate.boundValueOps("test").set(list);
		List<Integer> test = (List<Integer>) redisTemplate.boundValueOps("test").get();
		for (Integer integer : test) {
			System.out.println(integer);
		}
	}

	@Test //测试List取出数据值
	public void testListGet(){
		redisTemplate.delete("test");
		List<Integer> list=new ArrayList<>();
		list.add(5);
		list.add(4);
		redisTemplate.boundListOps("test").rightPush(list);
		list.add(5);
		list.add(4);
		list.add(3);
		list.add(2);
		redisTemplate.boundListOps("test").rightPush(list);
		list.add(5);
		list.add(4);
		list.add(3);
		list.add(2);
		list.add(1);
		redisTemplate.boundListOps("test").rightPush(list);
		List<Integer> test = redisTemplate.boundListOps("test").range(0,1);
		System.out.println(test);
	}

	@Test //测试List的右压栈排序
	public void testListRightPush(){
		redisTemplate.delete("test");
		List<Integer> list=new ArrayList<>();
		list.add(5);
		list.add(4);
		list.add(3);
		redisTemplate.boundListOps("test").rightPush(list);
		List<Integer> test =redisTemplate.boundListOps("test").range(0, -1);
		System.out.println(test);
	}

	@Test //测试缓存重新保存是否会覆盖
	public void testRedisTemplateCover(){
		redisTemplate.boundValueOps("test").set("原来的");
		System.out.println(redisTemplate.boundValueOps("test").get());
		redisTemplate.boundValueOps("test").set("成功覆盖");
		System.out.println(redisTemplate.boundValueOps("test").get());
	}

	@Test //测试redisTemplate
	public void testRedisTemplate(){
		redisTemplate.boundValueOps("test").set("成功！");
		Object test = redisTemplate.boundValueOps("test").get();
		System.out.println(test);
	}

	@Test //测试redisTemplate能否成功存取
	public void testKeywordFindTopTenToRedis(){
		List<Keyword> records=new ArrayList<>();
		records.add(new Keyword().setKeywordName("111"));
		records.add(new Keyword().setKeywordName("222"));
		records.add(new Keyword().setKeywordName("333"));
		System.out.println("存数据");
		redisTemplate.boundValueOps(CacheKey.KEYWORD_TOP_TEN.toString()).set(records);
		System.out.println(CacheKey.KEYWORD_TOP_TEN.toString());
		System.out.println("取数据");
		List<Keyword> keywords = (List<Keyword>) redisTemplate.boundValueOps(CacheKey.KEYWORD_TOP_TEN.toString()).get();
		for (Keyword keyword : keywords) {
			System.out.println(keyword.getKeywordName());
		}
	}

	@Test //测试redis是否能够初始化存数据 后成功从缓存中取出数据
	public void testRedisInit(){
		List<Keyword> topTen = keywordService.findTopTen();
		if (topTen==null){
			System.out.println("null");
		}
		for (Keyword keyword : topTen) {
			System.out.println(keyword.getKeywordName());
		}
		//System.out.println("Test");
	}

/*	@Test //测试缓存连接

	public void testEhcache(){
		IPage<Passage> page=new Page<>(1,3);
		System.out.println("------第一次查询----");
		passageService.findAll(page);
		System.out.println("------第二次查询----");
		passageService.findAll(page);
	}
*/

}
