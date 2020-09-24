package cn.lena.microblog.controller;


import cn.lena.microblog.domain.entity.Discuss;
import cn.lena.microblog.domain.utils.Msg;
import cn.lena.microblog.service.DiscussService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lena
 * @since 2020-07-24
 */
@Controller
@RequestMapping("/discuss")
public class DiscussController {

	@Autowired
	private DiscussService discussService;

	//t添加评论
	@RequestMapping("/insert")
	public Msg insert(Discuss discuss){
		Integer result = discussService.insert(discuss);
		if (result==1){		//添加成功
			return new Msg().success("添加成功");
		}
		return new Msg().fail("添加失败");
	}

	//t修改评论内容
	@RequestMapping("/update")
	public Msg update(Discuss discuss){
		Integer result = discussService.update(discuss);
		if (result==1){		//添加成功
			return new Msg().success("修改成功");
		}
		return new Msg().fail("修改失败");
	}

	//t删除评论（同时删除子评论）
	@RequestMapping("/delete")
	public Msg delete(Discuss discuss){
		Integer result = discussService.delete(discuss);
		if (result==1){		//添加成功
			return new Msg().success("删除成功");
		}
		return new Msg().fail("删除失败");
	}

	//t根据文章id查询评论（子评论存放在评论中）
	@RequestMapping("/findByPassageId")
	@ResponseBody	//以json格式响应
	public List<Discuss> findByPassageId(Integer passageId) {
		return discussService.findByPassageId(passageId);
	}
}
