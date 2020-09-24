package cn.lena.microblog.service;

import cn.lena.microblog.domain.entity.Passage;
import cn.lena.microblog.domain.entity.PassageCatagory;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lena
 * @since 2020-07-26
 */
public interface PassageCatagoryService extends IService<PassageCatagory> {

	//修改当前分类下文章数目
	public Boolean updateCatagory(Passage passage, Integer flag);

}
