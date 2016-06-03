/*
 * The MIT License
 *
 * Copyright 2016 Sebastian Maier (sebastian@comci.de).
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package de.comci.utils.collection.equality;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

/**
 * A {@link Set} implementation allowing the user to configure deviating equals
 * and hash functions for the sets elements. The original methods will not be
 * used.
 *
 * This class uses a standard {@link HashSet} for element storage.
 *
 * @author Sebastian Maier (sebastian@comci.de)
 *
 * @param <T> The type of this sets elements
 */
public class EqualityHashSet<T> implements Set<T> {

    /**
     * The function defining equality between elements. If you need to handle 
     * null values, make sure your equality function can handle null as well.
     */
    private final BiFunction<T, T, Boolean> equalsFunction;
    
    /**
     * The function defining an elements hashCode. If you need to handle null 
     * values, make sure your hash function can handle null as well.
     */
    private final ToIntFunction<T> hashFunction;
    
    /**
     * The set used for storage
     */
    private final Set<ElementWrapper> backingSet;

    /**
     * Constructs a new, empty set; the backing {@link HashSet} instance has
     * default initial capacity (16) and load factor (0.75) for its backing
     * {@link HashMap}.
     *
     * Users must ensure that their equals and hash functions adheres to the
     * equals contract. If the equals function considers two elements to be
     * equal they should also have the same hashCode.
     *
     * @param equals the function defining equality between elements. If you
     * need to handle null values, make sure your equality function can handle
     * null as well.
     * @param hash the function defining an elements hashCode. If you need to
     * handle null values, make sure your hash function can handle null as well.
     */
    public EqualityHashSet(BiFunction<T, T, Boolean> equals, ToIntFunction<T> hash) {
        this.equalsFunction = equals;
        this.hashFunction = hash;
        this.backingSet = new HashSet<>();
    }

    /**
     * Constructs a new set containing the elements in the specified collection.
     * The {@link HashSet} is created with default load factor (0.75) for its
     * backing {@link HashMap} and an initial capacity sufficient to contain the
     * elements in the specified collection.
     *
     * Users must ensure that their equals and hash functions adheres to the
     * equals contract. If the equals function considers two elements to be
     * equal they should also have the same hashCode.
     *
     * @param c the collection whose elements are to be placed into this set
     * @param equals the function defining equality between elements. If you
     * need to handle null values, make sure your equality function can handle
     * null as well.
     * @param hash the function defining an elements hashCode. If you need to
     * handle null values, make sure your hash function can handle null as well.
     */
    public EqualityHashSet(Collection<T> c, BiFunction<T, T, Boolean> equals, ToIntFunction<T> hash) {
        this.equalsFunction = equals;
        this.hashFunction = hash;
        if (c != null) {
            this.backingSet = c.stream().map(ElementWrapper::new).collect(Collectors.toSet());
        } else {
            this.backingSet = new HashSet<>();
        }
    }

    /**
     * Constructs a new, empty set; the backing {@link HashSet} instance has the
     * specified initial capacity and the specified load factor for its backing
     * {@link HashMap}.
     *
     * Users must ensure that their equals and hash functions adheres to the
     * equals contract. If the equals function considers two elements to be
     * equal they should also have the same hashCode.
     *
     * @param initialCapacity the initial capacity of the hash map
     * @param equals the function defining equality between elements. If you
     * need to handle null values, make sure your equality function can handle
     * null as well.
     * @param hash the function defining an elements hashCode. If you need to
     * handle null values, make sure your hash function can handle null as well.
     */
    public EqualityHashSet(int initialCapacity, BiFunction<T, T, Boolean> equals, ToIntFunction<T> hash) {
        this.equalsFunction = equals;
        this.hashFunction = hash;
        this.backingSet = new HashSet<>(initialCapacity);
    }

    /**
     * Constructs a new, empty set; the backing {@link HashSet} instance has the
     * specified initial capacity and the specified load factor for its backing
     * {@link HashMap}.
     *
     * Users must ensure that their equals and hash functions adheres to the
     * equals contract. If the equals function considers two elements to be
     * equal they should also have the same hashCode.
     *
     * @param initialCapacity the initial capacity of the hash map
     * @param loadFactor the load factor of the hash map
     * @param equals the function defining equality between elements. If you
     * need to handle null values, make sure your equality function can handle
     * null as well.
     * @param hash the function defining an elements hashCode. If you need to
     * handle null values, make sure your hash function can handle null as well.
     */
    public EqualityHashSet(int initialCapacity, float loadFactor, BiFunction<T, T, Boolean> equals, ToIntFunction<T> hash) {
        this.equalsFunction = equals;
        this.hashFunction = hash;
        this.backingSet = new HashSet<>(initialCapacity, loadFactor);
    }

    @Override
    public int size() {
        return backingSet.size();
    }

    @Override
    public boolean isEmpty() {
        return backingSet.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        try {
            return backingSet.contains(new ElementWrapper((T) o));
        } catch (NullPointerException | ClassCastException ex) {
            return false;
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {

            private final Iterator<ElementWrapper> backingIterator = backingSet.iterator();

            @Override
            public boolean hasNext() {
                return backingIterator.hasNext();
            }

            @Override
            public T next() {
                return backingIterator.next().value;
            }
        };
    }

    @Override
    public Object[] toArray() {
        return backingSet.stream().map(w -> w.value).toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {

        int size = backingSet.size();
        T[] r = a.length >= size ? a : (T[]) java.lang.reflect.Array
                .newInstance(a.getClass().getComponentType(), size);

        Iterator<ElementWrapper> iterator = backingSet.iterator();
        for (int i = 0; i < size; i++) {
            r[i] = (T) iterator.next().value;
        }
        return r;
    }

    @Override
    public boolean add(T e) {
        final ElementWrapper wrapper = new ElementWrapper(e);
        return backingSet.add(wrapper);
    }

    @Override
    public boolean remove(Object o) {
        try {
            return backingSet.remove(new ElementWrapper((T) o));
        } catch (NullPointerException | ClassCastException ex) {
            return false;
        }
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        // #contains will take care of any type issues
        return c.stream()
                .map(i -> this.contains(i))
                .reduce((a, b) -> a && b)
                .orElse(false);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        return c.stream()
                .map(i -> this.add(i))
                .reduce((a, b) -> a || b)
                .orElse(false);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        EqualityHashSet<T> other = new EqualityHashSet<>(equalsFunction, hashFunction);
        c.forEach(i -> {
            try {
                other.add((T) i);
            } catch (NullPointerException | ClassCastException ex) {
                // ignore
            }
        });
        return backingSet.retainAll(other.backingSet);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        // #remove will take care of any type issues
        return c.stream()
                .map(i -> remove(i))
                .reduce((a, b) -> a || b)
                .orElse(false);
    }

    @Override
    public void clear() {
        backingSet.clear();
    }

    /**
     * Class to act as element wrapper. Allowing us to use custom equality and
     * hash functions.
     */
    private class ElementWrapper {

        private final T value;

        /**
         * Create a new element wrapper
         *
         * @param value the actual value we want to store
         */
        ElementWrapper(T value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object obj) {

            if (obj == null || !ElementWrapper.class.isAssignableFrom(obj.getClass())) {
                return false;
            }

            try {
                final Boolean isEqual = equalsFunction.apply(this.value, ((ElementWrapper) obj).value);
                return isEqual;
            } catch (NullPointerException | ClassCastException ex) {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return hashFunction.applyAsInt(value);
        }

        @Override
        public String toString() {

            StringBuilder builder = new StringBuilder();

            if (value != null) {
                builder.append(value.toString());
            } else {
                builder.append("null");
            }

            builder.append(" (").append(hashCode()).append(")");

            return builder.toString();
        }

    }

}
