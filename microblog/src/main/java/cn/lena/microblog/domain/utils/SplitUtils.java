package cn.lena.microblog.domain.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//通过字符串返回分割后数组Set
public class SplitUtils {

	////字符串分割后返回list集合
	public static List<Integer> stringToList(String s){
		String[] split = s.split(",");
		List<Integer> list=new ArrayList<>();
		for (String s1 : split) {
			if (s1.equals("")||s1==null){
				break;
			}
			list.add(Integer.parseInt(s1));
		}
		return list;
	}


	//字符串分割后返回set集合
	public static Set<Integer> stringToSet(String s){
		String[] split = s.split(",");
		Set<Integer> set=new HashSet<>();
		for (String s1 : split) {
			if (s1.equals("")||s1==null){
				break;
			}
			set.add(Integer.parseInt(s1));
		}
		return set;
	}


}
