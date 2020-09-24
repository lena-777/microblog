package cn.lena.microblog.service;

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
public interface PassageService extends IService<Passage> {

	//文章点赞数目+1/-1
	Boolean updateLiked(Integer passageId, Integer flag);

	//文章浏览量+1
	Boolean addVisited(Integer passageId);

	//t发表文章/存入草稿箱中：取决于isPublish的值
	Integer insert(Passage passage);

	//t更新文章（更新文章内容分类等）
	Integer update(Passage passage);

	//t查询用户收藏的文章
	IPage<Passage> findCollectionByUserId(Integer userId,IPage<Passage> page);

	//t查询所有文章 分页(使用selectPage) 排序（按浏览量排序）
	IPage<Passage> findAllPage(IPage<Passage> page);

	//查询所有文章
	List<Passage> findAll();

	//t根据文章ID查询文章
	Passage findByPassageId(Integer passageId);

	//t根据发布人（用户ID）查询文章
	IPage<Passage> findByUserId(Integer userId,IPage<Passage> page);

	//t根据文章名模糊查询文章 分页
	IPage<Passage> findByKeyword(String name,IPage<Passage> page);

	//t将文章当前关键词下文章数目-1/+1
	//Boolean updatePassageNum(Integer keywordId,Integer flag);

	//t将文章分类下文章数目-1/+1
	//Boolean updatePassageNum(Integer userId,Integer catagoryId,Integer flag);

	//t删除文章（逻辑删除:测试是否时逻辑删除）同步更新数据：文章分类下的文章数量/用户文章数量/
	Integer delete(Integer passageId);

	//查询所有文章存到缓存中
	void findAllToRedis();

	//查询当前页数据（先从缓存中查找，若缓存中没有数据，则到数据库中查找）
	IPage<Passage> findByPage(Integer current,Integer size);
	//List<Passage> findByPage(Integer current,Integer size);

}
