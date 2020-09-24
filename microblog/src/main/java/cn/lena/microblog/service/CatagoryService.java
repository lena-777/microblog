package cn.lena.microblog.service;

import cn.lena.microblog.domain.entity.Catagory;
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
public interface CatagoryService extends IService<Catagory> {

	//t创建默认分类
	Integer insert(Integer userId);

	//t创建分类
	Integer insert(Catagory catagory);

	//t查询当前用户所有分类 （根据文章数量排序）----是否需要分页？
	List<Catagory> findAll(Integer userId);

	//t根据用户ID和分类ID 查询当前分类的文章 分页并排序（时间）
	List<Passage> findPassages(Catagory catagory,IPage<Passage> page);

	//t删除该分类（逻辑删除）	原先该分类的文章默认删除该分类==>即删除中间表中，该分类的文章，或者将分类ID换成默认分类(1)下的，记得修改PassageNum
	Integer delete(Catagory catagory);

	//t修改分类信息（例如分类名） 注意：默认分类不能修改！！！
	Integer update(Catagory catagory);

}
