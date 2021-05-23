package com.dolap.product;

import com.dolap.product.model.Product;
import com.dolap.product.request.CreateProductRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

public class TestUtils {

	public static final String CREATE_PRODUCT_URL = "/product/create";
	public static final String UPDATE_PRODUCT_URL = "/product/update";
	public static final String DELETE_PRODUCT_URL = "/product/delete";
	public static final String REQUEST_PRODUCT_URL = "/product";

	public static final String PRODUCT_CATEGORY_PARAM_NAME = "productCategory";
	public static final String PAGE_PARAM_NAME = "page";
	public static final String COUNT_PARAM_NAME = "count";

	public static MockHttpServletRequestBuilder getProductOfCategoryRequestBuilder(Object category, int page,
																				   int count) {
		return get(REQUEST_PRODUCT_URL).param(PRODUCT_CATEGORY_PARAM_NAME, category.toString())
				.param(PAGE_PARAM_NAME, String.valueOf(page)).param(COUNT_PARAM_NAME, String.valueOf(count));
	}

	public static String objectToJson(Object product) {
		Gson gson = new Gson();
		return gson.toJson(product);
	}

	public static <T> T objectFromJson(String json, Class<T> classOfT) {
		Gson gson = new Gson();
		return gson.fromJson(json, classOfT);
	}

	public static List<Product> productListFromJson(String contentAsString) {
		Type listOfProductObject = new TypeToken<ArrayList<Product>>() {
		}.getType();
		Gson gson = new Gson();
		return gson.fromJson(contentAsString, listOfProductObject);
	}

	public static CreateProductRequest createProductRequestFromProduct(Product product) {
		return CreateProductRequest.builder().name(product.getName()).category(product.getCategory()).price(product.getPrice())
				.imageLink(product.getImageLink()).build();
	}

}
