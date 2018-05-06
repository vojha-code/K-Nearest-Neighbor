/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package knn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author vojha
 */
public class MajorityVoteString {

    public static void main(String args[]) {

        ArrayList<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("a");
        list.add("b");
        MajorityVoteString mjs = new MajorityVoteString();
        System.out.println("Most common string: " + winnerClass(list));
    }

    public static String winnerClass(ArrayList<String> list) {
        String common = "";
        Map<String, Integer> stringsCount = new HashMap<>();
        for (String s : list) {
            //System.out.println(s);
            Integer c = stringsCount.get(s);
            if (c == null) {
                c = new Integer(0);
            }
            c++;
            stringsCount.put(s, c);
        }

        Map.Entry<String, Integer> mostRepeated = null;
        for (Map.Entry<String, Integer> e : stringsCount.entrySet()) {
            if (mostRepeated == null || mostRepeated.getValue() < e.getValue()) {
                mostRepeated = e;
            }
        }
        if (mostRepeated != null) {
            //System.out.println("Majority vote to: " + mostRepeated.getKey());
            common =  mostRepeated.getKey();
            //System.out.print(common);
        }
        return common;
    }
}
