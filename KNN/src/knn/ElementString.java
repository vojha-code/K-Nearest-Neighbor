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
public class ElementString implements Comparable<ElementString> {

    int index;
    String value;

    ElementString(int index, String value){
        this.index = index;
        this.value = value;
    }

    @Override
    public int compareTo(ElementString e) {
        return this.value.compareTo(e.value);
    }
}