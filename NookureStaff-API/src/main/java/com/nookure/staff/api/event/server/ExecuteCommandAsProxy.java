package com.nookure.staff.api.event.server;

import com.nookure.staff.api.event.Event;

public record ExecuteCommandAsProxy(String command) implements Event {}
