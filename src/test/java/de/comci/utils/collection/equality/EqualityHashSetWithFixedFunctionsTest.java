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

import java.util.Set;
import static org.fest.assertions.Assertions.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Sebastian Maier (sebastian@comci.de)
 */
public class EqualityHashSetWithFixedFunctionsTest {

    private Set<Integer> instance;

    @Before
    public void createInstance() {
        instance = new EqualityHashSet<>((a, b) -> true, a -> 1);
    }
    
    @Test
    public void shouldAddFirstElement() {
        assertThat(instance.add(1)).isTrue();
        assertThat(instance).hasSize(1).containsOnly(1);
    }
    
    @Test
    public void shouldNotAddAnyMoreElement() {
        assertThat(instance.add(1)).isTrue();
        assertThat(instance.add(2)).isFalse();
        assertThat(instance).hasSize(1).containsOnly(1);
    }
    
    @Test
    public void shouldRemove() {
        assertThat(instance.add(1)).isTrue();
        assertThat(instance.remove(2)).isTrue();
        assertThat(instance).isEmpty();
    }

}
