package cn.lena.microblog.service.impl;

import cn.lena.microblog.domain.entity.Messages;
import cn.lena.microblog.domain.entity.Users;
import cn.lena.microblog.domain.utils.CacheKey;
import cn.lena.microblog.domain.utils.SplitUtils;
import cn.lena.microblog.mapper.MessagesMapper;
import cn.lena.microblog.mapper.UsersMapper;
import cn.lena.microblog.service.CatagoryService;
import cn.lena.microblog.service.MessagesService;
import cn.lena.microblog.service.UsersService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lena
 * @since 2020-07-11
 */
@Service
@Transactional
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements UsersService {

	@Autowired
	private UsersMapper usersMapper;

	@Autowired
	private MessagesService messagesService;

	@Autowired
	private MessagesMapper messagesMapper;

	@Autowired
	private CatagoryService catagoryService;

	@Autowired
	private RedisTemplate redisTemplate;

	@Override	//取消关注
	public void cancelAttention(Integer attId, Integer beAttId) {
		Users att = usersMapper.selectById(attId);
		Users beAtt = usersMapper.selectById(beAttId);
		//修改关注的人的用户信息
		UpdateWrapper<Users> query=new UpdateWrapper<>();
		String s1 = att.getAttentionId();
		s1=s1.replaceAll(beAttId+",","");//删除关注的ID
		s1=s1.replaceAll(beAttId+"","");
		//att.setAttentionId(s1);
		query.set("attentionId",s1).eq("userId",attId);
		usersMapper.update(att,query);//更新用户信息
		//修改被关注的人的用户信息
		UpdateWrapper<Users> query1=new UpdateWrapper<>();
		String s2=beAtt.getFansId();
		s2=s2.replaceAll(attId+",","");
		s2=s2.replaceAll(attId+"","");//删除粉丝的ID
		//beAtt.setFansId(s2);
		//beAtt.setFans(beAtt.getFans()-1);//粉丝数-1
		query1.set("fansId",s2).set("fans",beAtt.getFans()-1).eq("userId",beAttId);
		usersMapper.update(beAtt,query1);//更新用户信息
		//删除中间表信息
		messagesMapper.delete(new QueryWrapper<Messages>()
				.eq("operation",3).eq("sentUserId",attId).eq("receiveUserId",beAttId));
		saveToRedis(att);	//更新缓存信息
	}

	@Override	//关注(attId:关注的人的Id,beAttId:被关注人的Id)
	public Integer insertFans(Integer attId, Integer beAttId) {
		//修改关注的人的用户信息
		Users att = usersMapper.selectById(attId);
		Users beAtt = usersMapper.selectById(beAttId);
		if (att.getAttentionId()==null||att.getAttentionId().equals("")){
			att.setAttentionId(beAttId+"");
		}else {
			att.setAttentionId(att.getAttentionId()+","+beAttId);	//添加粉丝ID
		}
		att.setFans(att.getFans()+1);		//粉丝数目+1
		//att.update(query);	//更新用户数据
		att.updateById();
		//修改被关注的人的用户信息
		if (beAtt.getFansId()==null||beAtt.getFansId().equals("")){
			beAtt.setFansId(attId+"");		//添加关注人的ID
		}else {
			beAtt.setFansId(beAtt.getFansId()+","+attId);
		}
		beAtt.updateById();		//更新用户数据
		saveToRedis(att);			//更新缓存信息
		return messagesService.attention(attId,beAttId);	//添加关注消息
	}

	@Override	//查询关注的人
	public List<Users> findAttention(Integer userId) {
		Users u=findUserFromRedis();		//从缓存中获取当前对象
		Users user;
		if (u!=null){		//缓存中取
			user=u;
		}
		else {			//从数据库中取
			//查询当前user对象的数据信息
			user = usersMapper.selectById(userId);
		}
		//获取关注的人的Id列表
		String attentionId = user.getAttentionId();
		//获取关注用户列表
		List<Integer> list = SplitUtils.stringToList(attentionId);
		List<Users> users=new ArrayList<>();
		for (Integer id : list) {
			users.add(usersMapper.selectById(id));
		}
		return users;
	}

	@Override	//查询粉丝
	public List<Users> findFans(Integer userId) {
		Users u=findUserFromRedis();		//从缓存中获取当前对象
		Users user;
		if (u!=null){		//缓存中取
			user=u;
		}
		else {			//从数据库中取
			//查询当前user对象的数据信息
			user = usersMapper.selectById(userId);
		}
		//获取粉丝Id列表
		String fansId = user.getFansId();
		List<Integer> list = SplitUtils.stringToList(fansId);
		//获取粉丝集合
		List<Users> users=new ArrayList<>();
		for (Integer id : list) {
			users.add(usersMapper.selectById(id));
		}
		return users;
	}

	//从缓存查找当前登录的user对象
	public Users findUserFromRedis(){
		return (Users)redisTemplate.boundValueOps(CacheKey.USER.toString()).get();
	}

	@Override	//t根据ID查询用户
	public Users findById(Integer userId) {
		if (userId==0){
			return null;
		}
		Users users = usersMapper.selectById(userId);
		return users;
	}

	//更新缓存对象==>重新保存，会覆盖原先的数据
	public void saveToRedis(Users users){
		//redisTemplate.delete(CacheKey.USER.toString());		//清除原先的缓存数据
		redisTemplate.boundValueOps(CacheKey.USER.toString()).set(users);		//将用户存入缓存中
	}

	@Override	//登录
	public Users login(Integer userId, String password){
		QueryWrapper<Users> query=new QueryWrapper<>();
		query.eq("userId",userId).eq("password",password);
		Users users = usersMapper.selectOne(query);
		saveToRedis(users);		//存入缓存
		System.out.println(users);
		return users;
	}

	@Override	//注册
	public Integer register(Users user) {
		Date date=new java.sql.Date(new Date().getTime());
		user.setJoinDate(date);
		user.setLastTime(date);
		user.setCatagory(1);
		//新用户注册若没有设置头像
		if (user.getAvater()==""){
			//通过性别添加头像
			if (user.getSex()=="女"){
				user.setAvater("../static/img/girl.jpg");
			}
			//没有设置性别，或者性别为男，都默认使用男生头像
			else{
				user.setAvater("../static/img/boy.jpg");
			}
		}
		catagoryService.insert(user.getUserId());
		int i = usersMapper.insert(user);
		//i指的是受到影响的行数，如果添加成功，即影响行数为1
		if (i==1){
			saveToRedis(user);		//存入缓存
			//执行完insert方法之后，获取的自增长id值会自动填充到user对象中，只需要使用getId的方法即可获得id值。
			return user.getUserId();
		}
		else return 0;
	}

	@Override//查询粉丝数较高的前8人
	public List<Users> findTopUser() {
		QueryWrapper<Users> query=new QueryWrapper<>();
		query.orderByDesc("fans").eq("status",1);
		List<Users> users=usersMapper.selectList(query);
		return users.subList(0,9);
	}


}
