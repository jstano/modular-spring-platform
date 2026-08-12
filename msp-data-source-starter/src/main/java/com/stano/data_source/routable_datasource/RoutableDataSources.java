package com.stano.data_source.routable_datasource;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

/**
 * Map-like holder of routed data sources (or other routing targets) keyed by tenant identifier.
 *
 * <p>Instances are mutable only from within this package: {@link #put(Object, Object)} is allowed,
 * but {@link #remove(Object)}, {@link #putAll(Map)}, and {@link #clear()} are unsupported from
 * outside callers since removal is expected to go through the package-private {@link
 * #removeInternal(Object)}, keeping {@link RoutableDataSource} in full control of its target map.
 *
 * @param <K> the type of the routing key (typically the tenant/database identifier)
 * @param <V> the type of the routing target (typically a {@link javax.sql.DataSource})
 */
public class RoutableDataSources<K, V> {
  private final Map<K, V> dataSourceMap = new HashMap<>();

  /**
   * Creates an instance seeded with the entries of the given map.
   *
   * @param dataSourceMap the initial entries to copy in
   */
  public RoutableDataSources(Map<K, V> dataSourceMap) {
    this.dataSourceMap.putAll(dataSourceMap);
  }

  /**
   * Returns the number of registered entries.
   *
   * @return the number of entries
   */
  public int size() {
    return dataSourceMap.size();
  }

  /**
   * Returns whether there are no registered entries.
   *
   * @return {@code true} if there are no entries
   */
  public boolean isEmpty() {
    return dataSourceMap.isEmpty();
  }

  /**
   * Returns whether the given key is registered.
   *
   * @param key the key to check
   * @return {@code true} if an entry exists for the given key
   */
  public boolean containsKey(K key) {
    return dataSourceMap.containsKey(key);
  }

  /**
   * Returns whether the given value is registered under any key.
   *
   * @param value the value to check
   * @return {@code true} if the value is present
   */
  public boolean containsValue(V value) {
    return dataSourceMap.containsValue(value);
  }

  /**
   * Returns the value registered for the given key.
   *
   * @param key the key to look up
   * @return the value for the key, or {@code null} if none is registered
   */
  public V get(K key) {
    return dataSourceMap.get(key);
  }

  /**
   * Registers a value under the given key, replacing any existing value.
   *
   * @param key the key to register under
   * @param value the value to register
   * @return the previously registered value, or {@code null} if none was registered
   */
  public V put(K key, V value) {
    return dataSourceMap.put(key, value);
  }

  /**
   * Unsupported from outside callers; use the package-private {@link #removeInternal(Object)}
   * instead.
   *
   * @param key ignored
   * @return never returns normally
   * @throws UnsupportedOperationException always
   */
  public V remove(K key) {
    throw new UnsupportedOperationException();
  }

  /**
   * Removes the entry for the given key. Package-private so only {@link RoutableDataSource} can
   * remove stale entries.
   *
   * @param key the key to remove
   * @return the previously registered value, or {@code null} if none was registered
   */
  V removeInternal(K key) {
    return dataSourceMap.remove(key);
  }

  /**
   * Unsupported.
   *
   * @param m ignored
   * @throws UnsupportedOperationException always
   */
  public void putAll(Map<K, V> m) {
    throw new UnsupportedOperationException();
  }

  /**
   * Unsupported.
   *
   * @throws UnsupportedOperationException always
   */
  public void clear() {
    throw new UnsupportedOperationException();
  }

  /**
   * Returns the set of registered keys.
   *
   * @return the registered keys
   */
  public Set<K> keySet() {
    return dataSourceMap.keySet();
  }

  /**
   * Returns the registered values.
   *
   * @return the registered values
   */
  public Collection<V> values() {
    return dataSourceMap.values();
  }

  /**
   * Returns the registered key/value entries.
   *
   * @return the registered entries
   */
  public Set<Map.Entry<K, V>> entrySet() {
    return dataSourceMap.entrySet();
  }

  /**
   * Invokes the given consumer for each registered key/value pair.
   *
   * @param consumer the consumer to invoke for each entry
   */
  public void forEach(BiConsumer<K, V> consumer) {
    dataSourceMap.forEach(consumer);
  }
}
