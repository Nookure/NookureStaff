package com.nookure.staff.paper.data;

import java.io.IOException;
import org.bukkit.util.io.BukkitObjectInputStream;

public class PluginObjectInputStream extends BukkitObjectInputStream {
    protected PluginObjectInputStream() throws IOException, SecurityException {}

    protected PluginObjectInputStream(java.io.InputStream in) throws IOException {
        super(in);
    }

    @Override
    protected Object resolveObject(Object obj) throws IOException {
        return super.resolveObject(obj);
    }
}
