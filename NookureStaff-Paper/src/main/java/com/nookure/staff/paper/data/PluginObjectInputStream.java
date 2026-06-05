package com.nookure.staff.paper.data;

import org.bukkit.util.io.BukkitObjectInputStream;

import java.io.IOException;

public class PluginObjectInputStream extends BukkitObjectInputStream {
  protected PluginObjectInputStream() throws IOException, SecurityException {
  }

  protected PluginObjectInputStream(java.io.InputStream in) throws IOException {
    super(in);
  }

  @Override
  protected Object resolveObject(Object obj) throws IOException {
    return super.resolveObject(obj);
  }
}
