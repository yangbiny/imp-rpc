package com.impassive.rpc.core.protocol;

import com.impassive.rpc.common.ImpUrl;
import com.impassive.rpc.core.api.Protocol;
import com.impassive.rpc.core.api.Registry;
import com.impassive.rpc.core.api.RegistryFactory;
import com.impassive.rpc.exception.ExceptionCode;
import com.impassive.rpc.exception.ServiceExportException;
import com.impassive.rpc.extension.ExtensionLoader;
import com.impassive.rpc.remote.api.Transport;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ImpProtocol implements Protocol {

  private static final Map<Class<?>, ImpUrl> exportedUrlMap = new ConcurrentHashMap<>();

  @Override
  public void export(ImpUrl impUrl) {
    // 1. 检查 服务是否已经暴露
    checkIsExported(impUrl);
    // 2. 打开 端口
    Transport.bind(impUrl);
    // 3. 写入注册中心
    Registry registry = ExtensionLoader
        .buildExtensionLoader(RegistryFactory.class)
        .buildDefaultExtension()
        .buildRegistry(impUrl);
    registry.register(impUrl);

    exportedUrlMap.put(null, impUrl);
  }

  @Override
  public <T> T refer(ImpUrl refer) {
    return null;
  }

  @Override
  public void unExport(ImpUrl impUrl) {
    // 3. 写入注册中心
    Registry registry = ExtensionLoader
        .buildExtensionLoader(RegistryFactory.class)
        .buildDefaultExtension()
        .buildRegistry(impUrl);
    registry.unRegister(impUrl);
  }

  private void checkIsExported(ImpUrl impUrl) {
    Class<?> classType = null;
    ImpUrl exportedImpUrl = exportedUrlMap.get(classType);
    if (exportedImpUrl != null) {
      throw new ServiceExportException(ExceptionCode.SERVICE_EXPORTER_EXCEPTION,
          String.format("不允许重复暴露服务 : %s", classType.getName()));
    }
  }
}
