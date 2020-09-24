package cn.lena.microblog.controller;


import cn.lena.microblog.domain.entity.Discuss;
import cn.lena.microblog.domain.entity.Messages;
import cn.lena.microblog.domain.utils.Msg;
import cn.lena.microblog.service.MessagesService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lena
 * @since 2020-07-11
 */
@Controller
@RequestMapping("/messages")
public class MessagesController {

	@Autowired
	private MessagesService messagesService;

	//t添加消息
	@RequestMapping("/insert")
	public Msg insert(Messages messages){
		Integer result = messagesService.insert(messages);
		if (result==1)
			return new Msg().success("添加消息成功");
		return new Msg().fail("添加消息失败");
	}


	//t根据接收人查询消息 分页 按接收时间排序（查询某一用户收到的消息）;
	@RequestMapping("/findByUserId")
	public List<Messages> findByUserId(Integer current, Integer size, Integer userId){
		IPage<Messages> page=new Page<>(current,size);
		return messagesService.findByUserId(userId, page).getRecords();		//返回当前页数据

	}

	//t评论(disId:评论的人的Id,beDisId:被评论人的Id)
	@RequestMapping("/discuss")
	public Msg insertDis(Integer attId, Integer beAttId, Discuss discuss){
		Integer result = messagesService.discuss(attId,beAttId,discuss);
		if (result==1)
			return new Msg().success("添加评论消息成功");
		return new Msg().fail("添加评论消息失败");
	}

	//t点赞(likeId:点赞的人的Id,beLikeId:被点赞人的Id) 文章点赞数没有+1
	@RequestMapping("/liked")
	public Msg insertLiked(Integer attId, Integer beAttId,Integer passageId){
		Integer result = messagesService.liked(attId,beAttId,passageId);
		if (result==1)
			return new Msg().success("添加消息成功");
		return new Msg().fail("添加消息失败");
	}

	//在用户添加关注时user表会触发该方法，该消息相当于一个日志表，会一并记录，因此不会有单独调用的可能。
	//关注(attId:关注的人的Id,beAttId:被关注人的Id) 添加关注的用户users表
/*	@RequestMapping("/attention")
	public Msg insertAtt(Integer attId, Integer beAttId){
		Integer result = messagesService.attention(attId,beAttId);
		if (result==1)
			return new Msg().success("添加消息成功");
		return new Msg().fail("添加消息失败");
	}*/

	//t删除消息
	@RequestMapping("/delete")
	public Msg delete(Integer messageId){
		Integer result = messagesService.delete(messageId);
		if (result==1)
			return new Msg().success("删除成功");
		return new Msg().fail("删除失败");

	}

}
