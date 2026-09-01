package com.nookure.staff.api.event.staff;

import com.nookure.staff.api.event.Event;
import java.util.UUID;

public record StaffModeDisabledEvent(UUID staffUUID) implements Event {}
