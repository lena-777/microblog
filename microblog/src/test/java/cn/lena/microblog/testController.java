package cn.lena.microblog;

import cn.lena.microblog.controller.*;
import cn.lena.microblog.domain.entity.*;
import cn.lena.microblog.domain.utils.Msg;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class testController {

	@Autowired
	private PassageController passageController;

	@Autowired
	private UsersController usersController;

	@Autowired
	private MessagesController messagesController;

	@Autowired
	private KeywordController keywordController;

	@Autowired
	private DiscussController discussController;

	@Autowired
	private CatagoryController catagoryController;



	@Test		//评论
	public void testMessagesDiscuss(){
		Discuss d=new Discuss();
		d.setContext("写的太好了！").setPassageId(4003);
		messagesController.insertDis(1002,1001,d);
	}

	@Test
	public void testMessagesFindByUserId(){
		List<Messages> byUserId = messagesController.findByUserId(1, 10, 1001);
		for (Messages messages : byUserId) {
			System.out.println(messages.getSentUserId()+":"+messages.getOperation()+"--"+messages.getDiscussContent());
		}
	}

	@Test		//修改普通分类==>成功
	public void testCatagoryUpdate2(){
		Catagory c=new Catagory();
		c.setUserId(1010).setCatagoryId(2).setCatagoryName("修改");
		Msg update = catagoryController.update(c);
		System.out.println(update.getMsg());
	}

	@Test		//修改默认分类==>失败
	public void testCatagoryUpdate(){
		Catagory c=new Catagory();
		c.setUserId(1010).setCatagoryId(1).setCatagoryName("test");
		Msg update = catagoryController.update(c);
		System.out.println(update.getMsg());
	}

	@Test		//看该类下文章是否会转移、用户表下分类数目-1、分类表deleted=1
	public void testCatagoryDelete2(){
		Catagory c=new Catagory();
		c.setCatagoryId(2).setUserId(1001);
		catagoryController.delete(c);
	}

	@Test
	public void testCatagoryDelete(){
		Catagory c=new Catagory();
		c.setCatagoryId(2).setUserId(1011);
		catagoryController.delete(c);
	}

	@Test
	public void testCatagoryFindPassage(){
		Catagory c=new Catagory();
		c.setUserId(1001).setCatagoryId(2);
		List<Passage> passages = catagoryController.findPassages(c, 1, 3);
		for (Passage passage : passages) {
			System.out.println(passage.getTitle());
		}
	}

	@Test
	public void testCatagoryFindAll(){
		List<Catagory> all = catagoryController.findAll(1001);
		for (Catagory catagory : all) {
			System.out.println(catagory.getCatagoryName());
		}
	}

	@Test
	public void testCatagoryInsert2(){
		Catagory c=new Catagory();
		c.setUserId(1011).setCatagoryName("操作系统");
		catagoryController.insert(c);
	}

	@Test
	public void testCatagoryInsert(){
		Catagory c=new Catagory();
		c.setUserId(1003).setCatagoryName("学习");
		catagoryController.insert(c);
	}

	@Test
	public void testDiscussFindByPassageId(){
		List<Discuss> byPassageId = discussController.findByPassageId(4002);
		for (Discuss discuss : byPassageId) {
			System.out.println(discuss);
		}
	}

	@Test
	public void testDiscussDelete(){
		Msg delete = discussController.delete(new Discuss().setDiscussId(5008));
		System.out.println(delete.getMsg());
	}

	@Test
	public void testDiscussUpdate(){
		Discuss d=new Discuss();
		d.setDiscussId(5008);
		d.setContext("说的太好了！");
		discussController.update(d);
	}

	@Test
	public void testDiscussInsert(){
		Discuss d=new Discuss();
		d.setPassageId(4005).setUserId(1004).setContext("通俗易懂！");
		Msg insert = discussController.insert(d);
		System.out.println(insert.getMsg());
	}

	@Test
	public void testKeywordFindPage(){
		List<Keyword> java = keywordController.findAll(1,6);
		for (Keyword k : java) {
			System.out.println(k.getKeywordName());
		}
	}

	@Test
	public void testKeywordFindTop(){
		List<Keyword> java = keywordController.findTop();
		for (Keyword k : java) {
			System.out.println(k.getKeywordName());
		}
	}

	@Test
	public void testKeywordFindPassageByKeyword(){
	/*	List<Passage> java = keywordController.findPassageByKeyword("JAVA", 1, 5);
		for (Passage passage : java) {
			System.out.println(passage.getTitle());
		}	*/
	}

	@Test
	public void testKeywordFindByName(){
		List<Keyword> test = keywordController.findByName("开发", 1, 5);
		for (Keyword keyword : test) {
			System.out.println(keyword.getKeywordName());
		}
	}

	@Test
	public void testKeywordInsert(){
		Keyword k=new Keyword();
		k.setKeywordName("test");
		keywordController.insert(k);
	}

	@Test
	public void testUsersCancelAttention(){
		usersController.cancelAttention(1007,1002);
	}

	@Test
	public void testUsersAttention(){
		Msg attention = usersController.attention(1003, 1002);
		System.out.println(attention.getMsg());
	}

	@Test
	public void testMessagesInsertLiked(){
		Msg msg = messagesController.insertLiked(1003,1001,4001);
		System.out.println(msg.getMsg());
	}

	@Test
	public void testMessagesInsertAtt(){
/*		Msg msg = messagesController.insertAtt(1003, 1005);
		System.out.println(msg.getMsg());*/
	}

	@Test
	public void testMessagesDelete(){
		Msg delete = messagesController.delete(3012);
		System.out.println(delete.getMsg());
	}

	@Test
	public void testMessagesInsert(){
		Messages m=new Messages();
		m.setSentUserId(1002).setReceiveUserId(1003).setPassageId(4005).setOperation(1);
		Msg insert = messagesController.insert(m);
		System.out.println(insert.getCode());
	}

	@Test
	public void testPassageUpdate(){
		Passage p = passageController.findByPassageId(4006);
		p.setTitle("update");
		Msg update = passageController.update(p);
		System.out.println(update.getMsg());
	}

	@Test
	public void testUsersFindOne(){
		System.out.println(usersController.findById(1001));
	}

	//t测试PassageController的查询关注的人方法
	@Test
	public void testFindAttention(){
		List<Users> att = usersController.findAttention(1001);
		for (Users fan : att) {
			System.out.println(fan);
		}
	}

	//t测试PassageController的查询粉丝方法
	@Test
	public void testFindFans(){
		List<Users> fans = usersController.findFans(1001);
		for (Users fan : fans) {
			System.out.println(fan);
		}
	}

	//t测试PassageController的insert方法
	@Test
	public void testInsert(){
		Passage p=new Passage();
		p.setUserId(1001);
		p.setTitle("test");
		p.setContent("insert...");
		p.setDescribed("...");
	//	p.setCatagory("1");
		Msg insert = passageController.insert(p);
		System.out.println(insert);
	}

	//t测试PassageController的update方法
	@Test
	public void testUpdate(){
		Passage passage = passageController.findByPassageId(4005);
		passage.setTitle("更新");
		passageController.update(passage);
	}

	//t测试PassageController的delete方法
	@Test
	public void testDelete(){
		Msg msg = passageController.deletePassage(4005);
		System.out.println(msg);
	}

	//测试PassageController的findCollectionByUserId方法
	@Test
	public void testFindCollectionByUserId(){
		List<Passage> passages = passageController.findCollectionByUserId(1001, 1, 2);
		for (Passage passage : passages) {
			System.out.println(passage.getTitle());
		}
	}

	//查询出的数据在postman出现中文乱码问题
	@Test
	public void testFindByPassageId(){
	//	Msg msg = passageController.findByPassageId(4001);
	//	Map<String, Object> extend = msg.getExtend();
	//	System.out.println(extend);
	}
}
