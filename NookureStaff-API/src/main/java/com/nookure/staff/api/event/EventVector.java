package com.nookure.staff.api.event;

import java.lang.ref.Reference;
import java.lang.reflect.Method;

public record EventVector(Method method, Reference<Object> listener, NookSubscribe nookSubscribe) {

}
