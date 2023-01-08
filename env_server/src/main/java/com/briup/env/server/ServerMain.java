package com.briup.env.server;

import java.util.Collection;

import com.briup.env.common.entity.Environment;
import com.briup.env.common.interfaces.Configuration;
import com.briup.env.common.interfaces.DbStore;
import com.briup.env.common.interfaces.Server;
import com.briup.env.common.interfaces.impl.ConfigurationImpl;

public class ServerMain {
	public static void main(String[] args) {
		Configuration configuration = new ConfigurationImpl();
		Server server = configuration.getServer();
		// 接收
		Collection<Environment> coll = server.receive();
		// 入库对象
		DbStore dbStore = configuration.getDbStore();
		// 入库
		dbStore.dbstore(coll);
	}
}
