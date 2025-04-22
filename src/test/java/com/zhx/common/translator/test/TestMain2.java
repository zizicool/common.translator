package com.zhx.common.translator.test;

import java.util.ArrayList;

import com.alibaba.fastjson2.JSON;
import com.zhx.common.translator.TranslatorEngine;
import com.zhx.common.translator.test.enums.level.ClothingEnums;
import com.zhx.common.translator.test.enums.level.JiaDianEnums;
import com.zhx.common.translator.test.enums.level.Level1Enums;
import com.zhx.common.translator.test.pojo.Order;
import com.zhx.common.translator.test.pojo.Product;

public class TestMain2 extends AbstractTest {

	public static void main(String[] args) {
		TranslatorEngine  engine=AbstractTest.getTranslatorEngine();
		Order o=new Order();
		o.setName("家电");
		o.setProductCata(Level1Enums.JIA_DIAN.getValue());
		o.setProducts(new ArrayList<>());
		Product p=new Product();
		p.setTypeKeyType("C:com.zhx.common.translator.test.enums.level.Level2Enums");
		p.setPrice("111");
		p.setType(JiaDianEnums.ICEBOX.getValue());
		o.getProducts().add(p);
		p=new Product();
		p.setPrice("2222");
		p.setType(JiaDianEnums.TELEVISION.getValue());
		o.getProducts().add(p);
		engine.translateData(o);
		System.out.println(JSON.toJSONString(o));
		
		Order o2=new Order();
		o2.setName("衣服");
		o2.setProductCata(Level1Enums.CLOTHING.getValue());
		o2.setProducts(new ArrayList<>());
		p=new Product();
		p.setPrice("333");
		p.setType(ClothingEnums.OVERCOAT.getValue());
		o2.getProducts().add(p);
		p=new Product();
		p.setPrice("444");
		p.setType(ClothingEnums.TROUSERS.getValue());
		o2.getProducts().add(p);
		engine.translateData(o2);
		System.out.println(JSON.toJSONString(o2));
		
				
		

	}

}
