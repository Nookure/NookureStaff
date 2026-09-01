package com.nookure.staff.api.event.staff;

import com.nookure.staff.api.event.Event;
import java.util.UUID;

public record StaffModeEnabledEvent(UUID staffUUID) implements Event {}
