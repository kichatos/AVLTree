package avltree;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AVLNodeTest {
    static <T extends Comparable<T>> boolean nodeIsBalanced(AVLNode<T> node) {
        if (node == null) {
            return true;
        }

        boolean nodeBalanced = AVLNode.isBalanced(node);
        return nodeBalanced && nodeIsBalanced(node.left) && nodeIsBalanced(node.right);
    }

    @Test
    public void split() throws Exception {
        AVLTree<Integer> tree = new AVLTree<>();
        Integer min = 0;
        Integer max = 1 << 10;
        for (Integer i = min; i <= max; ++i) {
            tree.add(i);
        }

        int size = tree.size();

        int splitPoint = 22;
        List<AVLNode<Integer>> list = AVLNode.split(tree.head, splitPoint, true);
        AVLNode<Integer> left = list.get(0);
        AVLNode<Integer> right = list.get(1);

        assertEquals(AVLNode.getNodeCount(left), splitPoint + 1);
        assertEquals(AVLNode.getNodeCount(right), size - splitPoint - 1);
        assertTrue(nodeIsBalanced(left));
        assertTrue(nodeIsBalanced(right));
    }
}
