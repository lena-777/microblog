package cn.lena.microblog.service.impl;

import cn.lena.microblog.domain.entity.Passage;
import cn.lena.microblog.domain.utils.CacheKey;
import cn.lena.microblog.domain.utils.SplitUtils;
import cn.lena.microblog.mapper.PassageMapper;
import cn.lena.microblog.mapper.UsersMapper;
import cn.lena.microblog.service.PassageCatagoryService;
import cn.lena.microblog.service.PassageKeywordService;
import cn.lena.microblog.service.PassageService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
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
public class PassageServiceImpl extends ServiceImpl<PassageMapper, Passage> implements PassageService {

	@Autowired
	private PassageMapper passageMapper;

	@Autowired
	private UsersMapper usersMapper;

	@Autowired
	private PassageCatagoryService passageCatagoryService;

	@Autowired
	private PassageKeywordService passageKeywordService;

	@Autowired
	private RedisTemplate redisTemplate;

	@Override//文章点赞数目+1/-1
	public Boolean updateLiked(Integer passageId, Integer flag) {
		Passage passage = passageMapper.selectById(passageId);
		Passage p = passage.setLiked(passage.getLiked() + flag);
		boolean b = p.updateById();
		findAllToRedis();		//更新缓存
		return b;
	}

	@Override//文章浏览量+1
	public Boolean addVisited(Integer passageId) {
		Passage passage = passageMapper.selectById(passageId);
		passage.setVisited(passage.getVisited()+1);
		boolean b = passage.updateById();
		//findAllToRedis();		//更新缓存(浏览量就不需要更新缓存吧？)
		return b;
	}

	@Override//发表文章/存入草稿箱中：取决于isPublish的值
	public Integer insert(Passage passage) {
		//若原先已经存在草稿箱了 则有创建日期 无需要进行填充
		if (passage.getCreateDate()==null){
			passage.setCreateDate(new Date());
		}
		passage.setUpdateTime(new Date());
		//设为公开的文章（可以在页面对按钮进行绑定 自己填充该值）
		//passage.setIsPublish(1);
		if (passage.getIsPublish()==1){			//发表的文章
			//修改相关的数据。（分类，关键词，中间表）
			passageKeywordService.updateKeyword(passage,+1);
			passageCatagoryService.updateCatagory(passage,+1);
		}
		findAllToRedis();		//更新缓存
		return passageMapper.insert(passage);
	}

	@Override//更新文章（更新文章内容分类等）
	public Integer update(Passage passage) {
		int i = passageMapper.updateById(passage);
		findAllToRedis();		//更新缓存
		return i;
	}

	@Override//删除文章（逻辑删除:测试是否时逻辑删除）同步更新数据：文章分类下的文章数量/用户文章数量/
	public Integer delete(Integer passageId) {
		//要在删除前进行查询:删除后deleted=1，而查询时mybatisplus会自动携带deleted=0的默认条件，因此会造成空指针异常
		Passage passage = passageMapper.selectById(passageId);
		if (passage.equals(null))
			return 0;
		//逻辑删除
		int result = passageMapper.deleteById(passageId);
		if (result==1){	//成功删除：影响的行数为1（返回值）
/*			//该分类下文章数目-1
			List<Integer> catagoryList = SplitUtils.stringToList(passage.getCatagory());
			for (Integer catagory : catagoryList) {
				updatePassageNum(passage.getUserId(),catagory,-1);
			}
			//该关键词下文章数目-1
			List<Integer> keywordList = SplitUtils.stringToList(passage.getKeywords());
			for (Integer keyword : keywordList) {
				updatePassageNum(keyword,-1);
			}*/
			//修改分类/关键词的数据/中间表
			passageCatagoryService.updateCatagory(passage,-1);
			passageKeywordService.updateKeyword(passage,-1);
			findAllToRedis();		//更新缓存
		}
		return result;
	}

	@Override//查询所有文章存入缓存中
	public void findAllToRedis() {
		//不带条件的查询所有
		//List<Passage> passages = passageMapper.selectList(null);
		//redisTemplate.boundValueOps(CacheKey.PASSAGE_TREE.toString()).set(passages);

		//清除缓存==>避免原先有缓存数据，在进行查询（原因：使用list的右压入，即使原先有数据，也会添加在后面，而不会进行覆盖。因此需要先清除）
		redisTemplate.delete(CacheKey.PASSAGE_TREE.toString());
		//根据访问量降序显示
		QueryWrapper<Passage> query=new QueryWrapper();
		query.orderByDesc("visited").eq("isPrivate",0).eq("isPublish",1);
		List<Passage> p = passageMapper.selectList(query);
		redisTemplate.boundValueOps(CacheKey.PASSAGE_TREE.toString()).set(p);
	}

	//从缓存中获取当前页数据
	public IPage<Passage> findPageByRedis(Integer current, Integer size) {
		Integer start=(current-1)*size;		//从0开始
		Integer end=current*size;		//list的subList表示从(start,end)这个范围，不包含end。因此不用-1
		//List<Passage> passages= (List<Passage>)redisTemplate.boundValueOps(CacheKey.PASSAGE_TREE.toString()).get();
		//------------------------------------------------------------------------------------------------------------
		List<Passage> passages =  (List<Passage>) redisTemplate.boundValueOps(CacheKey.PASSAGE_TREE.toString()).get();
		//若缓存中没有数据
		if (passages==null){
			findAllToRedis();			//将文章存入缓存中
			return findPageByRedis(current,size);		//重新从缓存中提取数据
		}

		Page<Passage> page=new Page<>(current,size);
		page.setRecords(passages.subList(start,end));
		page.setTotal(end-start);
		//page.setSize(page.getTotal()/size);		//总数据/每页数目（是否整除？）
		//System.out.println(page.getRecords());
		return page;
	}

	@Override//查询当前页数据（先从缓存中查找，若缓存中没有数据，则到数据库中查找）
	public IPage<Passage> findByPage(Integer current, Integer size) {
		IPage<Passage> list = findPageByRedis(current, size);
		if (list==null){
			findAllToRedis();		//重新存数据进入缓存
			return findByPage(current,size);
/*			IPage<Passage> page=new Page<>(current,size);
			return findAll(page).getRecords();*/
		}
		return list;
	}

	@Override//t查询用户收藏的文章
	public IPage<Passage> findCollectionByUserId(Integer userId, IPage<Passage> page) {
		//获取用户收藏的文章id
		String s = usersMapper.selectById(userId).getCollect();
		Set<Integer> ids = SplitUtils.stringToSet(s);
		//根据id查询文章
		QueryWrapper<Passage> query=new QueryWrapper<>();
		query.in("passageId",ids).eq("isPrivate",0).eq("isPublish",1);
		return passageMapper.selectPage(page,query);
	}

	@Override	//查询所有文章
	public List<Passage> findAll() {
		List<Passage> passages =  (List<Passage>) redisTemplate.boundValueOps(CacheKey.PASSAGE_TREE.toString()).get();
		//若缓存中没有数据
		if (passages==null){
			findAllToRedis();			//将文章存入缓存中
			return findAll();		//重新从缓存中提取数据
		}
		return passages;
	}

	@Override//t查询所有文章 分页(使用selectPage) 排序（按浏览量排序）
	public IPage<Passage> findAllPage(IPage<Passage> page) {
		QueryWrapper<Passage> query=new QueryWrapper();
		//根据访问量降序显示
		query.orderByDesc("visited").eq("isPrivate",0).eq("isPublish",1);
		return passageMapper.selectPage(page, query);
	}

	@Override//t根据发布人（用户ID）查询文章
	public IPage<Passage> findByUserId(Integer userId, IPage<Passage> page) {
		//List<Passage> list = redisTemplate.boundListOps(CacheKey.PASSAGE_TREE.toString()).range(0, -1);
		QueryWrapper<Passage> query=new QueryWrapper();
		//查询userId发送的所有文章
		query.eq("userId",userId).eq("isPrivate",0).eq("isPublish",1);
		return passageMapper.selectPage(page,query);
	}

	@Override//t根据文章名模糊查询文章 分页
	public IPage<Passage> findByKeyword(String name, IPage<Passage> page) {
		//查询条件
		QueryWrapper<Passage> query=new QueryWrapper<>();
		query.like("title",name).eq("isPrivate",0).eq("isPublish",1);
		//查询当前页数据
		return passageMapper.selectPage(page, query);
	}

	@Override//t根据文章ID查询文章
	public Passage findByPassageId(Integer passageId) {
		if (passageId==0)
			return null;
		return passageMapper.selectById(passageId);
	}

/*	//从缓存中获取当前页数据
	public List<Passage> findPageByRedis(Integer current, Integer size) {
		Integer start=(current-1)*size;		//从0开始
		Integer end=current*size;		//list的subList表示从(start,end)这个范围，不包含end。因此不用-1
		//List<Passage> passages= (List<Passage>)redisTemplate.boundValueOps(CacheKey.PASSAGE_TREE.toString()).get();
		//------------------------------------------------------------------------------------------------------------
		List<Passage> passages =  (List<Passage>) redisTemplate.boundValueOps(CacheKey.PASSAGE_TREE.toString()).get();
		//若缓存中没有数据
		if (passages==null){
			findAllToRedis();			//将文章存入缓存中
			return findPageByRedis(current,size);		//重新从缓存中提取数据
		}
		return passages.subList(start,end);
	}*/

	/*	//修改当前分类下文章数目(转移到passageCatagoryService中了)
	public Boolean updateCatagory(Passage passage,Integer flag){
		//添加中间表
		if (flag==1){
			//查询该文章属于哪个分类
			Integer[] catagory = passage.getCatagory();
			PassageCatagory p=new PassageCatagory();
			p.setPassageId(passage.getPassageId());
			p.setUserId(passage.getUserId());
			for (Integer c : catagory) {
				p.setCatagoryId(c);
				//添加中间表信息
				passageCatagoryMapper.insert(p);
				//该分类下文章数目+1
				Catagory catagory1 = catagoryMapper.selectById(c);
				catagory1.setPassageNum(catagory1.getPassageNum()+1);
				catagoryMapper.updateById(catagory1);
			}
		}
		//删除中间表
		else{
			//查询该文章属于哪个分类
			List<PassageCatagory> catagories
					= passageCatagoryMapper.selectList(new QueryWrapper<PassageCatagory>().eq("passageId", passage.getPassageId()));
			for (PassageCatagory catagory : catagories) {
				//删除中间表信息
				catagory.deleteById();		//flag=-1时，为删除文章的操作,此时需要删除中间表
				Catagory one = catagoryMapper.selectById(catagory.getCatagoryId());
				one.setPassageNum(one.getPassageNum()-1);
				catagoryMapper.updateById(one);
			}
		}
		return true;
	}*/

/*	//修改当前关键词下文章数目(转移到passageKeywordService中了)
	public Boolean updateKeyword(Passage passage,Integer flag){
		//添加文章
		if (flag==1){
			Integer[] keywords = passage.getKeywords();
			PassageKeyword keyword=new PassageKeyword();
			keyword.setPassageId(passage.getPassageId());
			for (Integer key : keywords) {
				//添加中间表信息
				keyword.setKeywordId(key);
				passageKeywordMapper.insert(keyword);
				//添加关键词下文章数目
				Keyword keyword1 = keywordMapper.selectById(key);
				keyword1.setPassageNum(keyword1.getPassageNum()+1);
				keywordMapper.updateById(keyword1);
			}
		}
		//删除文章
		else{
			//查询该文章属于哪个关键词
			List<PassageKeyword> keywords
					= passageKeywordMapper.selectList(new QueryWrapper<PassageKeyword>().eq("passageId", passage.getPassageId()));
			//删除中间表
			for (PassageKeyword keyword : keywords) {
				keyword.deleteById();
				//关键词下面文章数目-1
				//QueryWrapper<PassageKeyword> query1=new QueryWrapper<>();
				//query1.eq("keywordId",keyword.getKeywordId());
				Keyword key = keywordMapper.selectById(keyword.getKeywordId());
				key.setPassageNum(key.getPassageNum()-1);
				keywordMapper.updateById(key);
			}
		}
		return true;
	}*/

/*
	@Override//t将文章当前关键词下文章数目-1/+1
	public Boolean updatePassageNum(Integer keywordId, Integer flag) {
		Keyword keyword = keywordMapper.selectById(keywordId);
		if (keyword.equals(null))
			return false;
		if (keyword.getPassageNum()<=0)
			return false;
		keyword.setPassageNum(keyword.getPassageNum() + flag);
		return keyword.updateById();
	}
*/

/*
	@Override//t将文章分类下文章数目-1/+1
	public Boolean updatePassageNum(Integer userId, Integer catagoryId, Integer flag) {
		QueryWrapper<Catagory> query=new QueryWrapper<>();
		query.eq("userId",userId).eq("catagoryId",catagoryId);
		Catagory catagory = catagoryMapper.selectOne(query);
		if (catagory.equals(null))
			return false;
		if (catagory.getPassageNum()<=0)
			return false;
		catagory.setPassageNum(catagory.getPassageNum()+flag);
		//query中userId和catagoryId没有改变
		return catagory.update(query);
	}
*/
}
