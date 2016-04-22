package avltree;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

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

    private <T extends Comparable<T>> boolean nodeIsBalanced(AVLNode<T> node) {
        return node == null || AVLNode.isBalanced(node) && nodeIsBalanced(node.left) && nodeIsBalanced(node.right);
    }

    private <T extends Comparable<T>> boolean treeIsBalanced(AVLTree<T> tree) {
        return nodeIsBalanced(tree.head);
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