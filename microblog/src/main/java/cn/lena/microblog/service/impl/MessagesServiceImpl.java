package cn.lena.microblog.service.impl;

import cn.lena.microblog.domain.entity.Discuss;
import cn.lena.microblog.domain.entity.Messages;
import cn.lena.microblog.mapper.DiscussMapper;
import cn.lena.microblog.mapper.MessagesMapper;
import cn.lena.microblog.service.MessagesService;
import cn.lena.microblog.service.PassageService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

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
public class MessagesServiceImpl extends ServiceImpl<MessagesMapper, Messages> implements MessagesService {

	@Autowired
	private MessagesMapper messagesMapper;

	@Autowired
	private DiscussMapper discussMapper;

	@Autowired
	private PassageService passageService;

	@Override//t添加消息
	public Integer insert(Messages messages) {
		if (messages.getSentUserId().equals(null)||messages.getReceiveUserId().equals(null))
			return 0;
		return messagesMapper.insert(messages);
	}

	@Override//t根据接收人查询消息 分页 按接收时间排序（查询某一用户收到的消息）;
	public IPage<Messages> findByUserId(Integer userId, IPage<Messages> page) {
		QueryWrapper<Messages> query=new QueryWrapper<>();
		//mybatis-plus会自动携带deleted=0的条件
		query.eq("receiveUserId",userId).orderByDesc("operationTime");
		return messagesMapper.selectPage(page,query);
	}

	@Override//t评论(disId:评论的人的Id,beDisId:被评论人的Id)
	public Integer discuss(Integer disId, Integer beDisId, Discuss discuss) {
		Messages messages=new Messages();
		messages.setSentUserId(disId);
		messages.setReceiveUserId(beDisId);
		messages.setOperation(2);//2表示评论
		messages.setOperationTime(new Date());
		messages.setPassageId(discuss.getPassageId());
		messages.setDiscussContent(discuss.getContext());		//评论内容
		if (discuss.getTime()==null){
			discuss.setTime(new Date());
		}
		discussMapper.insert(discuss);
		return messagesMapper.insert(messages);
	}

	@Override//t点赞(likeId:点赞的人的Id,beLikeId:被点赞人的Id)
	public Integer liked(Integer likeId, Integer beLikeId,Integer passageId) {
		Messages messages=new Messages();
		messages.setSentUserId(likeId);
		messages.setReceiveUserId(beLikeId);
		messages.setOperation(1);//1表示点赞
		messages.setOperationTime(new Date());
		messages.setPassageId(passageId);
		passageService.updateLiked(passageId,1);	//添加点赞数
		return messagesMapper.insert(messages);
	}

	@Override//t关注(attId:关注的人的Id,beAttId:被关注人的Id)
	public Integer attention(Integer attId, Integer beAttId) {
		Messages messages=new Messages();
		messages.setSentUserId(attId);
		messages.setReceiveUserId(beAttId);
		messages.setOperation(3);//3表示关注
		messages.setOperationTime(new Date());
		return messagesMapper.insert(messages);
	}

	@Override//t删除消息
	public Integer delete(Integer messageId) {
		Messages messages = messagesMapper.selectById(messageId);
		if (messages.getOperation()==1){		//删除的是点赞操作
			passageService.updateLiked(messages.getPassageId(),-1);		//删除当前文章的点赞数
		}
		return messagesMapper.deleteById(messageId);
	}

}
