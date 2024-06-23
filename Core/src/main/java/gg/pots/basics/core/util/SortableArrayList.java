package gg.pots.basics.core.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SortableArrayList<T> extends ArrayList<T> {

    private final Comparator<T> comparator;

    /**
     * Constructor for making a new SortableArrayList instance
     *
     * @param comparator the comparator to sort the list with
     */
    public SortableArrayList(Comparator<T> comparator) {
        this.comparator = comparator;
    }

    /**
     * Constructor for making a new SortableArrayList instance
     *
     * @param comparator the comparator to sort the list with
     * @param list       a list which contents will be automatically added to this list
     */
    public SortableArrayList(List<T> list, Comparator<T> comparator) {
        super(list);
        this.comparator = comparator;
    }

    @Override
    public T remove(int index) {
        final T obj = super.remove(index);
        this.sort(this.comparator);

        return obj;
    }

    @Override
    public boolean add(T t) {
        final boolean success = super.add(t);
        this.sort(this.comparator);

        return success;
    }

    @Override
    public boolean remove(Object o) {
        final boolean success = super.remove(o);
        this.sort(this.comparator);

        return success;
    }
}
