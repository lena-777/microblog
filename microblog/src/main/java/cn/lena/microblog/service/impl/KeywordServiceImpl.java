package cn.lena.microblog.service.impl;

import cn.lena.microblog.domain.entity.Keyword;
import cn.lena.microblog.domain.entity.Passage;
import cn.lena.microblog.domain.entity.PassageKeyword;
import cn.lena.microblog.domain.utils.CacheKey;
import cn.lena.microblog.mapper.KeywordMapper;
import cn.lena.microblog.mapper.PassageKeywordMapper;
import cn.lena.microblog.mapper.PassageMapper;
import cn.lena.microblog.service.KeywordService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
public class KeywordServiceImpl extends ServiceImpl<KeywordMapper, Keyword> implements KeywordService {

	@Autowired
	private KeywordMapper keywordMapper;

	@Autowired
	private PassageKeywordMapper passageKeywordMapper;

	@Autowired
	private PassageMapper passageMapper;

	@Autowired
	private RedisTemplate redisTemplate;

	@Override//t模糊查询关键词
	public List<Keyword> findByName(String name, IPage<Keyword> page) {
		QueryWrapper<Keyword> query=new QueryWrapper<>();
		query.orderByDesc("passageNum").like("keywordName",name);
		return keywordMapper.selectPage(page,query).getRecords();
	}

	@Override//t添加关键词
	public Integer insert(Keyword keyword) {
		//判断是否存在该关键词
		Keyword key
				= keywordMapper.selectOne(new QueryWrapper<Keyword>().eq("keywordName", keyword.getKeywordName()));
		if (key==null){
			return keywordMapper.insert(keyword);
		}
		return 0;
	}

	@Override//根据关键词ID查询文章
	public IPage<Passage> findPassageByKeywordId(Integer keywordId, IPage<Passage> page) {
		//通过关键词ID查询当前关键词下的文章ID
		List<PassageKeyword> keywords
				= passageKeywordMapper.selectList(new QueryWrapper<PassageKeyword>().eq("keywordId", keywordId));
		//定义Set集合存取文章ids
		Set<Integer> ids=new HashSet<>();
		for (PassageKeyword key : keywords) {
			//获取文章id
			ids.add(key.getPassageId());
		}
		return passageMapper.selectPage(page,new QueryWrapper<Passage>().in("passageId",ids).eq("isPrivate",0).eq("isPublish",1));
	}

	@Override//t根据关键词查询文章
	public IPage<Passage> findPassageByKeyword(String keyword, IPage<Passage> page) {
		//查询出有关关键词
		Keyword keyword1 = keywordMapper.selectOne(new QueryWrapper<Keyword>().eq("keywordName",keyword));
		//获取关键词ID:keyword1.getKeywordId()
		//通过关键词ID查询文章
		List<PassageKeyword> keywords
				= passageKeywordMapper.selectList(new QueryWrapper<PassageKeyword>().eq("keywordId", keyword1.getKeywordId()));
		//定义Set集合存取文章ids
		Set<Integer> ids=new HashSet<>();
		for (PassageKeyword key : keywords) {
			//获取文章id
			ids.add(key.getPassageId());
		}
		return passageMapper.selectPage(page,new QueryWrapper<Passage>().in("passageId",ids).eq("isPrivate",0).eq("isPublish",1));
	}

	@Override//t查询所有（前十）关键词 并排序
	public List<Keyword> findTopTen() {
		//System.out.println("取数据");
		List<Keyword> list=(List<Keyword>)redisTemplate.boundValueOps(CacheKey.KEYWORD_TOP_TEN.toString()).get();
		if (list==null){
			findTopTenToRedis();
			return findTopTen();
		}
		return list;
	}

	//将查询排名前十的关键词添加进缓存中
	public void findTopTenToRedis() {
		QueryWrapper<Keyword> query=new QueryWrapper<>();
		query.orderByDesc("passageNum");
		//筛选第一页且每页十条信息==>筛选前十条
		IPage<Keyword> page=new Page<>(1,10);
		List<Keyword> records = keywordMapper.selectPage(page, query).getRecords();
		redisTemplate.boundValueOps(CacheKey.KEYWORD_TOP_TEN.toString()).set(records);
		//System.out.println("存数据");
	}

	@Override//t查询所有关键词 分页并排序
	public List<Keyword> findByPage(IPage<Keyword> page) {
		QueryWrapper<Keyword> query=new QueryWrapper<>();
		query.orderByDesc("passageNum");
		return keywordMapper.selectPage(page,query).getRecords();
	}

	@Override		//查询所有关键词存入缓存中（用户不一定会查询全部关键词，因此在触发的时候才存入缓存中）
	public List<Keyword> findAll(){
		List<Keyword> list=(List<Keyword>)redisTemplate.boundValueOps(CacheKey.KEYWORD_TREE.toString()).get();
		if (list==null){		//若缓存中没有数据，则在数据库中进行查找，并将查找到的结果存入缓存中
			QueryWrapper<Keyword> query=new QueryWrapper<>();
			query.orderByDesc("passageNum");
			list=keywordMapper.selectList(query);
			redisTemplate.boundValueOps(CacheKey.KEYWORD_TREE.toString()).set(list);
		}
		return list;
	}

	@Override	//模糊查询所有关键词
	public List<Keyword> findLikeName(String name) {
		QueryWrapper<Keyword> query=new QueryWrapper<>();
		query.orderByDesc("passageNum").like("keywordName",name);
		return keywordMapper.selectList(query);
	}
}
