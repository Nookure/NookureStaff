package es.angelillo15.mast.api.redis;

import es.angelillo15.mast.api.redis.events.server.ServerConnectedEvent;
import es.angelillo15.mast.api.redis.events.server.ServerDisconnectedEvent;
import es.angelillo15.mast.api.redis.events.staff.StaffJoinEvent;
import es.angelillo15.mast.api.redis.events.staff.StaffLeaveEvent;

public enum Events {
    SERVER_CONNECTED("ServerConnectedEvent", ServerConnectedEvent.class),
    SERVER_DISCONNECTED("ServerDisconnectedEvent", ServerDisconnectedEvent.class),
    STAFF_JOIN("StaffJoinEvent",StaffJoinEvent .class),
    STAFF_LEAVE("StaffLeaveEvent", StaffLeaveEvent.class);

    private final String event;
    private final Class<? extends Event> eventClass;

    Events(String event, Class<? extends Event> eventClass) {
        this.event = event;
        this.eventClass = eventClass;
    }

    public String getEventName() {
        return event;
    }

    public Class<? extends Event> getEvent() {
        return eventClass;
    }
}
