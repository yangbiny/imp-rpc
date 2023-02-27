package com.impassive.rpc.common;

import com.impassive.rpc.utils.StringTools;

/**
 * 注册中心的信息
 */
public record URLRegisterAddress(String address, Integer port, String path) {

  public String path() {
    if (StringTools.isEmpty(path)) {
      return "/imp/rpc";
    }
    return path;
  }

}
