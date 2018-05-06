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
public class ElementDouble implements Comparable<ElementDouble> {

    int index;
    double value;

    ElementDouble(int index, double value){
        this.index = index;
        this.value = value;
    }

    @Override
    public int compareTo(ElementDouble e) {
        return (int)this.value - (int)e.value;
    }
}