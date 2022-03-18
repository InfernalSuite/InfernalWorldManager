package com.infernalsuite.iwm.common.event.gen;

import com.infernalsuite.iwm.api.InfernalWorldManager;
import com.infernalsuite.iwm.api.event.IWMEvent;
import com.infernalsuite.iwm.api.event.util.Param;
import com.infernalsuite.iwm.common.cache.LoadingMap;
import com.infernalsuite.iwm.common.event.IWMEventDispatcher;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.ClassFileVersion;
import net.bytebuddy.description.NamedElement;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.scaffold.subclass.ConstructorStrategy;
import net.bytebuddy.implementation.FieldAccessor;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.implementation.MethodCall;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;

import static net.bytebuddy.matcher.ElementMatchers.*;

/**
 * Holds the generated event class for a given type of {@link IWMEvent}
 */
public class GeneratedEventClass {

    /**
     * Loading cache of event types to {@link GeneratedEventClass}
     */
    private static final Map<Class<? extends IWMEvent>, GeneratedEventClass> CACHE = LoadingMap.of(clazz -> {
        try {
            return new GeneratedEventClass(clazz);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    });

    /**
     * Generates a {@link GeneratedEventClass} for the given {@code event} type.
     *
     * @param event the event type
     * @return the generated class
     */
    public static GeneratedEventClass generate(Class<? extends IWMEvent> event) { return CACHE.get(event); }

    /**
     * Pre-generates {@link GeneratedEventClass generated event classes} for known event types.
     */
    public static void preGenerate() {
        for (Class<? extends IWMEvent> event : IWMEventDispatcher.getKnownEventTypes()) {
            generate(event);
        }
    }

    /**
     * A method handle for the constructor of the event class
     */
    private final MethodHandle constructor;

    /**
     * An array of {@link MethodHandle method handles} which can set values for each of the properties in the event class.
     */
    private final MethodHandle[] setters;

    /**
     * Creates a new {@link GeneratedEventClass} for the given event type
     *
     * @param eventClass the event type
     * @throws Throwable if something goes wrong
     */
    private GeneratedEventClass(Class<? extends IWMEvent> eventClass) throws Throwable {
        TypeDescription eventClassType = new TypeDescription.ForLoadedType(eventClass);

        String eventClassSuffix = eventClass.getName().substring(IWMEvent.class.getPackage().getName().length());
        String packageWithName = GeneratedEventClass.class.getName();
        String generatedClassName = packageWithName.substring(0, packageWithName.lastIndexOf('.')) + eventClassSuffix;

        DynamicType.Builder<AbstractEvent> builder = new ByteBuddy(ClassFileVersion.JAVA_V11)
                .subclass(AbstractEvent.class, ConstructorStrategy.Default.IMITATE_SUPER_CLASS_OPENING)
                .name(generatedClassName)
                .implement(eventClassType)
                .method(isAnnotatedWith(Param.class))
                    .intercept(FieldAccessor.of(NamedElement.WithRuntimeName::getInternalName))
                .method(named("getEventType").and(returns(Class.class)).and(takesArguments(0)))
                    .intercept(FixedValue.value(eventClassType))
                .method(named("mhl").and(returns(MethodHandles.Lookup.class)).and(takesArguments(0)))
                    .intercept(MethodCall.invoke(MethodHandles.class.getMethod("lookup")))
                .withToString();

        Method[] properties = Arrays.stream(eventClass.getMethods())
                .filter(m -> m.isAnnotationPresent(Param.class))
                .sorted(Comparator.comparingInt(m -> m.getAnnotation(Param.class).value()))
                .toArray(Method[]::new);

        for (Method method : properties) {
            builder = builder.defineField(method.getName(), method.getReturnType(), Visibility.PRIVATE);
        }

        Class<? extends AbstractEvent> generatedClass = builder.make().load(GeneratedEventClass.class.getClassLoader()).getLoaded();
        this.constructor = MethodHandles.publicLookup().in(generatedClass)
                .findConstructor(generatedClass, MethodType.methodType(void.class, InfernalWorldManager.class))
                .asType(MethodType.methodType(AbstractEvent.class, InfernalWorldManager.class));

        MethodHandles.Lookup lookup = ((AbstractEvent) this.constructor.invoke((Object) null)).mhl();

        this.setters = new MethodHandle[properties.length];
        for (int i = 0; i < properties.length; i++) {
            Method method = properties[i];
            this.setters[i] = lookup.findSetter(generatedClass, method.getName(), method.getReturnType())
                    .asType(MethodType.methodType(void.class, new Class[]{AbstractEvent.class, Object.class}));
        }
    }

    /**
     * Creates a new instance of the event.
     *
     * @param api an instance of the {@link InfernalWorldManager IWM API}
     * @param properties the event properties
     * @return the event instance
     * @throws Throwable if something goes wrong
     */
    public IWMEvent newInstance(InfernalWorldManager api, Object... properties) throws Throwable {
        if (properties.length != this.setters.length) {
            throw new IllegalStateException("Unexpected number of properties. Given: " + properties.length + ", Expected: " + this.setters.length);
        }

        final AbstractEvent event = (AbstractEvent) this.constructor.invokeExact(api);

        for (int i = 0; i < this.setters.length; i++) {
            MethodHandle setter = this.setters[i];
            Object value = properties[i];
            setter.invokeExact(event, value);
        }

        return event;
    }

}
