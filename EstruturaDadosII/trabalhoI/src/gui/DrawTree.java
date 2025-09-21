// package gui;

// import arvore.Node;
// import java.util.ArrayList;
// import java.util.List;

// public class DrawTree {
//   public int x = -1;
//   public int y;
//   public int mod = 0;
//   public Node tree;
//   public List<DrawTree> children = new ArrayList<>();
//   public DrawTree parent; // For back-references

//   public DrawTree(Node tree, int depth) {
//     this.tree = tree;
//     this.y = depth;

//     // Create children recursively
//     if (tree.filhos != null) {
//       for (Node child : tree.filhos) {
//         DrawTree childTree = new DrawTree(child, depth + 1);
//         childTree.parent = this;
//         children.add(childTree);
//       }
//     }
//   }
// }