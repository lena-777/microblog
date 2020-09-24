package cn.lena.microblog.service.impl;

import cn.lena.microblog.domain.entity.Catagory;
import cn.lena.microblog.domain.entity.Passage;
import cn.lena.microblog.domain.entity.PassageCatagory;
import cn.lena.microblog.domain.entity.Users;
import cn.lena.microblog.mapper.CatagoryMapper;
import cn.lena.microblog.mapper.PassageCatagoryMapper;
import cn.lena.microblog.mapper.PassageMapper;
import cn.lena.microblog.mapper.UsersMapper;
import cn.lena.microblog.service.CatagoryService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
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
public class CatagoryServiceImpl extends ServiceImpl<CatagoryMapper, Catagory> implements CatagoryService {

	@Autowired
	private CatagoryMapper catagoryMapper;

	@Autowired
	private PassageMapper passageMapper;

	@Autowired
	private PassageCatagoryMapper passageCatagoryMapper;

	@Autowired
	private UsersMapper usersMapper;


	@Override//t创建默认分类
	public Integer insert(Integer userId) {
		//该用户不存在默认分类   ==>用户创建的时候就创建这个分类，不需要考虑这种情况
/*		if (catagoryMapper.selectOne(new QueryWrapper<Catagory>().eq("userId", userId).eq("catagoryId", 1))!=null){
			return 0;
		}*/
		Catagory c=new Catagory();
		c.setCatagoryId(1).setCatagoryName("默认分类").setUserId(userId);
		return catagoryMapper.insert(c);
	}

	@Override//t创建分类
	public Integer insert(Catagory catagory) {
		if (catagory.getCatagoryId()==null){
			Integer userId=catagory.getUserId();
			List<Catagory> ids = catagoryMapper.selectList(new QueryWrapper<Catagory>().eq("userId", userId));
			Integer i=0;		//暂存当前用户的最大分类id
			for (Catagory id : ids) {
				if (i<id.getCatagoryId())
					i=id.getCatagoryId();
			}
			catagory.setCatagoryId(i+1);		//设置下一个分类ID
		}
		QueryWrapper<Catagory> query=new QueryWrapper<>();
		query.eq("userId", catagory.getUserId()).eq("catagoryId", catagory.getCatagoryId());
		Catagory c = catagoryMapper.selectOne(query);
		if (c==null){		//判断是否有重复
			//用户下分类数目+1
			Users users = usersMapper.selectById(catagory.getUserId());
			users.setCatagory(users.getCatagory() + 1);
			users.updateById();
			//添加分类
			return catagoryMapper.insert(catagory);
		}

		return 0;
	}

	@Override//t查询当前用户所有分类 （根据文章数量排序）
	public List<Catagory> findAll(Integer userId) {
		QueryWrapper<Catagory> query=new QueryWrapper<>();
		query.eq("userId",userId).orderByDesc("passageNum");
		return catagoryMapper.selectList(query);
	}

	@Override//t根据用户ID和分类ID 查询当前分类的文章 分页并排序（时间）
	public List<Passage> findPassages(Catagory catagory, IPage<Passage> page) {
		//查询当前分类下的文章
		QueryWrapper<PassageCatagory> query=new QueryWrapper<>();
		query.eq("userId",catagory.getUserId()).eq("catagoryId",catagory.getCatagoryId());
		List<PassageCatagory> catagories = passageCatagoryMapper.selectList(query);
		Set<Integer> ids=new HashSet<>();		//存取当前分类下的文章ID
		for (PassageCatagory pc : catagories) {
			ids.add(pc.getPassageId());
		}
		QueryWrapper<Passage> query1=new QueryWrapper<>();
		query1.in("passageId",ids);
		return passageMapper.selectPage(page,query1).getRecords();
	}

	//t删除该分类（逻辑删除）	原先该分类的文章默认删除该分类==>即删除中间表中，该分类的文章，或者将分类ID换成默认分类(1)下的，记得修改PassageNum
	@Override
	public Integer delete(Catagory catagory) {
		if (catagory.getCatagoryId()==1){		//当前分类为默认分类,不能删除
			return 0;
		}
		QueryWrapper<Catagory> query=new QueryWrapper<>();
		query.eq("userId",catagory.getUserId()).eq("catagoryId",catagory.getCatagoryId());
		int result = catagoryMapper.delete(query);
		if (result==1){
			//成功删除之后，将当前分类ID下的文章，转移到默认分类1下
			UpdateWrapper<PassageCatagory>	q=new UpdateWrapper<>();
			q.eq("userId",catagory.getUserId()).eq("catagoryId",catagory.getCatagoryId()).set("catagoryId",1);
			passageCatagoryMapper.update(null,q);
/*			List<Catagory> catagories = catagoryMapper.selectList(query);	//查询条件和上面一样
			for (Catagory c : catagories) {
				c.setCatagoryId(1);	//修改分类为默认分类
				catagoryMapper.update(c,query);
			}*/
			//当前用户下的分类数目-1
			Users users = usersMapper.selectById(catagory.getUserId());
			users.setCatagory(users.getCatagory() - 1);
			users.updateById();
		}
		return result;
	}

	@Override//t修改分类信息（例如分类名） 注意：默认分类不能修改！！！
	public Integer update(Catagory catagory) {
		if (catagory.getCatagoryId()==1){		//当前分类为默认分类
			return 0;
		}
		QueryWrapper<Catagory> query=new QueryWrapper<>();
		query.eq("userId",catagory.getUserId()).eq("catagoryId",catagory.getCatagoryId());
		//不可以使用catagoryId作为修改条件:catagoryId不是主键==>一个分类是由用户id和分类id共同决定的
		return catagoryMapper.update(catagory,query);
	}
}
