package com.zhx.common.translator.test.pojo;

import java.util.List;

import com.zhx.common.translator.annotation.GValue;

/**
 * 订单
 * @author zhx
 * @date 2023年9月20日
 */
public class Order {
	
	
	private String name;

	@GValue
	private String productCata;
	
	private List<Product> products;

	public String getProductCata() {
		return productCata;
	}

	public void setProductCata(String productCata) {
		this.productCata = productCata;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
 
	
	
	
	
}
