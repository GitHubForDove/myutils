package org.easyarch.myutils.algorithm.tree.btree;

import sun.reflect.generics.tree.Tree;

import java.util.Map;

/**
 * @author xingtianyu(code4j) Created on 2018-4-10.
 */
public class AVLTree<E extends Comparable> {

    private static final int THRESHOLD = 2;

    private static final int EMPTY = 0;

    private TreeNode<E> root;

    public void add(E elem){
        if (elem == null){
            return ;
        }
        TreeNode<E> parent = add(this.root,elem);
        if (parent == null){
            System.out.println("current is root,"+elem);
        }else{
            System.out.println("add elem is:"+elem+" and parent is "+parent.elem);
        }
    }


    private TreeNode<E> add(TreeNode<E> currentNode, E elem){
        if (root == null){
            root = new TreeNode<>(null,elem,null,null);
            return null;
        }
        if (elem.compareTo(currentNode.elem) > 0){
            if (currentNode.right == null){
                currentNode.right = new TreeNode(null,elem,null,currentNode);
                rebalance(currentNode,currentNode.right);
                return currentNode;
            }else{
                return add(currentNode.right,elem);
            }
        }else if (elem.compareTo(currentNode.elem) < 0){
            if (currentNode.left == null){
                currentNode.left = new TreeNode(null,elem,null,currentNode);
                rebalance(currentNode,currentNode.left);
                return currentNode;
            }else{
                return add(currentNode.left,elem);
            }
        }
        //等于当前节点插入失败
        return null;
    }

    private void rebalance(TreeNode<E> currentNode,TreeNode<E> newNode){
        if (currentNode == null||newNode == null){
            return;
        }
        int lDeep = deep(currentNode.left);
        int rDeep = deep(currentNode.right);
        int factor = lDeep - rDeep;
        // already balanced
        if (Math.abs(factor) < THRESHOLD){
            rebalance(currentNode.parent,newNode);
            return ;
        }
        //左深右浅，LL/LR
        if (factor >= THRESHOLD){
            if (newNode.elem.compareTo(currentNode.elem) < 0 && newNode.elem.compareTo(currentNode.left.elem) > 0){
                //LR，先左旋再右旋
                leftRotate(currentNode.left);
                rightRotate(currentNode);
            }else if (newNode.elem.compareTo(currentNode.elem) < 0 && newNode.elem.compareTo(currentNode.left.elem) < 0){
                //LL
                rightRotate(currentNode);
            }
            return ;
        }
        if (factor <= -THRESHOLD){
            if (newNode.elem.compareTo(currentNode.elem) > 0 && newNode.elem.compareTo(currentNode.right.elem) < 0){
                //RL，先右旋再左旋
                rightRotate(currentNode.right);
                leftRotate(currentNode);
            }else if (newNode.elem.compareTo(currentNode.elem) > 0 && newNode.elem.compareTo(currentNode.right.elem) > 0){
                //RR
                leftRotate(currentNode);
            }
            return;
        }

    }

    /**
     * 当前节点向左旋转，目标是为了通过移动指针将相应的子树根节点调整到合适位置
     * 1.当前节点是否是根节点，不是，则判断当前节点在它父节点的哪一测，左侧则将当前节点右子树的根节点绑定到父节点的左侧；右侧则将当前节点右子树的根节点绑定到父节点的右侧。
     * 2.是根节点则直接将当前节点右子树根节点作为当前树的根节点，并设置它的父节点为空（因为他已经是顶级根节点）。
     * 3.如果当前节点的右子树还有左孩子，则将左孩子绑定到当前节点右侧，并将这个左孩子的父节点指向当前节点。否则，当前节点右侧为空
     * 3.当前节点指向当前节点右子树根节点的左侧，当前节点的父节点指向当前节点的右子树的根目录
     * @param currentNode
     */
    private void leftRotate(TreeNode<E> currentNode){
        TreeNode<E> cRight = currentNode.right;
        TreeNode<E> cParent = currentNode.parent;
        //父节点指向新的子节点前先判断是绑定到左子树还是右子树
        if (cParent != null){
            //记得判断左边或右边为空的情况，空则认为不是那一侧的
            if (cParent.left != null && currentNode.elem.compareTo(cParent.left.elem) == 0){
                cParent.left = cRight;
            }else if (cParent.right != null && currentNode.elem.compareTo(cParent.right.elem) == 0){
                cParent.right = cRight;
            }
            cRight.parent = cParent;
        }else{
            //当前节点为根节点的情况
            cRight.parent = null;
            this.root = cRight;
        }
        //当前节点包括一个左分支的情况
        if (cRight.left != null){
            currentNode.right = cRight.left;
            cRight.left.parent = currentNode;
        }else{
            currentNode.right = null;
        }
        cRight.left = currentNode;
        currentNode.parent = cRight;
    }

    /**
     * 当前节点向右旋转,同左
     * @param currentNode
     */
    private void rightRotate(TreeNode<E> currentNode){
        TreeNode<E> cLeft = currentNode.left;
        TreeNode<E> cParent = currentNode.parent;
        //当前节点不是root节点,则直接把当前节点的父作为左子树的父，把父节点的左子树指向当前节点左子树.否则左子树作为root节点
        if (cParent != null){
            //记得判断左边或右边为空的情况，空则认为不是那一侧的
            if (cParent.left!= null && currentNode.elem.compareTo(cParent.left.elem) == 0){
                cParent.left = cLeft;
            }else if (cParent.right != null && currentNode.elem.compareTo(cParent.right.elem) == 0){
                cParent.right = cLeft;
            }
            cLeft.parent = cParent;
        }else{
            cLeft.parent = null;
            this.root = cLeft;
        }
        //上升的节点有右子树
        if (cLeft.right != null){
            //上升节点的右子树挂到下沉节点的左侧
            currentNode.left = cLeft.right;
            //上一步挂到下沉节点左侧的节点，其父节点指向下沉节点
            cLeft.right.parent = currentNode;
        }else{
            currentNode.left = null;
        }
        //下沉节点挂到上升节点的右侧
        cLeft.right = currentNode;
        currentNode.parent = cLeft;
    }

    private int deep(TreeNode<E> currentNode){
        if (currentNode == null){
            return EMPTY;
        }
        if (currentNode.right == null && currentNode.left == null){
            return 1;
        }
        return 1+ Math.max(deep(currentNode.left),deep(currentNode.right));
    }

    public E find(E elem){
        if (elem == null){
            return null;
        }
        if (root == null){
            return null;
        }
        TreeNode currentNode = root;
        return find(currentNode,elem);
    }

    private E find(TreeNode currentNode, E elem){
        if (currentNode == null){
            return null;
        }
        if (elem.compareTo(currentNode.elem) == 0){
            return (E) currentNode.elem;
        }
        if (elem.compareTo(currentNode.elem)>0){
            return find(currentNode.right,elem);
        }else if (elem.compareTo(currentNode.elem)<0){
            return find(currentNode.left,elem);
        }else{
            return (E) currentNode.elem;
        }
    }

    public boolean remove(E elem){
        if (elem == null){
            return false;
        }
        if (root == null){
            return false;
        }
        TreeNode currentNode = root;
        boolean removed = remove(currentNode,elem);
        if (removed){
            rebalance(currentNode,currentNode);
        }
        return removed;
    }

    private boolean remove(TreeNode<E> currentNode,E elem){
        if (currentNode == null){
            return false;
        }
        if (elem.compareTo(currentNode.elem)>0){
            return remove(currentNode.right,elem);
        }else if (elem.compareTo(currentNode.elem)<0){
            return remove(currentNode.left,elem);
        }else{
            TreeNode<E> cParent = currentNode.parent;
            //被删除节点是叶子节点
            if (currentNode.left == null && currentNode.right == null){
                if (cParent.left == currentNode){
                    cParent.left = null;
                }else if (cParent.right == currentNode){
                    cParent.right = null;
                }
                currentNode.free();
                return true;
            }
            //被删除节点有左叶子
            if (currentNode.left != null && currentNode.right == null){
                TreeNode<E> cLeft = currentNode.left;
                cLeft.parent = currentNode.parent;
                currentNode.parent.left = cLeft;
                currentNode.free();
                return true;
            }
            //被删除节点有右叶子
            if (currentNode.left == null && currentNode.right != null){
                TreeNode<E> cRight = currentNode.right;
                cRight.parent = currentNode.parent;
                currentNode.parent.right = cRight;
                currentNode.free();
                return true;
            }
            //被删除节点存在左右子树
            TreeNode<E> promotedNode = promoteNode(currentNode,currentNode);
            //被删除的是根
            if (cParent == null){
                promotedNode.parent = null;
            }else{
                promotedNode.parent = currentNode.parent;
            }
            if (currentNode.left != promotedNode){
                promotedNode.left = currentNode.left;
                currentNode.left.parent = promotedNode;
            }
            if (currentNode.right != promotedNode){
                promotedNode.right = currentNode.right;
                currentNode.right.parent = promotedNode;
            }
            if (cParent.left == currentNode){
                cParent.left = promotedNode;
            }else if (cParent.right == currentNode){
                cParent.right = promotedNode;
            }

            currentNode.free();
            return true;
        }
    }

    /**
     * 寻找当前节点的子树中
     * @param currentNode
     * @param deletedNode
     * @return
     */
    private TreeNode<E> promoteNode(TreeNode<E> currentNode,TreeNode<E> deletedNode){
        if (currentNode == null){
            return null;
        }
        if (currentNode == deletedNode){
            TreeNode<E> rightNode = currentNode.right;
            if (rightNode == null){
                return currentNode.left == null?currentNode:currentNode.left;
            }
            return promoteNode(rightNode,deletedNode);
        }
        TreeNode<E> leftNode = currentNode.left;
        if (leftNode == null){
            return currentNode;
        }
        return promoteNode(leftNode,deletedNode);
    }

    public void iterate(){
        iterate(root);
    }

    private void iterate(TreeNode node){
        if (node == null){
            return ;
        }
        System.out.println(node.elem);
        iterate(node.left);
        iterate(node.right);
    }

    public class TreeNode<E extends Comparable>{

        public TreeNode<E> left;

        public TreeNode<E> right;

        public TreeNode<E> parent;

        private E elem;

        public TreeNode(TreeNode left, E elem, TreeNode right, TreeNode parent){
            this.left = left;
            this.elem = elem;
            this.right = right;
            this.parent = parent;
        }

        public void free(){
            this.left = null;
            this.right = null;
            this.parent = null;
            this.elem = null;
        }
    }
}
