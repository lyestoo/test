package org.dom4j.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 *
 * @author Jirsák Filip
 * @since 2.0
 */
public class LazyList<T> implements List<T> {
    private List<T> implementation;

    public LazyList() {
        this.implementation = new ArrayList<T>();
    }

    public LazyList(int initialCapacity) {
        this.implementation = new ArrayList<T>(initialCapacity);
    }

    public <T> T[] toArray(T[] a) {
        return implementation.toArray(a);
    }

    public Object[] toArray() {
        return implementation.toArray();
    }

    public List<T> subList(int fromIndex, int toIndex) {
        return implementation.subList(fromIndex, toIndex);
    }

    public int size() {
        return implementation.size();
    }

    public T set(int index, T element) {
        return implementation.set(index, element);
    }

    public boolean retainAll(Collection<?> c) {
        return implementation.retainAll(c);
    }

    public boolean removeAll(Collection<?> c) {
        return implementation.removeAll(c);
    }

    public T remove(int index) {
        return implementation.remove(index);
    }

    public boolean remove(Object o) {
        return implementation.remove(o);
    }

    public ListIterator<T> listIterator(int index) {
        return implementation.listIterator(index);
    }

    public ListIterator<T> listIterator() {
        return implementation.listIterator();
    }

    public int lastIndexOf(Object o) {
        return implementation.lastIndexOf(o);
    }

    public Iterator<T> iterator() {
        return implementation.iterator();
    }

    public boolean isEmpty() {
        return implementation.isEmpty();
    }

    public int indexOf(Object o) {
        return implementation.indexOf(o);
    }

    @Override
    public int hashCode() {
        return implementation.hashCode();
    }

    public T get(int index) {
        return implementation.get(index);
    }

    @Override
    public boolean equals(Object o) {
        return implementation.equals(o);
    }

    public boolean containsAll(Collection<?> c) {
        return implementation.containsAll(c);
    }

    public boolean contains(Object o) {
        return implementation.contains(o);
    }

    public void clear() {
        implementation.clear();
    }

    public boolean addAll(int index, Collection<? extends T> c) {
        return implementation.addAll(index, c);
    }

    public boolean addAll(Collection<? extends T> c) {
        return implementation.addAll(c);
    }

    public void add(int index, T element) {
        implementation.add(index, element);
    }

    public boolean add(T e) {
        return implementation.add(e);
    }
}
