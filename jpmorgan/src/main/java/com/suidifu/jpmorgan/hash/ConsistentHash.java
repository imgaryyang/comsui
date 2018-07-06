package com.suidifu.jpmorgan.hash;


import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;

public class ConsistentHash <T> {
	private final HashFunction hashFunction;
	private final int numberOfReplicas;
	private final SortedMap<Integer, T> circle = new TreeMap<Integer, T>();

	public ConsistentHash() {
		this(new HashFunction(), 1000, null);
	}

	public ConsistentHash(Collection<T> nodes) {
		this(new HashFunction(), 1000, nodes);
	}

	public ConsistentHash(HashFunction hashFunction, int numberOfReplicas, Collection<T> nodes) {
		this.hashFunction = hashFunction;
		this.numberOfReplicas = numberOfReplicas;
		if (nodes != null && nodes.size() > 0)
			for (T node : nodes)
				addNode(node);
	}

	public void addNode(T node) {
		for (int i = 0; i < numberOfReplicas; i++) {
			circle.put(hashFunction.hash(node.toString() + i), node);
		}
	}

	public void removeNode(T node) {
		for (int i = 0; i < numberOfReplicas; i++) {
			circle.remove(hashFunction.hash(node.toString() + i));
		}
	}

	public T getNode(String businessId) {
		if (circle.isEmpty())
			return null;
		int hash = hashFunction.hash(businessId);
		if (!circle.containsKey(hash)) {
			SortedMap<Integer, T> tailMap = circle.tailMap(hash);
			hash = tailMap.isEmpty() ? circle.firstKey() : tailMap.firstKey();
		}
		return circle.get(hash);
	}

	public int getNodeSize() {
		return this.circle.size();
	}
}
