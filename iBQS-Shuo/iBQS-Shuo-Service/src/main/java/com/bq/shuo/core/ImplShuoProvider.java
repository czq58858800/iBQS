package com.bq.shuo.core;

import com.alibaba.dubbo.config.annotation.Service;
import com.bq.shuo.core.base.BaseProviderImpl;
import com.bq.shuo.provider.IShuoProvider;

/**
 * Created by Harvey.Wei on 2017/4/11 0011.
 */
@Service(interfaceClass = IShuoProvider.class)
public class ImplShuoProvider extends BaseProviderImpl implements IShuoProvider {
}
