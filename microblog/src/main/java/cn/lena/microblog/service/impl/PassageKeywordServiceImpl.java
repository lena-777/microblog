package cn.lena.microblog.service.impl;

import cn.lena.microblog.domain.entity.Keyword;
import cn.lena.microblog.domain.entity.Passage;
import cn.lena.microblog.domain.entity.PassageKeyword;
import cn.lena.microblog.mapper.KeywordMapper;
import cn.lena.microblog.mapper.PassageKeywordMapper;
import cn.lena.microblog.service.PassageKeywordService;
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
public class PassageKeywordServiceImpl extends ServiceImpl<PassageKeywordMapper, PassageKeyword> implements PassageKeywordService {

	@Autowired
	private KeywordMapper keywordMapper;

	@Autowired
	private PassageKeywordMapper passageKeywordMapper;

	@Override//修改当前关键词下文章数目
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
	}
}
