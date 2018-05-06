/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package knn;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author vojha
 */
public class MajorityVoteInt {

    public static void main(String[] args) {
        int[] array = {0, 1, 3, 4, 2, 1, 0, 0, 0, 10, 10, 0, 0};
        int[] array1 = {0, 1, 3, 4, 2, 1, 0, 0, 0, 0, 0};
        System.out.println(majority(array));
        System.out.println(majority(array1));
    }

    public static Integer majority(int[] array) {
        Map<Integer, Integer> count = new HashMap<Integer, Integer>();
        for (int number : array) {
            if (count.containsKey(number)) {
                count.put(number, count.get(number) + 1);
            } else {
                count.put(number, 1);
            }
        }
        Integer majority = null;
        Integer majorityCount = null;
        for (Integer key : count.keySet()) {
            if (count.get(key) > array.length / 2) {
                majority = key;
                majorityCount = count.get(key);
            }
        }
        return majority;
    }
}
