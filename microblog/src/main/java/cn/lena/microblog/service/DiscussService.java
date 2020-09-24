package cn.lena.microblog.service;

import cn.lena.microblog.domain.entity.Discuss;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lena
 * @since 2020-07-24
 */
public interface DiscussService extends IService<Discuss> {

	//t添加评论
	Integer insert(Discuss discuss);

	//t修改评论内容
	Integer update(Discuss discuss);

	//t删除评论（同时删除子评论）
	Integer delete(Discuss discuss);

	//t根据文章id查询评论（子评论存放在评论中）
	List<Discuss> findByPassageId(Integer passageId);


}
