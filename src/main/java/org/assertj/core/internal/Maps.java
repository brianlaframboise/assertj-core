/*
 * Created on Dec 21, 2010
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 * 
 * Copyright @2010-2011 the original author or authors.
 */
package org.assertj.core.internal;

import static org.assertj.core.error.ShouldBeEmpty.shouldBeEmpty;
import static org.assertj.core.error.ShouldBeNullOrEmpty.shouldBeNullOrEmpty;
import static org.assertj.core.error.ShouldContain.shouldContain;
import static org.assertj.core.error.ShouldContainKeys.shouldContainKeys;
import static org.assertj.core.error.ShouldContainValue.shouldContainValue;
import static org.assertj.core.error.ShouldHaveSameSizeAs.shouldHaveSameSizeAs;
import static org.assertj.core.error.ShouldHaveSize.shouldHaveSize;
import static org.assertj.core.error.ShouldNotBeEmpty.shouldNotBeEmpty;
import static org.assertj.core.error.ShouldNotContain.shouldNotContain;
import static org.assertj.core.error.ShouldNotContainKey.shouldNotContainKey;
import static org.assertj.core.error.ShouldNotContainValue.shouldNotContainValue;
import static org.assertj.core.internal.CommonValidations.checkSizes;
import static org.assertj.core.internal.CommonValidations.hasSameSizeAsCheck;
import static org.assertj.core.util.Iterables.sizeOf;
import static org.assertj.core.util.Objects.areEqual;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.data.MapEntry;
import org.assertj.core.util.VisibleForTesting;

/**
 * Reusable assertions for <code>{@link Map}</code>s.
 * 
 * @author Alex Ruiz
 * @author Nicolas François
 */
public class Maps {

  private static final Maps INSTANCE = new Maps();

  /**
   * Returns the singleton instance of this class.
   * 
   * @return the singleton instance of this class.
   */
  public static Maps instance() {
    return INSTANCE;
  }

  @VisibleForTesting
  Failures failures = Failures.instance();

  @VisibleForTesting
  Maps() {
  }

  /**
   * Asserts that the given {@code Map} is {@code null} or empty.
   * 
   * @param info contains information about the assertion.
   * @param actual the given map.
   * @throws AssertionError if the given {@code Map} is not {@code null} *and* contains one or more entries.
   */
  public void assertNullOrEmpty(AssertionInfo info, Map<?, ?> actual) {
    if (actual == null || actual.isEmpty()) {
      return;
    }
    throw failures.failure(info, shouldBeNullOrEmpty(actual));
  }

  /**
   * Asserts that the given {@code Map} is empty.
   * 
   * @param info contains information about the assertion.
   * @param actual the given {@code Map}.
   * @throws AssertionError if the given {@code Map} is {@code null}.
   * @throws AssertionError if the given {@code Map} is not empty.
   */
  public void assertEmpty(AssertionInfo info, Map<?, ?> actual) {
    assertNotNull(info, actual);
    if (actual.isEmpty()) {
      return;
    }
    throw failures.failure(info, shouldBeEmpty(actual));
  }

  /**
   * Asserts that the given {@code Map} is not empty.
   * 
   * @param info contains information about the assertion.
   * @param actual the given {@code Map}.
   * @throws AssertionError if the given {@code Map} is {@code null}.
   * @throws AssertionError if the given {@code Map} is empty.
   */
  public void assertNotEmpty(AssertionInfo info, Map<?, ?> actual) {
    assertNotNull(info, actual);
    if (!actual.isEmpty()) {
      return;
    }
    throw failures.failure(info, shouldNotBeEmpty());
  }

  /**
   * Asserts that the number of entries in the given {@code Map} is equal to the expected one.
   * 
   * @param info contains information about the assertion.
   * @param actual the given {@code Map}.
   * @param expectedSize the expected size of {@code actual}.
   * @throws AssertionError if the given {@code Map} is {@code null}.
   * @throws AssertionError if the number of entries in the given {@code Map} is different than the expected one.
   */
  public void assertHasSize(AssertionInfo info, Map<?, ?> actual, int expectedSize) {
    assertNotNull(info, actual);
    checkSizes(actual, actual.size(), expectedSize, info);
  }

  /**
   * Asserts that the number of entries in the given {@code Map} has the same size as the other {@code Iterable}.
   * 
   * @param info contains information about the assertion.
   * @param map the given {@code Map}.
   * @param other the group to compare
   * @throws AssertionError if the given {@code Map} is {@code null}.
   * @throws AssertionError if the given {@code Iterable} is {@code null}.
   * @throws AssertionError if the number of entries in the given {@code Map} does not have the same size.
   */
  public void assertHasSameSizeAs(AssertionInfo info, Map<?, ?> map, Iterable<?> other) {
    assertNotNull(info, map);
    hasSameSizeAsCheck(info, map, other, map.size());
  }

  /**
   * Asserts that the number of entries in the given {@code Map} has the same size as the other array.
   * 
   * @param info contains information about the assertion.
   * @param map the given {@code Map}.
   * @param other the group to compare
   * @throws AssertionError if the given {@code Map} is {@code null}.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the number of entries in the given {@code Map} does not have the same size.
   */
  public void assertHasSameSizeAs(AssertionInfo info, Map<?, ?> map, Object[] other) {
    Objects.instance().assertNotNull(info, map);
    hasSameSizeAsCheck(info, map, other, map.size());
  }

  /**
   * Asserts that the given {@code Map} contains the given entries, in any order.
   * 
   * @param info contains information about the assertion.
   * @param actual the given {@code Map}.
   * @param entries the entries that are expected to be in the given {@code Map}.
   * @throws NullPointerException if the array of entries is {@code null}.
   * @throws IllegalArgumentException if the array of entries is empty.
   * @throws NullPointerException if any of the entries in the given array is {@code null}.
   * @throws AssertionError if the given {@code Map} is {@code null}.
   * @throws AssertionError if the given {@code Map} does not contain the given entries.
   */
  public void assertContains(AssertionInfo info, Map<?, ?> actual, MapEntry[] entries) {
    isNotNull(entries);
    assertNotNull(info, actual);
    // if both actual and values are empty, then assertion passes.
    if (actual.isEmpty() && entries.length == 0)
      return;
    failIfEmptySinceActualIsNotEmpty(entries);
    Set<MapEntry> notFound = new LinkedHashSet<MapEntry>();
    for (MapEntry entry : entries) {
      if (!containsEntry(actual, entry)) {
        notFound.add(entry);
      }
    }
    if (notFound.isEmpty()) {
      return;
    }
    throw failures.failure(info, shouldContain(actual, entries, notFound));
  }

  /**
   * Asserts that the given {@code Map} does not contain the given entries.
   * 
   * @param info contains information about the assertion.
   * @param actual the given {@code Map}.
   * @param entries the entries that are expected to be in the given {@code Map}.
   * @throws NullPointerException if the array of entries is {@code null}.
   * @throws IllegalArgumentException if the array of entries is empty.
   * @throws NullPointerException if any of the entries in the given array is {@code null}.
   * @throws AssertionError if the given {@code Map} is {@code null}.
   * @throws AssertionError if the given {@code Map} contains any of the given entries.
   */
  public void assertDoesNotContain(AssertionInfo info, Map<?, ?> actual, MapEntry[] entries) {
    isNotEmptyOrNull(entries);
    assertNotNull(info, actual);
    Set<MapEntry> found = new LinkedHashSet<MapEntry>();
    for (MapEntry entry : entries) {
      if (containsEntry(actual, entry)) {
        found.add(entry);
      }
    }
    if (found.isEmpty()) {
      return;
    }
    throw failures.failure(info, shouldNotContain(actual, entries, found));
  }

  /**
   * Verifies that the actual map contain the given key.
   * 
   * @param info contains information about the assertion.
   * @param actual the given {@code Map}.
   * @param keys the given keys
   * @throws AssertionError if the actual map is {@code null}.
   * @throws AssertionError if the actual map not contains the given key.
   */
  public <K, V> void assertContainsKeys(AssertionInfo info, Map<K, V> actual, K... keys) {
    assertNotNull(info, actual);
    Set<K> notFound = new LinkedHashSet<K>();
    for (K key : keys) {
      if (!actual.containsKey(key)) {
        notFound.add(key);
      }
    }
    if (notFound.isEmpty()) {
      return;
    }
    throw failures.failure(info, shouldContainKeys(actual, notFound));
  }

  /**
   * Verifies that the actual map not contains the given key.
   * 
   * @param info contains information about the assertion.
   * @param actual the given {@code Map}.
   * @param key the given key
   * @throws AssertionError if the actual map is {@code null}.
   * @throws AssertionError if the actual map contains the given key.
   */
  public <K, V> void assertDoesNotContainKey(AssertionInfo info, Map<K, V> actual, K key) {
    assertNotNull(info, actual);
    if (!actual.containsKey(key)) {
      return;
    }
    throw failures.failure(info, shouldNotContainKey(actual, key));
  }

  /**
   * Verifies that the actual map contain the given value.
   * 
   * @param info contains information about the assertion.
   * @param actual the given {@code Map}.
   * @param value the given value
   * @throws AssertionError if the actual map is {@code null}.
   * @throws AssertionError if the actual map not contains the given value.
   */
  public <K, V> void assertContainsValue(AssertionInfo info, Map<K, V> actual, V value) {
    assertNotNull(info, actual);
    if (actual.containsValue(value)) {
      return;
    }
    throw failures.failure(info, shouldContainValue(actual, value));
  }

  /**
   * Verifies that the actual map not contains the given value.
   * 
   * @param info contains information about the assertion.
   * @param actual the given {@code Map}.
   * @param value the given value
   * @throws AssertionError if the actual map is {@code null}.
   * @throws AssertionError if the actual map contains the given value.
   */
  public <K, V> void assertDoesNotContainValue(AssertionInfo info, Map<K, V> actual, V value) {
    assertNotNull(info, actual);
    if (!actual.containsValue(value)) {
      return;
    }
    throw failures.failure(info, shouldNotContainValue(actual, value));
  }

  private static void isNotEmptyOrNull(MapEntry[] entries) {
    isNotNull(entries);
    if (entries.length == 0) {
      throw new IllegalArgumentException("The array of entries to look for should not be empty");
    }
  }

  private static void isNotNull(MapEntry[] entries) {
    if (entries == null) {
      throw new NullPointerException("The array of entries to look for should not be null");
    }
  }

  private boolean containsEntry(Map<?, ?> actual, MapEntry entry) {
    if (entry == null) {
      throw new NullPointerException("Entries to look for should not be null");
    }
    if (!actual.containsKey(entry.key)) {
      return false;
    }
    return areEqual(actual.get(entry.key), entry.value);
  }

  private void assertNotNull(AssertionInfo info, Map<?, ?> actual) {
    Objects.instance().assertNotNull(info, actual);
  }

  private static void failIfEmptySinceActualIsNotEmpty(MapEntry[] values) {
    if (values.length == 0)
      throw new AssertionError("actual is not empty");
  }
}
