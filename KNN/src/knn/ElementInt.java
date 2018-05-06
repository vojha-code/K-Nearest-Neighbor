/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package knn;

/**
 *
 * @author vojha
 */
public class ElementInt implements Comparable<ElementInt> {

    int index, value;

    ElementInt(int index, int value){
        this.index = index;
        this.value = value;
    }

    public int compareTo(ElementInt e) {
        return this.value - e.value;
    }
}