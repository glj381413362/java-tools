package com.common.tools.util.tree;

import java.util.Comparator;
import java.util.List;

/**
 * 树结构构建工具相关接口
 *
 */
public interface TreeBuildCallback<K, T> {

	/**
	 * 获取节点上下级关联 Key
	 *
	 * @param node 节点
	 * @return Key
	 */
	K getKey(T node);

	/**
	 * 构建节点
	 *
	 * @param node 节点
	 */
	void buildNode(T node);

	/**
	 * 上下级关联
	 *
	 * @param parent T 父节点
	 * @param child  T 子节点
	 */
	void withChild(T parent, T child);

	/**
	 * 上下节点关联
	 *
	 * @param parent   父节点
	 * @param children 子节点们
	 */
	void withChildren(T parent, List<T> children);

	/**
	 * 节点排序规则
	 *
	 * @return Comparator
	 */
	Comparator<T> sort();

}
