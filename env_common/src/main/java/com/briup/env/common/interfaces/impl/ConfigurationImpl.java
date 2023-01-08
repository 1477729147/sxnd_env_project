package com.briup.env.common.interfaces.impl;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.junit.Test;

import com.briup.env.common.interfaces.Client;
import com.briup.env.common.interfaces.Configuration;
import com.briup.env.common.interfaces.DbStore;
import com.briup.env.common.interfaces.EnvironmentInit;
import com.briup.env.common.interfaces.Gather;
import com.briup.env.common.interfaces.Logger;
import com.briup.env.common.interfaces.Server;

public class ConfigurationImpl implements Configuration{
	
	// 准备一个map集合保存对象
	private Map<String, EnvironmentInit> map = new HashMap<String, EnvironmentInit>();
	// 准备一个集合保存属性名字和属性值
	private Properties properties;
	
	public ConfigurationImpl(){
		try {
			// 在构造器里解析xml
			// 创建解析器
			SAXReader reader = new SAXReader();
			// 读取xml文件获得文档对象
			Document document = reader.read(new File("src/main/resources/config.xml"));
			// 获得根节点
			Element rootElement = document.getRootElement(); // 
			// 获得第一级子节点
			List<Element> elements = rootElement.elements();
			// 遍历
			for(Element e : elements) {
				// 获得第一级子节点的class属性，也就是配置文件中每个类的全限定类名
				String className = e.attributeValue("class");
				// 获得了文件里的className之后，就可以使用反射创建对象了
				Class<?> clazz = Class.forName(className);
				// Class对象调用空参构造器创建对象
				Object obj = clazz.newInstance();
				// 判断obj属不属于根接口，如果属于，进行强转强转
				EnvironmentInit ei = null;
				if(obj instanceof EnvironmentInit) {
					ei = (EnvironmentInit) obj;
				}
				
				// 在放入之前注入二级节点值
				// 获得所有的二级节点
				List<Element> elements2 = e.elements();
				// 对properties进行初始化
				properties = new Properties();
				// 遍历二级节点了
				for(Element e2 : elements2) {
					properties.setProperty(e2.getName(), e2.getStringValue());
				}
				ei.init(properties);
				// 这里的ei就是遍历文件得到的每一个子模块对象		
				map.put(e.getName(), ei);
					
			}
			// 将自己的对象也给注入进去（注意他的位置）	
			// 放到循环外面
			Collection<EnvironmentInit> values = map.values();
			for(EnvironmentInit e : values) {
				e.config(this);
			}
			System.out.println(map);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	@Override
	public Gather getGather() {
		return (Gather)map.get("gather");
	}

	@Override
	public Client getClient() {
		return (Client)map.get("client");
	}

	@Override
	public Server getServer() {
		return (Server)map.get("server");
	}

	@Override
	public DbStore getDbStore() {
		return (DbStore)map.get("dbstore");
	}


	@Override
	public Logger getLogger() {
		return (Logger)map.get("logger");
	}
	
}
