package avltree;

import java.util.ArrayList;
import java.util.List;

public class AVLNode<T extends Comparable<T>> {
    public static final int MIN_BALANCE_FACTOR = -1;
    public static final int MAX_BALANCE_FACTOR = 1;

    public T value;
    private int childrenCount;
    private int height;

    public AVLNode<T> left;
    public AVLNode<T> right;

    public AVLNode<T> parent;
    public AVLNode<T> next;
    public AVLNode<T> prev;

    public AVLNode(T value) {
        this(value, null, null);
    }

    public AVLNode(T value, AVLNode<T> left, AVLNode<T> right) {
        this(value, left, right, null, null);
        AVLNode.setPrev(this, this);
    }

    public AVLNode(T value, AVLNode<T> left, AVLNode<T> right, AVLNode<T> prev, AVLNode<T> next) {
        this.value = value;
        AVLNode.setLeft(this, left);
        AVLNode.setRight(this, right);
        AVLNode.update(this);
        AVLNode.setPrev(this, prev);
        AVLNode.setNext(this, next);
    }

    public static <T extends Comparable<T>> void setLeft(AVLNode<T> node, AVLNode<T> left) {
        if (node != null) {
            node.left = left;
            if (left != null) {
                left.parent = node;
            }
        }
    }

    public static <T extends Comparable<T>> void setRight(AVLNode<T> node, AVLNode<T> right) {
        if (node != null) {
            node.right = right;
            if (right != null) {
                right.parent = node;
            }
        }
    }

    public static <T extends Comparable<T>> void setPrev(AVLNode<T> node, AVLNode<T> prev) {
        if (node != null) {
            node.prev = prev;
            if (prev != null) {
                prev.next = node;
            }
        }
    }

    public static <T extends Comparable<T>> void setNext(AVLNode<T> node, AVLNode<T> next) {
        if (node != null) {
            node.next = next;
            if (next != null) {
                next.prev = node;
            }
        }
    }

    public static <T extends Comparable<T>> boolean isLeftSon(AVLNode<T> parent, AVLNode<T> son) {
        return parent != null && son == parent.left;
    }

    public static <T extends Comparable<T>> boolean isRightSon(AVLNode<T> parent, AVLNode<T> son) {
        return parent != null && son == parent.right;
    }

    public static <T extends Comparable<T>> void replaceSon(AVLNode<T> parent, AVLNode<T> son, AVLNode<T> repl) {
        if (son == null) {
            return;
        }

        if (parent == null) {
            repl.parent = null;
        }

        if (AVLNode.isLeftSon(parent, son)) {
            AVLNode.setLeft(parent, repl);
        }

        if (AVLNode.isRightSon(parent, son)) {
            AVLNode.setRight(parent, repl);
        }
    }

    public static <T extends Comparable<T>> int getHeight(AVLNode<T> node) {
        return node == null ? 0 : node.height;
    }

    public static <T extends Comparable<T>> int getBalanceFactor(AVLNode<T> node) {
        return node == null ? 0 : getHeight(node.right) - getHeight(node.left);
    }

    public static <T extends Comparable<T>> boolean isBalanced(AVLNode<T> node) {
        int balanceFactor = AVLNode.getBalanceFactor(node);
        return MIN_BALANCE_FACTOR <= balanceFactor && balanceFactor <= MAX_BALANCE_FACTOR;
    }

    public static <T extends Comparable<T>> int getNodeCount(AVLNode<T> node) {
        return node == null ? 0 : node.childrenCount + 1;
    }

    private static <T extends Comparable<T>> void update(AVLNode<T> node) {
        if (node == null) {
            return;
        }

        node.height = Math.max(getHeight(node.left), getHeight(node.right)) + 1;
        node.childrenCount = getNodeCount(node.left) + getNodeCount(node.right);
    }

    private static <T extends Comparable<T>> AVLNode<T> rotateRight(AVLNode<T> p) {
        AVLNode<T> q = p.left;
        AVLNode.setLeft(p, q.right);
        AVLNode.setRight(q, p);

        update(p);
        update(q);
        return q;
    }

    private static <T extends Comparable<T>> AVLNode<T> rotateLeft(AVLNode<T> q) {
        AVLNode<T> p = q.right;
        AVLNode.setRight(q, p.left);
        AVLNode.setLeft(p, q);

        update(q);
        update(p);
        return p;
    }

    private static <T extends Comparable<T>> AVLNode<T> balance(AVLNode<T> p) {
        update(p);

        if (getBalanceFactor(p) > MAX_BALANCE_FACTOR) {
            if (getBalanceFactor(p.right) < 0) {
                AVLNode.setRight(p, rotateRight(p.right));
            }

            return rotateLeft(p);
        }

        if (getBalanceFactor(p) < MIN_BALANCE_FACTOR) {
            if (getBalanceFactor(p.left) > 0) {
                AVLNode.setLeft(p, rotateLeft(p.left));
            }

            return rotateRight(p);
        }

        return p;
    }

    public static <T extends Comparable<T>> AVLNode<T> insert(AVLNode<T> p, T value) {
        if (p == null) {
            return new AVLNode<>(value);
        }

        if (value.compareTo(p.value) < 0) {
            if (p.left != null) {
                p.left = insert(p.left, value);
            }
            else {
                AVLNode.setLeft(p, new AVLNode<>(value, null, null, p.prev, p));
            }
        } else {
            if (p.right != null) {
                p.right = insert(p.right, value);
            }
            else {
                AVLNode.setRight(p, new AVLNode<>(value, null, null, p, p.next));
            }
        }

        return balance(p);
    }

    public static <T extends Comparable<T>> AVLNode<T> findMin(AVLNode<T> p) {
        return p == null || p.left == null ? p : findMin(p.left);
    }

    public static <T extends Comparable<T>> AVLNode<T> findMax(AVLNode<T> p) {
        return p == null || p.right == null ? p : findMax(p.right);
    }

    private static <T extends Comparable<T>> AVLNode<T> removeMin(AVLNode<T> p) {
        if (p == null) {
            return null;
        }

        if (p.left == null) {
            return p.right;
        }

        p.left = removeMin(p.left);
        return balance(p);
    }

    public static <T extends Comparable<T>> AVLNode<T> find(AVLNode<T> p, T value) {
        if (p == null) {
            return null;
        }

        int cmp = value.compareTo(p.value);
        if (cmp < 0) {
            return find(p.left, value);
        }
        else if (cmp > 0) {
            return find(p.right, value);
        }

        return p;
    }

    public static <T extends Comparable<T>> AVLNode<T> remove(AVLNode<T> p, T value) {
        if (p == null) {
            return null;
        }

        if (value.compareTo(p.value) < 0) {
            p.left = remove(p.left, value);
        } else if (value.compareTo(p.value) > 0) {
            p.right = remove(p.right, value);
        } else {
            AVLNode<T> q = p.left;
            AVLNode<T> r = p.right;

            if (r == null) {
                replaceSon(p.parent, p, q);
                AVLNode.setNext(p.prev, p.next);
                return q;
            }

            AVLNode<T> min = findMin(r);
            replaceSon(p.parent, p, min);
            AVLNode.setNext(p.prev, p.next);
            AVLNode.setRight(min, removeMin(r));
            AVLNode.setLeft(min, q);
            return balance(min);
        }

        return balance(p);
    }

    public static <T extends Comparable<T>> AVLNode<T> join(AVLNode<T> left, AVLNode<T> right) {
        if (left == null) {
            return right;
        }

        if (right == null) {
            return left;
        }

        AVLNode<T> head = findMin(right);
        setNext(findMax(left), head);
        setNext(findMax(right), findMin(left));
        setRight(head, removeMin(right));
        setLeft(head, left);
        head.parent = null;

        return balanceBranch(head);
    }

    public static <T extends Comparable<T>> List<AVLNode<T>> split(AVLNode<T> head, T value, boolean valueLeft) {
        if (head == null) {
            List<AVLNode<T>> res = new ArrayList<>();
            res.add(null);
            res.add(null);
            return res;
        }

        AVLNode<T> min = findMin(head);
        AVLNode<T> max = findMax(head);
        AVLNode<T> left = null;
        AVLNode<T> right = null;
        AVLNode<T> node = head;
        while (node != null) {
            int cmp = value.compareTo(node.value);
            if (cmp < 0 || cmp == 0 && !valueLeft) {
                if (right == null) {
                    node.parent = null;
                }
                else {
                    setLeft(right, node);
                }

                right = node;
                node = node.left;
            }
            else {
                if (left == null) {
                    node.parent = null;
                }
                else {
                    setRight(left, node);
                }

                left = node;
                node = node.right;
            }
        }

        setNext(left, min);
        setNext(max, right);
        setRight(left, null);
        setLeft(right, null);
        List<AVLNode<T>> res = new ArrayList<>();
        res.add(balanceBranch(left));
        res.add(balanceBranch(right));
        return res;
    }

    public static <T extends Comparable<T>> AVLNode<T> balanceBranch(AVLNode<T> node) {
        AVLNode<T> last = node;
        while (node != null) {
            update(node);
            while (!isBalanced(node)) {
                replaceSon(node.parent, node, balance(node));
            }

            last = node;
            node = node.parent;
        }

        update(last);
        return last;
    }

    public static <T extends Comparable<T>> T get(AVLNode<T> node, int index) {
        if (index < 0 || index >= getNodeCount(node)) {
            throw new IndexOutOfBoundsException();
        }

        if (index == getNodeCount(node.left)) {
            return node.value;
        } else {
            return index < getNodeCount(node.left) ? get(node.left, index) :
                    get(node.right, index - getNodeCount(node.left) - 1);
        }
    }

    @Override
    public String toString() {
        return "Node: " + value;
    }
}
