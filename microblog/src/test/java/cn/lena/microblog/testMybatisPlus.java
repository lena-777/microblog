package cn.lena.microblog;

import cn.lena.microblog.domain.entity.Users;
import cn.lena.microblog.mapper.UsersMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class testMybatisPlus {

	@Autowired
	private UsersMapper usersMapper;

	//查询数据库连接
	@Test
	public void testConnection(){
		List<Users> users = this.usersMapper.selectList(null);
		for (Users u:users){
			System.out.println(u.getUsername());
		}
	}

	//测试selectOne方法
	@Test
	public void testSelectOne(){
		QueryWrapper<Users> query=new QueryWrapper<>();
		query.eq("userId",1001).eq("password","123456");
		Users users = this.usersMapper.selectOne(query);
		System.out.println(users.getUsername());
	}

	//查询所有
	@Test
	public void testSelectList(){
		List<Users> users = this.usersMapper.selectList(null);
		for (Users user : users) {
			System.out.println(user.getUsername());
		}

	}
}
