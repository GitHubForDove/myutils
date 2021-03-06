package org.easyarch.myutils.algorithm.sort.tree;

import org.easyarch.myutils.algorithm.struct.tree.btree.AVLTree;

/**
 * Created by xingtianyu on 2018/3/27.
 */
public class DoubleTreeeTest {

    public static void main(String[] args) {
        AVLTree<User> tree = new AVLTree();
//        tree.add(new User("aaa",1));
//        tree.add(new User("bbb",2));
//        tree.add(new User("ccc",3));
//        tree.add(new User("ddd",4));
//        tree.add(new User("eee",5));
//        tree.add(new User("fff",6));
//        tree.add(new User("ggg",7));
//        tree.add(new User("hhh",8));
//        tree.add(new User("hhh",9));
//        tree.add(new User("hhh",10));

        tree.add(new User("hhh",10));
        tree.add(new User("hhh",9));
        tree.add(new User("hhh",8));
        tree.add(new User("ggg",7));
        tree.add(new User("fff",6));
        tree.add(new User("eee",5));
        tree.add(new User("ddd",4));
        tree.add(new User("ccc",3));
        tree.add(new User("bbb",2));
        tree.add(new User("aaa",1));

        tree.add(new User("hhh",65));
        tree.add(new User("hhh",93));
        tree.add(new User("hhh",82));
        tree.add(new User("ggg",15));
        tree.add(new User("fff",55));
        tree.add(new User("eee",16));
        tree.add(new User("ddd",22));
        tree.add(new User("ccc",104));
        tree.add(new User("bbb",72));
        tree.add(new User("aaa",32));
        System.out.println("remove:"+tree.remove(new User("sss",104)));
        System.out.println("remove:"+tree.remove(new User("sss",15)));
//        tree.add(new User("xxx",5));
//        tree.add(new User("xxx",3));
//        tree.add(new User("xxx",9));
//        tree.add(new User("xxx",1));
//        tree.add(new User("xxx",4));
//        tree.add(new User("xxx",10));
//        tree.remove(new User("xxx",5));
        tree.iterate();
//        tree.iterate();

    }
}
