package cn.lena.microblog;

import cn.lena.microblog.domain.entity.Users;
import cn.lena.microblog.mapper.PassageMapper;
import cn.lena.microblog.mapper.UsersMapper;
import cn.lena.microblog.service.PassageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class testClassOrAnnotation {

	@Autowired
	private PassageMapper passageMapper;

	@Autowired
	private UsersMapper usersMapper;

	@Autowired
	private PassageService passageService;




	//测试string的replace方法
	@Test
	public void testReplace(){
		String s="1002,1003,1007,1001";
		s=s.replaceAll(",1001","");
		System.out.println(s);
		System.out.println("-----------------");
		String s1="1001";
		s1=s1.replaceAll(",1001","");
		System.out.println(s1);
		System.out.println("-----------------");
		String s2="1001,1007";
		s2=s2.replaceAll(",1001","");
		s2=s2.replaceAll("1001","");
		System.out.println(s2);
		System.out.println("-----------------");
		String s3="1001,1007,1008";
		s3=s3.replaceAll("1001,","");
		System.out.println(s3);
	}

	//测试list
	@Test
	public void testArrayList(){
		List<Integer> list=new ArrayList<>();
		list.add(1);
		list.add(2);
		list.add(3);

		System.out.println(list.get(0));
	}

	//测试mybatisplus的逻辑删除功能
	@Test
	public void testDeleted(){
		int i = usersMapper.deleteById(1012);
		System.out.println("影响行数为："+i);
	}

	//测试分割数据split
	@Test
	public void testSplit(){
		String s = usersMapper.selectById(1001).getCollect();
	//	System.out.println("s:"+s);
		String[] split = s.split(",");
	//	System.out.println("split:"+split[0]+","+split[1]);
		for (String s1 : split) {
			int i = Integer.parseInt(s1);
			System.out.println(i);
		}
	}

	//t:map获取数组value
	@Test
	public void testMap(){
		Map<String,int[]> map=new HashMap<>();
		int[] a={1,2,3};
		map.put("ids",a);
		int[] ids = map.get("ids");
		System.out.println(ids[2]);
	}

	@Autowired
	ObjectMapper objectMapper;

	//测试json序列化
	@Test
	public void testJsonSerialize() throws JsonProcessingException {
/*
		test t=new test();
		int a[]={1,2,3};
		t.setA(a);
		t.setName("aaa");
		String s = objectMapper.writeValueAsString(t);
		System.out.println(s);
*/

	}

	//转换json格式数据
	@Test
	public void testJsonArray(){
		String s = usersMapper.selectById(1001).getCollect();
/*		Map<String,int[]> map = (Map) JSON.parse(s);
		int[] ids = map.get("fansId");
		for (int id : ids) {
			System.out.println(id);
		}*/


/*		QueryWrapper<Passage> query=new QueryWrapper<>();
		query.in("passageId",ids).eq("isDelete",0).eq("isPrivate",0).eq("isPublish",1);
		List<Passage> passages = passageMapper.selectList(query);
		for (Passage passage : passages) {
			System.out.println("you");
			System.out.println(passage.getTitle());
		}
		System.out.println("over");*/
	}

	//获取json格式数据
	@Test
	public void testJson(){
		String s = usersMapper.selectById(1001).getCollect();
	//	Map<String,Object> map = (Map) JSON.parse(s);	//fastjon
	//	System.out.println(map.get("passageId"));
	}

	//测试IPage
	@Test
	public void testIPage(){
/*
		Page<Passage> page=new Page<>(1,2);
		QueryWrapper<Passage> query=new QueryWrapper<>();
		query.like("title","u").eq("isDelete",0).eq("isPrivate",0).eq("isPublish",1);		//未删除的文章;
		IPage<Passage> iPage = passageMapper.selectPage(page, query);
		System.out.println(iPage.getTotal());
		List<Passage> records = iPage.getRecords();
		for (Passage record : records) {
			System.out.println(record.getTitle());
		}
*/

	}

	//测试注解@JsonFormat
	//@JsonFormat:使得在数据库中显示的是正确的时间格式，获取后也是正确的时间格式
	@Test
	public void testJsonFormat(){
		Users u=new Users();
		u.setJoinDate(new Date());
		System.out.println(u);
	}

	//测试格式化日期
	@Test
	public void testCalendar() throws ParseException {

		//使用Calendar输出的是没格式化的日期
/*		Calendar c=Calendar.getInstance();
		System.out.println(c.getTime());*/

		//使用SimpleDateFormat格式化日期
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		String format = sdf.format(new Date());
		System.out.println(format);		//2020-07-20

		Date parse = sdf.parse(format);
		System.out.println(parse);		//Mon Jul 20 00:00:00 CST 2020

		Date date = new java.sql.Date(parse.getTime());
		System.out.println(date);		//2020-07-20

		Date d=new java.sql.Date(new Date().getTime());
		System.out.println(d);			//2020-07-20

		//使用System.currentTimeMillis()获取时间,也是没有格式化的日期
/*		long l = System.currentTimeMillis();
		System.out.println(l);*/
	}
}
