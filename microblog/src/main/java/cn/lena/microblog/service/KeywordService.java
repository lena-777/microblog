package cn.lena.microblog.service;

import cn.lena.microblog.domain.entity.Keyword;
import cn.lena.microblog.domain.entity.Passage;
import com.baomidou.mybatisplus.core.metadata.IPage;
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
public interface KeywordService extends IService<Keyword> {

	//t模糊查询关键词
	List<Keyword> findByName(String name,IPage<Keyword> page);

	//t添加关键词
	Integer insert(Keyword keyword);

	//根据关键词ID查询文章
	IPage<Passage> findPassageByKeywordId(Integer keywordId, IPage<Passage> page);

	//t根据关键词查询文章
	IPage<Passage> findPassageByKeyword(String keyword, IPage<Passage> page);

	//t查询所有（前十）关键词 并排序
	List<Keyword> findTopTen();

	//t查询所有关键词 分页并排序
	List<Keyword> findByPage(IPage<Keyword> page);

	//查询所有
	List<Keyword> findAll();

	//模糊查询所有
	List<Keyword> findLikeName(String name);

	/*	//更新关键词数据（添加文章时，该关键词下的文章+1）在中间表passageKeyword中编写该方法
	Keyword updateNum(Integer keywordId);*/

	//将查询排名前十的关键词添加进缓存中
	public void findTopTenToRedis();

}
