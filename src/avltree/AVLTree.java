package avltree;

public class AVLTree<T extends Comparable<T>> {
    private static class AVLNode<T extends Comparable<T>> {
        private T value;
        private int childrenCount;
        private int height;
        private AVLNode<T> left;
        private AVLNode<T> right;

        public AVLNode(T value) {
            this.value = value;
            height = 1;
            childrenCount = 0;
            left = null;
            right = null;
        }

        private static <T extends Comparable<T>> int getHeight(AVLNode<T> node) {
            return node == null ? 0 : node.height;
        }

        private static <T extends Comparable<T>> int getBalanceFactor(AVLNode<T> node) {
            return node == null ? 0 : getHeight(node.right) - getHeight(node.left);
        }

        public static <T extends Comparable<T>> int getNodeCount(AVLNode<T> node) {
            return node == null ? 0 : node.childrenCount + 1;
        }

        private static <T extends Comparable<T>> void update(AVLNode<T> node) {
            node.height = Math.max(getHeight(node.left), getHeight(node.right)) + 1;
            node.childrenCount = getNodeCount(node.left) + getNodeCount(node.right);
        }

        private static <T extends Comparable<T>> AVLNode<T> rotateRight(AVLNode<T> p) {
            AVLNode<T> q = p.left;
            p.left = q.right;
            q.right = p;
            update(p);
            update(q);
            return q;
        }

        private static <T extends Comparable<T>> AVLNode<T> rotateLeft(AVLNode<T> q) {
            AVLNode<T> p = q.right;
            q.right = p.left;
            p.left = q;
            update(q);
            update(p);
            return p;
        }

        private static <T extends Comparable<T>> AVLNode<T> balance(AVLNode<T> p) {
            update(p);

            if (getBalanceFactor(p) == 2) {
                if (getBalanceFactor(p.right) < 0) {
                    p.right = rotateRight(p.right);
                }

                return rotateLeft(p);
            }

            if (getBalanceFactor(p) == -2) {
                if (getBalanceFactor(p.left) > 0) {
                    p.left = rotateLeft(p.left);
                }

                return rotateRight(p);
            }

            return p;
        }

        private static <T extends Comparable<T>> AVLNode<T> insert(AVLNode<T> p, T value) {
            if (p == null) {
                return new AVLNode<T>(value);
            }

            if (value.compareTo(p.value) < 0) {
                p.left = insert(p.left, value);
            } else {
                p.right = insert(p.right, value);
            }

            return balance(p);
        }

        private static <T extends Comparable<T>> AVLNode<T> findMin(AVLNode<T> p) {
            return p.left == null ? p : findMin(p.left);
        }

        private static <T extends Comparable<T>> AVLNode<T> removeMin(AVLNode<T> p) {
            if (p.left == null) {
                return p.right;
            }

            p.left = removeMin(p.left);
            return balance(p);
        }

        private static <T extends Comparable<T>> AVLNode<T> remove(AVLNode<T> p, T value) {
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
                    return q;
                }

                AVLNode<T> min = findMin(r);
                min.right = removeMin(r);
                min.left = q;
                return balance(min);
            }

            return balance(p);
        }

        private static <T extends Comparable<T>> T kthElement(AVLNode<T> node, int k) {
            if (k <= 0 || k > getNodeCount(node)) {
                return null;
            }

            if (k == getNodeCount(node.left) + 1) {
                return node.value;
            } else {
                return k < getNodeCount(node.left) + 1 ? kthElement(node.left, k) :
                        kthElement(node.right, k - getNodeCount(node.left) - 1);
            }
        }
    }

    private AVLNode<T> head;

    public AVLTree() {
        head = null;
    }

    public void insert(T value) {
        if (head == null) {
            head = new AVLNode<T>(value);
        } else {
            head = AVLNode.insert(head, value);
        }
    }

    public void remove(T value) {
        if (head != null) {
            head = AVLNode.remove(head, value);
        }
    }

    public T kthElement(int k) {
        return AVLNode.kthElement(head, k);
    }
}
