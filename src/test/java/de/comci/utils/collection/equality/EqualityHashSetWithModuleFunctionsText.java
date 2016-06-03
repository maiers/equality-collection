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
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import static org.fest.assertions.Assertions.*;

/**
 *
 * @author Sebastian Maier (sebastian@comci.de)
 */
public class EqualityHashSetWithModuleFunctionsText {

    private Set<Integer> instance;

    @Before
    public void createInstance() {
        instance = new EqualityHashSet<>((a, b) -> {
            return (a % 2 == 0 && b % 2 == 0);
        }, a -> {
            return (a % 2 == 0) ? 2 : a;
        });
    }

    @Test
    public void shouldAddMultipleUnevenElements() {
        assertThat(instance.add(1)).isTrue();
        assertThat(instance).hasSize(1).containsOnly(1);
        assertThat(instance.add(3)).isTrue();
        assertThat(instance).hasSize(2).containsOnly(1, 3);
        assertThat(instance.addAll(Arrays.asList(5, 7, 9, 11))).isTrue();
        assertThat(instance).hasSize(6).containsOnly(1, 3, 5, 7, 9, 11);
    }

    @Test
    public void shouldAddOnlyOneEvenElement() {
        assertThat(instance.add(2)).isTrue();
        assertThat(instance.add(4)).isFalse();
        assertThat(instance).hasSize(1).containsOnly(2);
        assertThat(instance.addAll(Arrays.asList(6, 8, 10, 12))).isFalse();
        assertThat(instance).hasSize(1).containsOnly(2);
    }

    @Test
    public void shouldWorkForInitialElements() {

        instance = new EqualityHashSet<>(Arrays.asList(1, 2, 3, 4, 5, 6), (a, b) -> {
            return (a % 2 == 0 && b % 2 == 0);
        }, a -> {
            return (a % 2 == 0) ? 2 : a;
        });

        assertThat(instance).hasSize(4).contains(1, 2, 3, 5);
    }

}
