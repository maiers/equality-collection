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

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import static org.fest.assertions.Assertions.*;
import static org.fest.assertions.Fail.fail;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Sebastian Maier (sebastian@comci.de)
 */
public class EqualityHashSetWithDefaultFunctionsTest {

    private Set<Integer> instance;

    @Before
    public void createInstance() {
        instance = new EqualityHashSet<>((a, b) -> Objects.equals(a, b), a -> Objects.hashCode(a));
    }

    @Test
    public void shouldAddAll() {
        assertThat(instance.addAll(Arrays.asList(3, 4, 5))).isTrue();
        assertThat(instance).hasSize(3).contains(3, 4, 5);
    }

    @Test
    public void shouldBeIterable() {
        assertThat(instance.addAll(Arrays.asList(1, 2, 3))).isTrue();
        Iterator actual = instance.iterator();
        assertThat(actual).isNotNull();
        for (int i = 1; i < 4; i++) {
            assertThat(actual.next()).isNotNull().isEqualTo(i);
        }
        try {
            actual.next();
            fail();
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NoSuchElementException.class);
        }
    }

    @Test
    public void shouldContainAll() {
        assertThat(instance.addAll(Arrays.asList(1, 2, 3))).isTrue();
        assertThat(instance.containsAll(Arrays.asList(1, 3))).isTrue();
    }

    @Test
    public void shouldContainValue() {
        assertThat(instance.add(1)).isTrue();
        assertThat(instance.contains(1)).isTrue();
    }

    @Test
    public void shouldConvertToArray() {
        assertThat(instance.addAll(Arrays.asList(1, 2))).isTrue();
        assertThat(instance.toArray()).hasSize(2).containsOnly(1, 2).isEqualTo(new Integer[]{1, 2});
    }

    @Test
    public void shouldConvertToArrayWithSizeFits() {
        assertThat(instance.addAll(Arrays.asList(1, 2))).isTrue();
        assertThat(instance.toArray(new Integer[2])).hasSize(2).containsOnly(1, 2).isEqualTo(new Integer[]{1, 2});
    }

    @Test
    public void shouldConvertToArrayWithSizeTooBig() {
        assertThat(instance.addAll(Arrays.asList(1, 2))).isTrue();
        assertThat(instance.toArray(new Integer[5])).hasSize(5).containsOnly(1, 2, null).isEqualTo(new Integer[]{1, 2, null, null, null});
    }

    @Test
    public void shouldConvertToArrayWithSizeTooSmall() {
        assertThat(instance.addAll(Arrays.asList(1, 2))).isTrue();
        assertThat(instance.toArray(new Integer[1])).hasSize(2).containsOnly(1, 2).isEqualTo(new Integer[]{1, 2});
    }

    @Test
    public void shouldInitiallyBeEmpty() {
        assertThat(instance.isEmpty()).isTrue();
    }

    @Test
    public void shouldNotAddAllIfNothingNew() {
        assertThat(instance.add(1)).isTrue();
        assertThat(instance.add(2)).isTrue();
        assertThat(instance.addAll(Arrays.asList(1, 2))).isFalse();
        assertThat(instance).hasSize(2).contains(1, 2);
    }

    @Test
    public void shouldNotAddTheSameValueAgain() {
        assertThat(instance.add(1)).isTrue();
        assertThat(instance.add(1)).isFalse();
        assertThat(instance).hasSize(1).containsOnly(1);
    }

    @Test
    public void shouldNotChangeIfAddAllIsEmpty() {
        assertThat(instance.addAll(Arrays.asList())).isFalse();
        assertThat(instance).isEmpty();
    }

    @Test
    public void shouldNotContainAll() {
        assertThat(instance.addAll(Arrays.asList(1, 2, 3))).isTrue();
        assertThat(instance.containsAll(Arrays.asList(3, 5))).isFalse();
    }

    @Test
    public void shouldNotContainValue() {
        assertThat(instance.contains(1)).isFalse();
    }

    @Test
    public void shouldNotRemoveAllWhenNonPresent() {
        assertThat(instance.addAll(Arrays.asList(1, 2))).isTrue();
        assertThat(instance.removeAll(Arrays.asList(3, 4))).isFalse();
        assertThat(instance).hasSize(2).containsOnly(1, 2);
    }

    @Test
    public void shouldNotRemoveWhatIsNotPresent() {
        assertThat(instance.add(1)).isTrue();
        assertThat(instance.remove(2)).isFalse();
        assertThat(instance).hasSize(1).containsOnly(1);
    }

    @Test
    public void shouldOnlyAddAllWhatIsNotPresentYet() {
        assertThat(instance.add(1)).isTrue();
        assertThat(instance.addAll(Arrays.asList(1, 2, 3))).isTrue();
        assertThat(instance).hasSize(3).contains(1, 2, 3);
    }

    @Test
    public void shouldRemoveAll() {
        assertThat(instance.addAll(Arrays.asList(1, 2))).isTrue();
        assertThat(instance.removeAll(Arrays.asList(1, 2))).isTrue();
        assertThat(instance).isEmpty();
    }

    @Test
    public void shouldRemoveAllWhenNotAllPresent() {
        assertThat(instance.addAll(Arrays.asList(1, 2))).isTrue();
        assertThat(instance.removeAll(Arrays.asList(1, 2, 3, 4))).isTrue();
        assertThat(instance).isEmpty();
    }

    @Test
    public void shouldRetainAll() {
        assertThat(instance.addAll(Arrays.asList(1, 2, 3))).isTrue();
        assertThat(instance.retainAll(Arrays.asList(1, 2, 3))).isFalse();
        assertThat(instance).hasSize(3).containsOnly(1, 2, 3);
    }

    @Test
    public void shouldRetainSome() {
        assertThat(instance.addAll(Arrays.asList(1, 2, 3))).isTrue();
        assertThat(instance.retainAll(Arrays.asList(1, 2))).isTrue();
        assertThat(instance).hasSize(2).containsOnly(1, 2);
    }

    @Test
    public void shouldSuccessfullyRemove() {
        assertThat(instance.add(1)).isTrue();
        assertThat(instance.remove(1)).isTrue();
        assertThat(instance).isEmpty();
    }

    @Test
    public void shouldSucessfullyAdd() {
        assertThat(instance.add(1)).isTrue();
        assertThat(instance).hasSize(1).containsOnly(1);
    }

    @Test
    public void shouldWorkForFIxedEqualsAndHash() {

        EqualityHashSet<Integer> i = new EqualityHashSet<>((a, b) -> true, a -> 1);

        assertThat(i.add(1)).isTrue();
        assertThat(i).hasSize(1);

        assertThat(i.add(5)).isFalse();
        assertThat(i).hasSize(1);

        assertThat(i.add(1)).isFalse();
        assertThat(i).hasSize(1);

        assertThat(i.remove(5)).isTrue();
        assertThat(i).isEmpty();

    }

    @Test
    public void shouldWorkForSomeMoreElaborateFunction() {

        EqualityHashSet<Integer> i = new EqualityHashSet<>((a, b) -> {
            return (a % 2 == 0 && b % 2 == 0);
        }, a -> {
            return (a % 2 == 0) ? 2 : a;
        });

        assertThat(i.add(1)).isTrue();
        assertThat(i).hasSize(1);

        assertThat(i.add(2)).isTrue();
        assertThat(i).hasSize(2);

        assertThat(i.add(3)).isTrue();
        assertThat(i).hasSize(3);

        assertThat(i.add(4)).isFalse();
        assertThat(i).hasSize(3);

        assertThat(i.add(5)).isTrue();
        assertThat(i).hasSize(4);

    }

    @Test
    public void shouldWorkForInitialElements() {

        EqualityHashSet<Integer> i = new EqualityHashSet<>(Arrays.asList(1, 2, 3, 4, 5, 6), (a, b) -> {
            return (a % 2 == 0 && b % 2 == 0);
        }, a -> {
            return (a % 2 == 0) ? 2 : a;
        });

        assertThat(i).hasSize(4).contains(1, 2, 3, 5);

    }

    /**
     * This requires the equality and hash functions to be able to handle
     * null values.
     */
    @Test
    public void shouldAddNullElement() {
        assertThat(instance.add(null)).isTrue();
        assertThat(instance).hasSize(1).containsOnly((Object) null);
    }

    /**
     * This requires the equality and hash functions to be able to handle
     * null values.
     */
    @Test
    public void shouldAddNullElementUsingAddAll() {
        assertThat(instance.addAll(Arrays.asList(1, null, 3))).isTrue();
        assertThat(instance).hasSize(3).containsOnly(1, null, 3);
    }

    /**
     * This requires the equality and hash functions to be able to handle
     * null values.
     */
    @Test
    public void shouldNotRemoveNonExistentNullElement() {
        assertThat(instance.add(1)).isTrue();
        assertThat(instance.remove(null)).isFalse();
        assertThat(instance).hasSize(1).containsOnly(1);
    }

    /**
     * This requires the equality and hash functions to be able to handle
     * null values.
     */
    @Test
    public void shouldRemoveNullElement() {
        assertThat(instance.add(null)).isTrue();
        assertThat(instance.remove(null)).isTrue();
        assertThat(instance).isEmpty();
    }

}
