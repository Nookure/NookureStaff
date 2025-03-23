package com.nookure.staff.api.event;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.nookure.staff.api.Logger;
import com.nookure.staff.api.NookureStaff;
import com.nookure.staff.api.exception.EventHandlerException;
import org.jetbrains.annotations.NotNull;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
public final class EventManager {
  private final Map<Class<? extends Event>, List<EventVector>> listeners = new ConcurrentHashMap<>();
  @Inject
  private NookureStaff instance;
  @Inject
  private Logger logger;

  /**
   * Register a listener class
   * The class must have methods annotated with {@link NookSubscribe}
   * in order to work, otherwise it will be ignored
   *
   * @param clazz Listener class to register
   */
  private void registerListener(Class<?> clazz) {
    registerListener(instance.getInjector().getInstance(clazz));
  }

  /**
   * Unregister a listener class
   *
   * @param listener Listener class to unregister
   */
  public void unregisterListener(Object listener) {
    for (Map.Entry<Class<? extends Event>, List<EventVector>> entry : listeners.entrySet()) {
      final var eventVectors = entry.getValue();
      final var eventVectorsIterator = eventVectors.iterator();

      while (eventVectorsIterator.hasNext()) {
        final var eventVector = eventVectorsIterator.next();
        final var listenerRef = eventVector.listener();
        final var actualListener = listenerRef.get();

        if (actualListener == null || actualListener == listener) {
          eventVectorsIterator.remove();
        }
      }
    }
  }

  /**
   * Unregister all listeners
   */
  public void unregisterAllListeners() {
    listeners.clear();
  }

  /**
   * Register a listener object
   * The class must have methods annotated with {@link NookSubscribe}
   * in order to work, otherwise it will be ignored
   *
   * @param listener Listener object to register
   */
  public void registerListenerReference(@NotNull Reference<Object> listener) {
    Objects.requireNonNull(listener, "Listener cannot be null");

    Class<?> clazz = listener.getClass();

    for (Method method : clazz.getDeclaredMethods()) {
      if (!method.isAnnotationPresent(NookSubscribe.class)) {
        continue;
      }

      logger.debug("Registering event handler " + method.getName() + " in " + clazz.getName());

      NookSubscribe nookSubscribe = method.getAnnotation(NookSubscribe.class);

      Class<?>[] parameterTypes = method.getParameterTypes();

      if (parameterTypes.length != 1) {
        continue;
      }

      Class<?> parameterType = parameterTypes[0];

      if (!Event.class.isAssignableFrom(parameterType)) {
        throw new EventHandlerException("Event handler method must have a parameter that extends Event");
      }

      @SuppressWarnings({"unchecked"})
      Class<? extends Event> eventClass = (Class<? extends Event>) parameterType;

      if (!listeners.containsKey(eventClass)) {
        listeners.put(eventClass, new ArrayList<>());
      }

      listeners.get(eventClass).add(new EventVector(method, listener, nookSubscribe));
    }
  }

  public void registerListener(@NotNull final Object listener) {
    registerListenerReference(new SoftReference<>(listener));
  }

  public void registerListenerWeakly(@NotNull final Object listener) {
    registerListenerReference(new WeakReference<>(listener));
  }

  /**
   * Call an event
   * This will call all the methods annotated with {@link NookSubscribe}
   * that are listening to the event
   *
   * @param event Event to call
   */
  @SuppressWarnings("UnusedReturnValue")
  public <T extends Event> CompletableFuture<T> fireEvent(@NotNull T event) {
    Objects.requireNonNull(event, "Event cannot be null");
    logger.debug("Firing event " + event.getClass().getName());

    List<EventVector> eventVectors = listeners.get(event.getClass());

    if (eventVectors == null) return CompletableFuture.completedFuture(event);
    if (eventVectors.isEmpty()) return CompletableFuture.completedFuture(event);

    eventVectors.sort((o1, o2) -> {
      NookSubscribe nookSubscribe1 = o1.nookSubscribe();
      NookSubscribe nookSubscribe2 = o2.nookSubscribe();

      return Integer.compare(nookSubscribe1.priority().getSlot(), nookSubscribe2.priority().getSlot());
    });

    return CompletableFuture.supplyAsync(() -> {
      eventVectors.forEach(eventVector -> {
        try {
          final var listener = eventVector.listener().get();

          if (listener == null) {
            unregisterListener(eventVector.listener());
            return;
          }

          eventVector.method().invoke(eventVector.listener(), event);
        } catch (Exception e) {
          throw new EventHandlerException("Could not invoke event handler", e);
        }
      });

      return event;
    });

  }
}
