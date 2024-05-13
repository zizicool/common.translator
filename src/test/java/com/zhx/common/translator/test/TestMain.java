package com.zhx.common.translator.test;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson2.JSON;
import com.zhx.common.translator.TranslatorEngine;
import com.zhx.common.translator.test.enums.AgeGroupEnums;
import com.zhx.common.translator.test.enums.YorNEnums;
import com.zhx.common.translator.test.pojo.User;

public class TestMain extends AbstractTest {

	public static void main(String[] args) {
		TranslatorEngine  engine=AbstractTest.getTranslatorEngine();
		List<User> ls=new ArrayList<>();
		User user=new User("001","张三男", YorNEnums.Y.getValue(), null, AgeGroupEnums.KIDS.getValue(), null);
		ls.add(user);
		user=new User("001","王五女", YorNEnums.N.getValue(), null, AgeGroupEnums.MIDDLE_AGER.getValue(), null);
		ls.add(user);
		user=new User("001","李四不变", "111", "不变", 22, "不变");
		ls.add(user);
		engine.translateData(ls);
		
		System.out.println(JSON.toJSONString(ls));
		user=new User("777","777默认值", "222", null, 333, null);
		user.setColors("1,3,5,6");
		user.setColors2(new ArrayList<String>());
		user.getColors2().add("2");
		user.getColors2().add("4");
		
		User son=new User("777_1","777 son", YorNEnums.Y.getValue(), null, AgeGroupEnums.KIDS.getValue(), null);
		user.setSon(son);
		engine.translateData(user);
		System.out.println(JSON.toJSONString(user));
		
				
		

	}

}
