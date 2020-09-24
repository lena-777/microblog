package cn.lena.microblog.domain.utils;

public enum CacheKey {

	KEYWORD_TOP_TEN,	//关键词数量前十（启动即存储）
	KEYWORD_TREE,		//存储所有关键词
	PASSAGE_TREE,		//所有文章（启动即存储）
	USER,		//当前登录的用户信息


}
