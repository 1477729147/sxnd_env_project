package com.briup.env.common.interfaces.impl;

import java.util.Properties;

import com.briup.env.common.interfaces.Configuration;
import com.briup.env.common.interfaces.Logger;

public class LoggerImpl implements Logger{
	private org.apache.log4j.Logger rootLogger;
	
	public LoggerImpl() {
		// 获得root logger
		rootLogger = org.apache.log4j.Logger.getRootLogger();
	}

	@Override
	public void debug(Object msg) {
		rootLogger.debug(msg);
	}

	@Override
	public void info(Object msg) {
		rootLogger.info(msg);
	}

	@Override
	public void warn(Object msg) {
		rootLogger.warn(msg);
	}

	@Override
	public void error(Object msg) {
		rootLogger.error(msg);
	}

	@Override
	public void fatal(Object msg) {
		rootLogger.fatal(msg);
	}

	@Override
	public void init(Properties properties) {
		// 日志模块不需要什么配置
	}

	@Override
	public void config(Configuration configuration) {
		// 日志模块也不需要引入其他模块
	}

}
