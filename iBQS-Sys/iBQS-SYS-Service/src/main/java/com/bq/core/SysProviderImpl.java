package com.bq.core;

import com.bq.provider.ISysProvider;
import com.bq.core.base.BaseProviderImpl;

import com.alibaba.dubbo.config.annotation.Service;

@Service(interfaceClass = ISysProvider.class)
public class SysProviderImpl extends BaseProviderImpl implements ISysProvider {
	
}