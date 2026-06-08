package com.stano.domain_jpa.routable_datasource;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

public class RoutableDataSources<K, V> {
  private final Map<K, V> dataSourceMap = new HashMap<>();

  public RoutableDataSources(Map<K, V> dataSourceMap) {
    this.dataSourceMap.putAll(dataSourceMap);
  }

  public int size() {
    return dataSourceMap.size();
  }

  public boolean isEmpty() {
    return dataSourceMap.isEmpty();
  }

  public boolean containsKey(K key) {
    return dataSourceMap.containsKey(key);
  }

  public boolean containsValue(V value) {
    return dataSourceMap.containsValue(value);
  }

  public V get(K key) {
    return dataSourceMap.get(key);
  }

  public V put(K key, V value) {
    return dataSourceMap.put(key, value);
  }

  public V remove(K key) {
    throw new UnsupportedOperationException();
  }

  V removeInternal(K key) {
    return dataSourceMap.remove(key);
  }

  public void putAll(Map<K, V> m) {
    throw new UnsupportedOperationException();
  }

  public void clear() {
    throw new UnsupportedOperationException();
  }

  public Set<K> keySet() {
    return dataSourceMap.keySet();
  }

  public Collection<V> values() {
    return dataSourceMap.values();
  }

  public Set<Map.Entry<K, V>> entrySet() {
    return dataSourceMap.entrySet();
  }

  public void forEach(BiConsumer<K, V> consumer) {
    dataSourceMap.forEach(consumer);
  }
}
