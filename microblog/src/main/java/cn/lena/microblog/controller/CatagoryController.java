package cn.lena.microblog.controller;


import cn.lena.microblog.domain.entity.Catagory;
import cn.lena.microblog.domain.entity.Passage;
import cn.lena.microblog.domain.utils.Msg;
import cn.lena.microblog.service.CatagoryService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
@RequestMapping("/catagory")
public class CatagoryController {

	@Autowired
	private CatagoryService catagoryService;

	//t创建分类
	@RequestMapping("/insert")
	public Msg insert(@RequestBody Catagory catagory){
		Integer result = catagoryService.insert(catagory);
		if (result==1){		//添加成功
			return new Msg().success("添加成功");
		}
		return new Msg().fail("添加失败");
	}

	//t查询当前用户所有分类 （根据文章数量排序）
	@RequestMapping("/findAll")
	@ResponseBody
	public List<Catagory> findAll(Integer userId) {
		List<Catagory> all = catagoryService.findAll(userId);
		System.out.println(all);
		return all;
		//return catagoryService.findAll(userId);
	}

	//t根据用户ID和分类ID 查询当前分类的文章 分页并排序（时间）
	@RequestMapping("/findPassages")
	public List<Passage> findPassages(Catagory catagory,Integer current,Integer size){
		IPage<Passage> page=new Page<>(current,size);
		return catagoryService.findPassages(catagory,page);
	}

	//t删除该分类（逻辑删除)
	@RequestMapping("/delete")
	public Msg delete(@RequestBody Catagory catagory){
		Integer result = catagoryService.delete(catagory);
		if (result==1){		//删除成功
			return new Msg().success("更新成功");
		}
		return new Msg().fail("更新失败");
	}

	//t修改分类信息（例如分类名） 注意：默认分类不能修改！！！
	@RequestMapping("/update")
	public Msg update(@RequestBody Catagory catagory){
		Integer result = catagoryService.update(catagory);
		if (result==1){		//更新成功
			return new Msg().success("更新成功");
		}
		return new Msg().fail("更新失败");
	}

}
