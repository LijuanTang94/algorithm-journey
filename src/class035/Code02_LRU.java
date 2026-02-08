package class035;

import java.util.HashMap;

// 实现LRU结构

	// 测试链接 : https://leetcode.cn/problems/lru-cache/
	# LRU Cache (O(1)) — Final Implementation & Notes

## Solution Idea

The LRU Cache maintains **access order**, not insertion order.

Core design:
- Use a `HashMap` for O(1) key → node lookup
- Use a **doubly linked list** to maintain recent usage order

Key rules:
- The **most recently used** node is always at the tail
- The **least recently used** node is always at the head
- Any access (`get` or existing `put`) is handled as:
  > `remove(node) → addLast(node)`
- New nodes are **always added at the tail**
- Eviction always removes `head.next`

Dummy head and tail nodes are used to eliminate edge cases.

All operations run in **O(1)** time.

---

## Final Correct Code

```java
class LRUCache {

    class Node {
        int key, val;
        Node pre, next;
        Node(int k, int v) {
            key = k;
            val = v;
        }
    }

    Node head, tail;
    int cap;
    HashMap<Integer, Node> map;

    public LRUCache(int capacity) {
        cap = capacity;
        map = new HashMap<>();
        head = new Node(-1, -1);
        tail = new Node(-1, -1);
        head.next = tail;
        tail.pre = head;
    }

    private void remove(Node node) {
        node.pre.next = node.next;
        node.next.pre = node.pre;
    }

    private void addLast(Node node) {
        Node last = tail.pre;
        last.next = node;
        node.pre = last;
        node.next = tail;
        tail.pre = node;
    }

    public int get(int key) {
        if (!map.containsKey(key)) return -1;
        Node node = map.get(key);
        remove(node);
        addLast(node);
        return node.val;
    }

    public void put(int key, int value) {
        if (cap == 0) return;

        if (map.containsKey(key)) {
            Node node = map.get(key);
            node.val = value;
            remove(node);
            addLast(node);
        } else {
            if (map.size() == cap) {
                Node lru = head.next;
                remove(lru);
                map.remove(lru.key);
            }
            Node node = new Node(key, value);
            addLast(node);
            map.put(key, node);
        }
    }
}



Common Mistakes & Key Takeaways
❌ Mistake 1: Node does not store key

Without key, evicted nodes cannot be removed from HashMap

Leads to stale entries and incorrect cache state

Fix:
Always store both key and value in the node.

❌ Mistake 2: Treating head / middle / tail differently

Overcomplicates logic

Causes pointer bugs

Fix:
Use a unified rule:

remove(node)
addLast(node)

❌ Mistake 3: Removing node before saving reference
remove(head.next);
map.remove(head.next.key); // WRONG


Fix:

Node lru = head.next;
remove(lru);
map.remove(lru.key);

❌ Mistake 4: Incorrect tail insertion order

Not preserving the old tail causes broken links

Correct pattern:

Node last = tail.pre;
last.next = node;
node.pre = last;
node.next = tail;
tail.pre = node;