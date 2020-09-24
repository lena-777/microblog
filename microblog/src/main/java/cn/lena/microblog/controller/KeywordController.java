package cn.lena.microblog.controller;


import cn.lena.microblog.domain.entity.Keyword;
import cn.lena.microblog.domain.entity.Passage;
import cn.lena.microblog.domain.utils.Msg;
import cn.lena.microblog.service.KeywordService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/keyword")
public class KeywordController {

	@Autowired
	private KeywordService keywordService;

	//查询所有关键词
	@RequestMapping("/findAll")
	public List<Keyword> findAll(){
		return keywordService.findAll();
	}

	//模糊查询所有关键词
	@RequestMapping("/findLikeName")
	public List<Keyword> findLikeName(String name){
		return keywordService.findLikeName(name);
	}

	//t模糊查询关键词。分页
	@RequestMapping("/findByName")
	public List<Keyword> findByName(String name, Integer current,Integer size){
		IPage<Keyword> page=new Page<>(current,size);
		return keywordService.findByName(name,page);
	}

	//t添加关键词
	@RequestMapping("/insert")
	public Msg insert(Keyword keyword){
		Integer result = keywordService.insert(keyword);
		if (result==1)
			return new Msg().success("添加成功");
		return new Msg().fail("添加失败");
	}

	//t根据关键词ID查询文章
	@RequestMapping("/findPassageByKeywordId")
	public IPage<Passage> findPassageByKeywordName(Integer keywordId, Integer current, Integer size){
		IPage<Passage> page=new Page<>(current,size);
		return keywordService.findPassageByKeywordId(keywordId,page);
	}

	//t根据关键词查询文章
	@RequestMapping("/findPassageByKeywordName")
	public IPage<Passage> findPassageByKeyword(String keyword,Integer current,Integer size){
		IPage<Passage> page=new Page<>(current,size);
		return keywordService.findPassageByKeyword(keyword,page);
	}

	//t查询所有（前十）关键词 并排序
	@RequestMapping("/findTop")
	public List<Keyword> findTop()
	{
		//List<Keyword> list=keywordService.findTopTen();
		//System.out.println(list);
		return keywordService.findTopTen();
	}

	//t查询所有关键词 分页并排序
	@RequestMapping("/findByPage")
	public List<Keyword> findAll(Integer current,Integer size){
		IPage<Keyword> page=new Page<>(current,size);
		return keywordService.findByPage(page);
	}

}
