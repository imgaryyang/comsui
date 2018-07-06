package com.suidifu.mq.hash;

import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import com.suidifu.mq.hash.hashfunction.Md5HashFunction;

public class ConsistentHash {
	private final HashFunction hashFunction;
	private final int numberOfReplicas;
	private final SortedMap<Integer, RabbitTemplate> circle = new TreeMap<Integer, RabbitTemplate>();

	public ConsistentHash() {
		this(null);
	}

	public ConsistentHash(Collection<RabbitTemplate> nodes) {
		this(nodes, new Md5HashFunction());
	}

	public ConsistentHash(Collection<RabbitTemplate> nodes, HashFunction hashFunction) {
		this(hashFunction, 1000, nodes);
	}

	public ConsistentHash(HashFunction hashFunction, int numberOfReplicas, Collection<RabbitTemplate> nodes) {
		this.hashFunction = hashFunction;
		this.numberOfReplicas = numberOfReplicas;
		if (nodes != null && nodes.size() > 0)
			for (RabbitTemplate node : nodes)
				addNode(node);
	}

	public void addNode(RabbitTemplate node) {
		for (int i = 0; i < this.numberOfReplicas; i++)
			this.circle.put(this.hashFunction.hash(node.getExchange() + "_" + i), node);
	}

	public void removeNode(RabbitTemplate node) {
		for (int i = 0; i < this.numberOfReplicas; i++)
			this.circle.remove(this.hashFunction.hash(node.getExchange() + "_" + i));
	}

	public RabbitTemplate getNode(String businessId) {
		if (this.circle.isEmpty())
			return null;
		int hash = this.hashFunction.hash(businessId);
		if (!this.circle.containsKey(hash)) {
			SortedMap<Integer, RabbitTemplate> tailMap = this.circle.tailMap(hash);
			hash = tailMap.isEmpty() ? this.circle.firstKey() : tailMap.firstKey();
		}
		return this.circle.get(hash);
	}

	public int getNodeSize() {
		return this.circle.size();
	}

	public Collection<RabbitTemplate> getAll() {
		return this.circle.values();
	}
}
