package cn.lena.microblog.service;

import cn.lena.microblog.domain.entity.Discuss;
import cn.lena.microblog.domain.entity.Messages;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lena
 * @since 2020-07-11
 */
public interface MessagesService extends IService<Messages> {

	//t添加消息
	Integer insert(Messages messages);

	//t根据接收人查询消息 分页 按接收时间排序（查询某一用户收到的消息）;
	IPage<Messages> findByUserId(Integer userId, IPage<Messages> page);

	//t评论(disId:评论的人的Id,beDisId:被评论人的Id)
	Integer discuss(Integer disId,Integer beDisId,Discuss discuss);

	//t点赞(likeId:点赞的人的Id,beLikeId:被点赞人的Id)
	Integer liked(Integer likeId,Integer beLikeId,Integer passageId);

	//t关注(attId:关注的人的Id,beAttId:被关注人的Id)
	Integer attention(Integer attId,Integer beAttId);

	//x发送消息（添加消息到表中）（按消息类型展开）

	//x群发消息（向粉丝发送消息）(先不实现)

	//t删除消息（删除评论后同时删除消息:不进行实现，无法确定是哪一条消息）
	Integer delete(Integer messageId);


}
