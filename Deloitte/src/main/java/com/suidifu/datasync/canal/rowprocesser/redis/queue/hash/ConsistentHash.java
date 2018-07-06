package com.suidifu.datasync.canal.rowprocesser.redis.queue.hash;

import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;

import com.suidifu.datasync.canal.StaticsConfig.RemittanceType;
import com.suidifu.datasync.canal.rowprocesser.redis.queue.hash.hashfunction.MurmurHashFunction;

public class ConsistentHash {
	private final HashFunction hashFunction;
	private final int numberOfReplicas;
	private final SortedMap<Integer, String> circle = new TreeMap<Integer, String>();

	public ConsistentHash(RemittanceType remittanceType, int quesize) {
		this.hashFunction = new MurmurHashFunction();
		this.numberOfReplicas = 1000;
		for (int queueNum = 1; queueNum <= quesize; queueNum++)
			addNode(remittanceType.syncResultRedisKey(queueNum));
	}

	public ConsistentHash(Collection<String> nodes) {
		this(nodes, new MurmurHashFunction());
	}

	public ConsistentHash(String[] nodes) {
		this(nodes, new MurmurHashFunction());
	}

	public ConsistentHash(Collection<String> nodes, HashFunction hashFunction) {
		this(hashFunction, 1000, nodes);
	}

	public ConsistentHash(String[] nodes, HashFunction hashFunction) {
		this(hashFunction, 1000, nodes);
	}

	public ConsistentHash(HashFunction hashFunction, int numberOfReplicas, Collection<String> nodes) {
		this.hashFunction = hashFunction;
		this.numberOfReplicas = numberOfReplicas;
		if (nodes != null && nodes.size() > 0)
			for (String node : nodes)
				addNode(node);
	}

	public ConsistentHash(HashFunction hashFunction, int numberOfReplicas, String[] nodes) {
		this.hashFunction = hashFunction;
		this.numberOfReplicas = numberOfReplicas;
		if (nodes != null && nodes.length > 0)
			for (String node : nodes)
				addNode(node);
	}

	public void addNode(String node) {
		for (int i = 0; i < numberOfReplicas; i++) {
			circle.put(hashFunction.hash(node.toString() + i), node);
		}
	}

	public void removeNode(String node) {
		for (int i = 0; i < numberOfReplicas; i++) {
			circle.remove(hashFunction.hash(node.toString() + i));
		}
	}

	public String getNode(String businessId) {
		if (circle.isEmpty())
			return null;
		int hash = hashFunction.hash(businessId);
		if (!circle.containsKey(hash)) {
			SortedMap<Integer, String> tailMap = circle.tailMap(hash);
			hash = tailMap.isEmpty() ? circle.firstKey() : tailMap.firstKey();
		}
		return circle.get(hash);
	}

	public int getNodeSize() {
		return this.circle.size();
	}
}
