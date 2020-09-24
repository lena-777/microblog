package cn.lena.microblog.service;

import cn.lena.microblog.domain.entity.Users;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lena
 * @since 2020-07-11
 */
public interface UsersService extends IService<Users> {

	//t取消关注
	void cancelAttention(Integer attId, Integer beAttId);

	//t关注用户
	Integer insertFans(Integer attId, Integer beAttId);

	//t查询关注的人
	List<Users> findAttention(Integer userId);

	//t查询粉丝
	List<Users> findFans(Integer userId);

	//t根据ID查询用户
	Users findById(Integer userId);

	//t登录
	Users login(Integer userId,String password);

	//t注册
	Integer register(Users user);

	//查询粉丝数较高的前8人
	List<Users> findTopUser();

}
