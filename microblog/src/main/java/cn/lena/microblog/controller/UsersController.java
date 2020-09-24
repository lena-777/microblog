package cn.lena.microblog.controller;


import cn.lena.microblog.domain.entity.Users;
import cn.lena.microblog.domain.utils.Msg;
import cn.lena.microblog.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lena
 * @since 2020-07-11
 */
@RestController
@RequestMapping("/users")
public class UsersController {

	@Autowired
	private UsersService usersService;

	//查询前几个关注度高的作者
	@RequestMapping("/findTop")
	public List<Users> findTopUsers(){
		return usersService.findTopUser();
/*		List<Users> user = usersService.findTopUser();
		System.out.println(user);
		return user;*/
	}

	//取消关注
	@RequestMapping("/cancelAttention")
	public void cancelAttention(Integer attId, Integer beAttId){
		usersService.cancelAttention(attId,beAttId);
	}

	//t关注(attId:关注的人的Id,beAttId:被关注人的Id)
	@RequestMapping("/attention")
	public Msg attention(Integer attId, Integer beAttId){
		Integer result = usersService.insertFans(attId, beAttId);
		if (result==1){
			return new Msg().success("关注成功！");
		}
		return new Msg().fail("关注失败！");
	}

	//t根据ID查询用户
	@RequestMapping("/findOne")
	public Users findById(Integer userId){
		Users u= usersService.findById(userId);
		System.out.println(u);
		return u;
	}

	//t查询关注的人
	@RequestMapping("/findAttention")
	public List<Users> findAttention(Integer userId) {
		return usersService.findAttention(userId);
	}

	//t查询粉丝
	@RequestMapping("/findFans")
	public List<Users> findFans(Integer userId){
		return usersService.findFans(userId);
	}

	//t查询所有
	@RequestMapping("/findAll")
	public List<Users> findAll(){
		return usersService.list();
	}

	//t登录
	@RequestMapping("/login")
	public Users login(Integer userId, String password){
		return usersService.login(userId, password);
	}

	//t注册
	@RequestMapping("/register")
	public Users register(@RequestBody Users user){
		System.out.println(user);
		int result = usersService.register(user);	//返回的result是注册成功用户的ID
		if (result!=0){			//注册成功
			return usersService.findById(result);
		}
		return null;
	}


	//t登录
/*	@RequestMapping("/login")
	public Msg login(Integer userId, String password){
		Users u= usersService.login(userId, password);
		if (u.equals(null)){
			return new Msg().fail();
		}
		else return new Msg().success().add("users",u);
	}*/

	//t注册
	//使用@RequestBody将前台传来的json数据转换成user对象
/*	@RequestMapping("/register")
	public Msg register(@RequestBody Users user){
		int result = usersService.register(user);	//返回的result是注册成功用户的ID
		if (result!=0){			//注册成功
			Users u = usersService.findById(result);
			return new Msg().success().add("users",u);
		}

		return new Msg().fail();
	}*/

}
