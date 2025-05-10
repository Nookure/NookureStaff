package com.nookure.staff.api.event;

import org.jetbrains.annotations.Nullable;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;

public record EventVector(Method method, Object listener, NookSubscribe nookSubscribe, boolean weak) {
  /**
   * Create a new event vector
   *
   * @param method        The method to call
   * @param listener      The listener to call the method on
   * @param nookSubscribe The nook subscribe annotation
   * @param weak          Whether the listener is weak or not
   */
  public EventVector(Method method, Object listener, NookSubscribe nookSubscribe, boolean weak) {
    this.method = method;
    this.listener = weak ? new WeakReference<>(listener) : listener;
    this.nookSubscribe = nookSubscribe;
    this.weak = weak;
  }

  /**
   * Get the listener
   *
   * @return The listener
   */
  @Nullable
  public Object listener() {
    if (weak) {
      Reference<?> reference = (Reference<?>) listener;
      return reference.get();
    } else {
      return listener;
    }
  }
}
