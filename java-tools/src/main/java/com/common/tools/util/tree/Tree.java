package com.common.tools.util.tree;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 构建树形结构
 *
 * @author gongliangjun 2020/06/15 3:33 PM
 */
public final class Tree {

  /**
   * 根据子节点构建树形结构
   *
   * @param descInheritanceRelationships 按继承关系倒序排列的集合.
   * @param keyNodeMap Key  节点映射.
   * @param callback 回调接口，在遍历时处理子节点.
   * @param <K> Key.
   * @param <T> Node.
   * @return list.
   */
  public static <K, T> List<T> buildTree(
      List<List<T>> descInheritanceRelationships,
      Map<K, T> keyNodeMap,
      TreeBuildCallback<K, T> callback) {
    Set<K> rootContainer = new HashSet<>();
    Set<K> traversedNodes = new HashSet<>();
    // relationship 按照当前子节点到父节点的顺序溯源排序，处理也是按此顺序
    for (List<T> relationship : descInheritanceRelationships) {
      // 当前节点的 key 则为下次处理的 childKey
      K childKey = null;
      int stop = relationship.size() - 1;
      for (int i = 0, len = relationship.size(); i < len; i++) {
        T node = relationship.get(i);
        K currentKey = callback.getKey(node);
        // 跳过初次处理
        if (childKey != null) {
          createRelationship(childKey, currentKey, keyNodeMap, callback);
        }
        // 当前节点处理过，表示其后的所有节点也已经处理
        if (traversedNodes.contains(currentKey)) {
          break;
        }
        traversedNodes.add(currentKey);
        childKey = currentKey;

        // 遍历到最后一个节点时，表示这是一棵独立的树，记录下根节点
        if (i == stop) {
          rootContainer.add(currentKey);
        }
      }
    }

    // 可能需要对节点再处理
    buildNode(keyNodeMap, callback);

    // 排序
    return rootContainer.stream()
        .map(keyNodeMap::get)
        .sorted(callback.sort())
        .collect(Collectors.toList());
  }

  /**
   * 处理节点
   *
   * @param keyNodeMap Key  节点映射
   * @param callback 处理回调接口
   * @param <K> Key
   * @param <T> Node
   */
  private static <K, T> void buildNode(Map<K, T> keyNodeMap, TreeBuildCallback<K, T> callback) {
    for (T value : keyNodeMap.values()) {
      callback.buildNode(value);
    }
  }

  /**
   * 创建关联关系
   *
   * @param childKey 子节点 Key
   * @param parentKey 父节点 Key
   * @param keyNodeMap Key -> 节点映射
   * @param callback 处理回调接口
   * @param <K> Key
   * @param <T> Node
   */
  private static <K, T> void createRelationship(
      K childKey, K parentKey, Map<K, T> keyNodeMap, TreeBuildCallback<K, T> callback) {
    T parent = keyNodeMap.get(parentKey);
    T child = keyNodeMap.get(childKey);
    callback.withChild(parent, child);
  }
}
