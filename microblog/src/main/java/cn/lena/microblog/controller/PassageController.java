package cn.lena.microblog.controller;


import cn.lena.microblog.domain.entity.Passage;
import cn.lena.microblog.domain.utils.Msg;
import cn.lena.microblog.service.PassageService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
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
@RequestMapping("/passage")
public class PassageController {

	@Autowired
	private PassageService passageService;

	//t发表文章/存入草稿箱中：取决于isPublish的值
	@RequestMapping("/insert")
	public Msg insert(Passage passage){
		Integer result = passageService.insert(passage);
		if (result==1)
			return new Msg().success("发表成功");
		return new Msg().fail("发表失败");
	}

	//t更新文章（更新文章内容分类等）
	@RequestMapping("/update")
	public Msg update(Passage passage){
		Integer i = passageService.update(passage);
		if (i==1){
			return new Msg().success();
		}
		return new Msg().fail("更新失败");
	}

	//t删除文章（逻辑删除:测试是否时逻辑删除）同步更新数据：文章分类下的文章数量/用户文章数量/
	@RequestMapping("/delete")
	public Msg deletePassage(Integer passageId){
		Integer result = passageService.delete(passageId);
		if (result==1)
			return new Msg().success();
		return new Msg().fail("删除失败");
	}

	//t查询用户收藏的文章
	@RequestMapping("/findCollectionByUserId")
	public List<Passage> findCollectionByUserId(Integer userId,Integer current, Integer size){
		IPage<Passage> page=new Page<>(current,size);
		return passageService.findCollectionByUserId(userId,page).getRecords();
	}

	//t根据发布人（用户ID）查询文章
	@RequestMapping("/findByUserId")
	public List<Passage> findByUserId(Integer userId,Integer current, Integer size){
		IPage<Passage> page=new Page<>(current,size);
		return passageService.findByUserId(userId, page).getRecords();
		//System.out.println("icome"+p);		//???进来了吗
	}

/*	//t查询所有 分页
	@RequestMapping("/findAll")
	public List<Passage> findAll(Integer current, Integer size){
		IPage<Passage> page=new Page<>(current,size);
		return passageService.findAll(page).getRecords();
	}*/

	@RequestMapping("/findByPage")
	public IPage<Passage> findByPage(Integer current, Integer size){
		return passageService.findByPage(current, size);
	}

	@RequestMapping("/findAll")
	public List<Passage> findAll(){
		return findAll();
	}

	//t根据文章ID查询文章
/*	@RequestMapping("/findByPassageId")
	public Msg findByPassageId(Integer passageId){
		Passage passgae = passageService.findByPassageId(passageId);
		System.out.println(passageId);
		if (passgae.equals(null))
			return Msg.fail();
		return Msg.success().add("passage",passgae);
	}*/

	//t根据文章ID查询文章
	@RequestMapping("/findByPassageId")
	public Passage findByPassageId(Integer passageId){
		return passageService.findByPassageId(passageId);
	}

	//t根据关键词模糊查询文章 分页
	@RequestMapping("/findByKeyword")
	@ResponseBody	//返回json格式的数据
	public List<Passage> findByKeyword(String name, Integer current, Integer size) {
		IPage<Passage> page=new Page<>(current,size);
		IPage<Passage> p=passageService.findByKeyword(name,page);
		return p.getRecords();
	}

}
