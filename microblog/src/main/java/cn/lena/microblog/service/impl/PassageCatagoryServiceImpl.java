package cn.lena.microblog.service.impl;

import cn.lena.microblog.domain.entity.Catagory;
import cn.lena.microblog.domain.entity.Passage;
import cn.lena.microblog.domain.entity.PassageCatagory;
import cn.lena.microblog.mapper.CatagoryMapper;
import cn.lena.microblog.mapper.PassageCatagoryMapper;
import cn.lena.microblog.service.PassageCatagoryService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lena
 * @since 2020-07-26
 */
@Service
@Transactional
public class PassageCatagoryServiceImpl extends ServiceImpl<PassageCatagoryMapper, PassageCatagory> implements PassageCatagoryService {

	@Autowired
	private PassageCatagoryMapper passageCatagoryMapper;

	@Autowired
	private CatagoryMapper catagoryMapper;

	@Override//修改当前分类下文章数目
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
	}
}
