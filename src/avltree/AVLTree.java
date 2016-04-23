package avltree;


import java.util.*;

public class AVLTree<E extends Comparable<E>> extends AbstractCollection<E> implements Collection<E> {
    public int size() {
        return AVLNode.getNodeCount(head);
    }

    AVLNode<E> head;

    public AVLTree() {
        head = null;
    }

    public AVLTree(AVLNode<E> head) {
        this.head = head;
    }

    public AVLTree(Collection<? extends E> c) {
        this.addAll(c);
    }

    public static <E extends Comparable<E>> AVLTree<E> join(AVLTree<E> left, AVLTree<E> right) {
        if (left == null) {
            return right;
        }

        if (right == null) {
            return left;
        }

        return new AVLTree<>(AVLNode.join(left.head, right.head));
    }

    public void retainInterval(E min, E max, boolean minOpen, boolean maxOpen) {
        if (min == null || max == null) {
            throw new NullPointerException();
        }

        if (min.compareTo(max) > 0) {
            removeInterval(max, min, !maxOpen, !minOpen);
            return;
        }

        List<AVLNode<E>> minList = AVLNode.split(head, min, minOpen);
        head = minList.get(1);
        List<AVLNode<E>> maxList = AVLNode.split(head, max, !maxOpen);
        head = maxList.get(0);
    }

    public void retainSegment(E min, E max) {
        this.retainInterval(min, max, false, false);
    }

    public void retainInterval(E min, E max) {
        this.retainInterval(min, max, true, true);
    }

    public void removeInterval(E min, E max, boolean minOpen, boolean maxOpen) {
        if (min == null || max == null) {
            throw new NullPointerException();
        }

        if (min.compareTo(max) > 0) {
            retainInterval(max, min, !maxOpen, !minOpen);
            return;
        }

        List<AVLNode<E>> minList = AVLNode.split(head, min, minOpen);
        head = minList.get(1);
        List<AVLNode<E>> maxList = AVLNode.split(head, max, !maxOpen);
        head = AVLNode.join(minList.get(0), maxList.get(1));
    }

    public void removeSegment(E min, E max) {
        this.removeInterval(min, max, false, false);
    }

    public void removeInterval(E min, E max) {
        this.removeInterval(min, max, true, true);
    }

    @Override
    public Iterator<E> iterator() {
        return new Itr();
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) {
            return false;
        }

        try {
            @SuppressWarnings("unchecked")
            E e = (E) o;
            return AVLNode.find(head, e) != null;
        }
        catch (ClassCastException e) {
            return false;
        }
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) {
            return false;
        }

        try {
            @SuppressWarnings("unchecked")
            E e = (E) o;
            int prevSize = this.size();
            head = AVLNode.remove(head, e);
            return prevSize != this.size();
        }
        catch (ClassCastException e) {
            return false;
        }
    }

    @Override
    public boolean add(E e) {
        int prevSize = this.size();
        head = AVLNode.insert(head, e);
        return prevSize != this.size();
    }

    public E get(int index) {
        if (index < 0 || index >= this.size()) {
            throw new IndexOutOfBoundsException();
        }

        return AVLNode.get(head, index);
    }

    private class Itr implements Iterator<E> {
        AVLNode<E> start = AVLNode.findMin(head);
        AVLNode<E> next = start;

        int startingSize = AVLTree.this.size();
        int moveCount;

        boolean moved;

        @Override
        public boolean hasNext() {
            return moveCount < startingSize;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            next = next.next;
            ++moveCount;
            moved = true;
            return next.prev.value;
        }

        @Override
        public void remove() {
            if (!moved) {
                throw new IllegalStateException();
            }

            if (next != null) {
                head = AVLNode.remove(head, next.prev.value);
            }
            else {
                head = null;
            }

            moved = false;
        }
    }
}
