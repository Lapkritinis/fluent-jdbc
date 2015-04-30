package org.codejargon.fluentjdbc.internal.support;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class FindFirst<T> {
    private static final Predicate notNull = e -> e != null;

    private Optional<Iterable<T>> elements;
    private Optional<Iterable<Supplier<T>>> elementsLazy;

    public FindFirst(
            Optional<Iterable<T>> elements,
            Optional<Iterable<Supplier<T>>> elementsLazy
    ) {
        this.elements = elements;
        this.elementsLazy = elementsLazy;
    }


    @SafeVarargs
    public static <T> FindFirst<T> from(T... elements) {
        return from(Arrays.asList(elements));
    }

    public static <T> FindFirst<T> from(Iterable<T> elements) {
        return new FindFirst<>(Optional.of(elements), Optional.empty());
    }

    @SafeVarargs
    public static <T> FindFirst<T> fromLazy(Supplier<T>... suppliers) {
        return fromLazy(Arrays.asList(suppliers));
    }

    public static <T> FindFirst<T> fromLazy(Iterable<Supplier<T>> suppliers) {
        return new FindFirst<>(Optional.empty(), Optional.of(suppliers));
    }

    public Optional<T> which(Predicate<T> predicate) {
        return stream().filter(predicate).findFirst();
    }

    @SuppressWarnings("unchecked")
    public Optional<T> whichIsNotNull() {
        return which(notNull);
    }

    private Stream<T> stream() {
        return elements.isPresent() ?
                Iterables.stream(elements.get()) :
                Iterables.stream(elementsLazy.get()).map(Supplier::get);
    }

}
