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
		//订单1
		Order o=new Order();
		o.setName("家电");
		o.setProductCata(Level1Enums.JIA_DIAN.getValue());
		o.setProducts(new ArrayList<>());
		//商品1
		Product p=new Product();
		p.setTypeKeyType("C:com.zhx.common.translator.test.enums.level.Level2Enums");
		p.setPrice("111");
		p.setType(JiaDianEnums.ICEBOX.getValue());
		//颜色编码
		p.setColorCode("01");
		p.setColorCodeType("01");   //使用color1
		p.setColorCodeDefi("M:{\"01\":\"color1\",\"02\":\"color2\"}");
		
		o.getProducts().add(p);
		//商品2
		p=new Product();
		p.setPrice("2222");
		p.setType(JiaDianEnums.TELEVISION.getValue());
		
			//颜色编码
		p.setColorCode("01");
		p.setColorCodeType("02");   //使用color2
		p.setColorCodeDefi("M:{\"01\":\"color1\",\"02\":\"color2\"}");
		
		o.getProducts().add(p);
		//翻译
		engine.translateData(o);
		System.out.println(JSON.toJSONString(o));
		
		
		//第二个订单
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
