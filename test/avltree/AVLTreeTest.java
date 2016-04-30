package avltree;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;

public class AVLTreeTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private AVLTree<Integer> createSimpleTree() {
        AVLTree<Integer> res = new AVLTree<>();
        res.add(1);
        res.add(2);
        res.add(3);

        return res;
    }

    static <T extends Comparable<T>> boolean treeIsBalanced(AVLTree<T> tree) {
        return AVLNodeTest.nodeIsBalanced(tree.head);
    }

    @Test
    public void balanceTestAscDifferentElements() throws Exception {
        Integer min = 0;
        Integer max = 1 << 10;

        AVLTree<Integer> tree = new AVLTree<>();
        for (Integer i = min; i < max; ++i) {
            tree.add(i);
        }

        assertTrue(treeIsBalanced(tree));
    }

    @Test
    public void balanceTestDescDifferentElements() throws Exception {
        Integer min = 0;
        Integer max = 1 << 10;

        AVLTree<Integer> tree = new AVLTree<>();
        for (Integer i = max; i > min; --i) {
            tree.add(i);
        }

        assertTrue(treeIsBalanced(tree));
    }

    @Test
    public void balanceTestSameElement() throws Exception {
        Integer x = 3;
        int count = 1 << 10;

        AVLTree<Integer> tree = new AVLTree<>();
        for (int i = 0; i < count; ++i) {
            tree.add(x);
        }

        assertTrue(treeIsBalanced(tree));
    }

    @Test
    public void add() throws Exception {
        AVLTree<Integer> tree = createSimpleTree();

        assertTrue(tree.contains(1));
        assertTrue(tree.contains(2));
        assertTrue(tree.contains(3));
        assertEquals(tree.size(), 3);
    }

    @Test
    public void remove() throws Exception {
        AVLTree<Integer> tree = createSimpleTree();

        assertFalse(tree.remove("A"));
        assertFalse(tree.remove(null));
        for (int i = 1; i <= 3; ++i) {
            assertTrue(tree.contains(i));
            assertTrue(tree.remove(i));
            assertFalse(tree.contains(i));
            assertFalse(tree.remove(i));
        }

    }

    @Test
    public void get() throws Exception {
        AVLTree<Integer> tree = createSimpleTree();

        assertEquals(tree.get(0), Integer.valueOf(1));
        assertEquals(tree.get(1), Integer.valueOf(2));
        assertEquals(tree.get(2), Integer.valueOf(3));
    }

    @Test
    public void contains() throws Exception {
        AVLTree<Integer> tree = createSimpleTree();

        assertFalse(tree.contains(null));
        assertFalse(tree.contains("1"));
        assertFalse(tree.contains(this));
    }

    @Test
    public void iteratorNext() throws Exception {
        Integer[] data = {9, 2, 3, 3, 15, 1, 6, 8, 10, 4};
        AVLTree<Integer> tree = new AVLTree<>(Arrays.asList(data));
        Arrays.sort(data);
        Iterator<Integer> iterator = tree.iterator();

        for (Integer aData : data) {
            assertEquals(iterator.next(), aData);
        }

        assertFalse(iterator.hasNext());
    }

    @Test
    public void iteratorRemove() throws Exception {
        AVLTree<Integer> tree = createSimpleTree();
        Iterator<Integer> iterator = tree.iterator();

        for (int i = 1; i <= 3; ++i) {
            iterator.next();
            iterator.remove();
            assertFalse(tree.contains(i));
        }

        assertFalse(iterator.hasNext());
    }

    @Test
    public void iteratorEmpty() throws Exception {
        AVLTree<Integer> tree = new AVLTree<>();
        Iterator<Integer> iterator = tree.iterator();
        thrown.expect(NoSuchElementException.class);
        iterator.next();
    }

    @Test
    public void getOutOfBoundsLower() throws Exception {
        AVLTree<Integer> tree = createSimpleTree();

        thrown.expect(IndexOutOfBoundsException.class);
        tree.get(-2);
    }

    @Test
    public void joinBalance() throws Exception {
        Integer[] leftArray = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};
        AVLTree<Integer> left = new AVLTree<>(Arrays.asList(leftArray));

        Integer[] rightArray = {17, 18, 19};
        AVLTree<Integer> right = new AVLTree<>(Arrays.asList(rightArray));

        AVLTree<Integer> joined = AVLTree.join(left, right);
        assertEquals(joined.size(), leftArray.length + rightArray.length);
        assertTrue(treeIsBalanced(joined));

        Integer i = 0;
        for (Integer elem : joined) {
            assertEquals(++i, elem);
        }
    }

    @Test
    public void joinSimple() throws Exception {
        Integer[] points = {0, 1, 23, 32, 44};
        ArrayList<AVLTree<Integer>> trees = new ArrayList<>();
        for (int i = 0; i < 5; ++i) {
            trees.add(new AVLTree<>(new AVLNode<Integer>(points[i])));
        }

        trees.set(0, AVLTree.join(trees.get(0), trees.get(1)));
        assertTrue(treeIsBalanced(trees.get(0)));
        trees.set(2, AVLTree.join(trees.get(2), trees.get(3)));
        assertTrue(treeIsBalanced(trees.get(2)));
        trees.set(2, AVLTree.join(trees.get(2), trees.get(4)));
        assertTrue(treeIsBalanced(trees.get(2)));
        trees.set(0, AVLTree.join(trees.get(0), trees.get(2)));
        assertTrue(treeIsBalanced(trees.get(0)));

        int i = 0;
        for (Integer elem : trees.get(0)) {
            assertEquals(elem, points[i++]);
        }
    }

    @Test
    public void retainInterval() throws Exception {
        Integer min = 0;
        Integer max = 1 << 10;

        AVLTree<Integer> tree = new AVLTree<>();
        for (Integer i = min; i <= max; ++i) {
            tree.add(i);
        }

        min = 22;
        max = 513;
        tree.retainSegment(min, max);
        assertTrue(treeIsBalanced(tree));
        assertEquals(tree.size(), max - min + 1);

        Integer i = min;
        for (Integer elem : tree) {
            assertEquals(i++, elem);
        }
    }

    @Test
    public void retainOverlappingInterval() throws Exception {
        Integer min = 0;
        Integer max = 1 << 10;

        AVLTree<Integer> tree = new AVLTree<>();
        for (Integer i = min; i <= max; ++i) {
            tree.add(i);
        }

        Integer size = tree.size();

        Integer l = 22;
        Integer r = 513;
        tree.retainSegment(r, l);
        assertTrue(treeIsBalanced(tree));
        assertEquals(tree.size(), size - (r - l - 1));

        Iterator<Integer> iterator = tree.iterator();
        for (Integer i = r; i <= max; ++i) {
            assertEquals(i, iterator.next());
        }

        for (Integer i = min; i <= l; ++i) {
            assertEquals(i, iterator.next());
        }

        assertFalse(iterator.hasNext());
    }

    @Test
    public void retainSingle() throws Exception {
        Integer elem = 1;

        AVLTree<Integer> tree = new AVLTree<>();
        tree.add(elem);
        tree.retainSegment(elem, elem);
        assertEquals(tree.size(), 1);
        assertEquals(tree.get(0), elem);
        for (Integer e : tree) {
            assertEquals(e, elem);
        }
    }

    @Test
    public void retainNotPresent() throws Exception {
        AVLTree<Integer> tree = createSimpleTree();
        tree.retainSegment(-5, 5);
        assertEquals(tree.size(), 3);

        Integer i = 1;
        for (Integer e : tree) {
            assertEquals(e, i++);
        }

        tree.retainSegment(-30, 2);
        assertEquals(tree.size(), 2);
        i = 1;
        for (Integer e : tree) {
            assertEquals(e, i++);
        }

        tree.retainSegment(2, 100);
        assertEquals(tree.size(), 1);
        i = 2;
        for (Integer e : tree) {
            assertEquals(e, i++);
        }
    }

    @Test
    public void retainEmpty() throws Exception {
        AVLTree<Integer> tree = new AVLTree<>();
        tree.retainSegment(-9, 13);
        assertTrue(tree.isEmpty());
    }

    @Test
    public void removeOverlappingInterval() throws Exception {
        Integer min = 0;
        Integer max = 1 << 10;

        AVLTree<Integer> tree = new AVLTree<>();
        for (Integer i = min; i <= max; ++i) {
            tree.add(i);
        }

        min = 22;
        max = 513;
        tree.removeInterval(max, min);
        assertTrue(treeIsBalanced(tree));
        assertEquals(tree.size(), max - min + 1);

        Integer i = min;
        for (Integer elem : tree) {
            assertEquals(i++, elem);
        }
    }

    @Test
    public void removeInterval() throws Exception {
        Integer min = 0;
        Integer max = 1 << 10;

        AVLTree<Integer> tree = new AVLTree<>();
        for (Integer i = min; i <= max; ++i) {
            tree.add(i);
        }

        Integer size = tree.size();

        Integer l = 22;
        Integer r = 513;
        tree.removeInterval(l, r);
        assertTrue(treeIsBalanced(tree));
        assertEquals(tree.size(), size - (r - l - 1));

        Iterator<Integer> iterator = tree.iterator();
        for (Integer i = min; i <= l; ++i) {
            assertEquals(i, iterator.next());
        }

        for (Integer i = r; i <= max; ++i) {
            assertEquals(i, iterator.next());
        }

        assertFalse(iterator.hasNext());
    }

    @Test
    public void getOutOfBoundsHigher() throws Exception {
        AVLTree<Integer> tree = createSimpleTree();

        thrown.expect(IndexOutOfBoundsException.class);
        tree.get(20);
    }

    @Test
    public void getOutOfBoundsEmpty() throws Exception {
        AVLTree<Integer> tree = new AVLTree<>();

        thrown.expect(IndexOutOfBoundsException.class);
        tree.get(0);
    }
}