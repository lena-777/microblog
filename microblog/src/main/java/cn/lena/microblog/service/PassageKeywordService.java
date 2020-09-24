package cn.lena.microblog.service;

import cn.lena.microblog.domain.entity.Passage;
import cn.lena.microblog.domain.entity.PassageKeyword;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lena
 * @since 2020-07-26
 */
public interface PassageKeywordService extends IService<PassageKeyword> {

	//修改当前关键词下文章数目
	public Boolean updateKeyword(Passage passage, Integer flag);

}
