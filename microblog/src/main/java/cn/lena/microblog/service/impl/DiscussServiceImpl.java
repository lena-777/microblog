package cn.lena.microblog.service.impl;

import cn.lena.microblog.domain.entity.Discuss;
import cn.lena.microblog.mapper.DiscussMapper;
import cn.lena.microblog.service.DiscussService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lena
 * @since 2020-07-24
 */
@Service
@Transactional
public class DiscussServiceImpl extends ServiceImpl<DiscussMapper, Discuss> implements DiscussService {

	@Autowired
	private DiscussMapper discussMapper;

	@Override//t添加评论
	public Integer insert(Discuss discuss) {
		if (discuss.getUserId()==null||discuss.getPassageId()==null){
			return 0;
		}
		if (discuss.getTime()==null){
			discuss.setTime(new Date());
		}
		return discussMapper.insert(discuss);
	}

	@Override//t修改评论内容
	public Integer update(Discuss discuss) {
		return discussMapper.updateById(discuss);
	}

	@Override//t删除评论（同时删除子评论）
	public Integer delete(Discuss discuss) {
		//删除当前评论
		int result = discussMapper.deleteById(discuss);
		if (result==1){		//删除成功
			//查找子评论：父ID==>当前评论的ID
			List<Discuss> discusses
					= discussMapper.selectList(new QueryWrapper<Discuss>().eq("parentId", discuss.getDiscussId()));
			for (Discuss d : discusses) {
				d.deleteById();		//删除子评论
			}
		}
		return result;
	}

	@Override//t根据文章id查询评论（子评论存放在评论中）
	public List<Discuss> findByPassageId(Integer passageId) {
		QueryWrapper<Discuss> query=new QueryWrapper<>();
		query.eq("parentId",0).eq("passageId",passageId);	//不是子评论
		List<Discuss> discusses = discussMapper.selectList(query);
		//测试一下子评论是否成功查询进去----------------------------------------------------------!!!
		for (Discuss discuss : discusses) {
			//查找子评论
			List<Discuss> d = discussMapper.selectList(new QueryWrapper<Discuss>().eq("parentId", discuss.getDiscussId()));
			if (!d.isEmpty()){			//若存子评论
				discuss.setDiscuss(d);		//将子评论添加到父评论对象中
			}
		}
		return discusses;
	}
}
