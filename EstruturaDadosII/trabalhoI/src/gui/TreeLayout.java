package gui;

import java.awt.Point;
import java.util.*;

import arvore.Node;

public class TreeLayout {

  public static DrawTree layout(Node root) {
    if (root == null)
      return null;

    DrawTree tree = new DrawTree(root, 0);
    setup(tree);
    addMods(tree, 0);
    return tree;
  }

  private static void setup(DrawTree tree) {
    Map<Integer, Integer> nexts = new HashMap<>();
    Map<Integer, Integer> offset = new HashMap<>();
    setup(tree, 0, nexts, offset);
  }

  private static void setup(DrawTree tree, int depth, Map<Integer, Integer> nexts, Map<Integer, Integer> offset) {
    // Initialize maps for this depth if needed
    if (!nexts.containsKey(depth))
      nexts.put(depth, 0);
    if (!offset.containsKey(depth))
      offset.put(depth, 0);

    // Recursively setup children
    for (DrawTree child : tree.children) {
      setup(child, depth + 1, nexts, offset);
    }

    tree.y = depth;

    if (tree.children.isEmpty()) {
      // Leaf node
      int place = nexts.get(depth);
      tree.x = place;
    } else if (tree.children.size() == 1) {
      // Single child
      tree.x = tree.children.get(0).x;
    } else {
      // Multiple children - center between extremes
      int leftMost = tree.children.get(0).x;
      int rightMost = tree.children.get(tree.children.size() - 1).x;
      tree.x = (leftMost + rightMost) / 2;
    }

    // Update offset
    int currentNext = nexts.get(depth);
    int currentOffset = offset.get(depth);
    int newOffset = Math.max(currentOffset, currentNext - tree.x);
    offset.put(depth, newOffset);

    // Apply offset
    if (!tree.children.isEmpty()) {
      tree.x += newOffset;
    }

    // Update next position for this depth
    nexts.put(depth, currentNext + 2);
    tree.mod = newOffset;
  }

  private static void addMods(DrawTree tree, int modSum) {
    tree.x += modSum;
    modSum += tree.mod;

    for (DrawTree child : tree.children) {
      addMods(child, modSum);
    }
  }

  // Helper method to convert DrawTree to coordinates map
  public static Map<Node, Point> getCoordinates(DrawTree drawTree) {
    Map<Node, Point> coords = new HashMap<>();
    collectCoordinates(drawTree, coords);
    return coords;
  }

  private static void collectCoordinates(DrawTree drawTree, Map<Node, Point> coords) {
    if (drawTree != null) {
      coords.put(drawTree.tree, new Point(drawTree.x, drawTree.y));
      for (DrawTree child : drawTree.children) {
        collectCoordinates(child, coords);
      }
    }
  }
}