package ru.ifmo.rain.bashunov.arrayset;

import java.util.*;

@SuppressWarnings("unused")
public class ArraySet<E> extends AbstractSet<E> implements NavigableSet<E> {

    /* private fields */
    private List<E> elements, reversedElements;
    private Comparator<? super E> comparator;
    private final int start, end;
    private boolean reversed;

    /* constructors */
    public ArraySet() {
        this(Collections.emptyList(), Collections.emptyList(), null, 0, -1, false);
    }

    public ArraySet(Collection<? extends E> collection) {
        this(collection, null);
    }

    public ArraySet(Comparator<? super E> comparator) {
        this(Collections.emptyList(), Collections.emptyList(), comparator, 0, -1, false);
    }

    @SuppressWarnings("WeakerAccess")
    public ArraySet(Collection<? extends E> collection, Comparator<? super E> comparator) {
        Set<E> treeSet = new TreeSet<>(comparator);
        treeSet.addAll(collection);
        this.elements = new ArrayList<>(treeSet);
        this.reversedElements = new ArrayList<>(elements);
        Collections.reverse(reversedElements);
        this.comparator = comparator;
        this.start = 0;
        this.end = elements.size() - 1;
        this.reversed = false;
    }

    /* common methods */
    @Override
    public Comparator<? super E> comparator() {
        return (!reversed) ? comparator : reversedComparator();
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<>() {

            private int currentPosition = -1;

            @Override
            public boolean hasNext() {
                return (!reversed) ? start + currentPosition < end : end - currentPosition > start;
            }

            @Override
            public E next() {
                currentPosition++;
                return (!reversed) ? elements.get(start + currentPosition) : elements.get(end - currentPosition);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("remove");
            }
        };
    }

    @Override
    public Iterator<E> descendingIterator() {
        return descendingSet().iterator();
    }

    @Override
    public NavigableSet<E> descendingSet() {
        return new ArraySet<>(elements, reversedElements, comparator, start, end, !reversed);
    }

    /* basic methods */
    @Override
    public E first() {
        checkEmpty();
        return (!reversed) ? elements.get(start) : elements.get(end);
    }

    @Override
    public E last() {
        checkEmpty();
        return (!reversed) ? elements.get(end) : elements.get(start);
    }

    @Override
    public int size() {
        return end - start + 1;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    /* navigation */
    @Override
    public E lower(E e) {
        return getElementFromNaturalIndex(lowerOrHigher(e, reversed));
    }

    @Override
    public E floor(E e) {
        return getElementFromNaturalIndex(floorOrCeiling(e, reversed));
    }

    @Override
    public E ceiling(E e) {
        return getElementFromNaturalIndex(floorOrCeiling(e, !reversed));
    }

    @Override
    public E higher(E e) {
        return getElementFromNaturalIndex(lowerOrHigher(e, !reversed));
    }

    /* subsets */
    @SuppressWarnings("unchecked")
    @Override
    public NavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
        if (comparator() != null && comparator().compare(fromElement, toElement) > 0)
            throw new IllegalArgumentException();
        if (comparator() == null && (fromElement instanceof Comparable) && ((Comparable) fromElement).compareTo(toElement) > 0)
            throw new IllegalArgumentException();
        if (reversed) {
            return getSubSet(getToIndex(toElement, toInclusive), getFromIndex(fromElement, fromInclusive));
        } else {
            return getSubSet(getFromIndex(fromElement, fromInclusive), getToIndex(toElement, toInclusive));
        }
    }

    @Override
    public NavigableSet<E> headSet(E toElement, boolean toInclusive) {
        return (!reversed) ? getSubSet(start, getToIndex(toElement, toInclusive)) : getSubSet(getToIndex(toElement, toInclusive), end);
    }

    @Override
    public NavigableSet<E> tailSet(E fromElement, boolean fromInclusive) {
        return (!reversed) ? getSubSet(getFromIndex(fromElement, fromInclusive), end) : getSubSet(start, getFromIndex(fromElement, fromInclusive));
    }

    @Override
    public SortedSet<E> subSet(E fromElement, E toElement) {
        return subSet(fromElement, true, toElement, false);
    }

    @Override
    public SortedSet<E> headSet(E toElement) {
        return headSet(toElement, false);
    }

    @Override
    public SortedSet<E> tailSet(E fromElement) {
        return tailSet(fromElement, true);
    }

    /* unsupported operations */
    @Override
    public E pollFirst() {
        throw new UnsupportedOperationException();
    }

    @Override
    public E pollLast() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    /* other methods */
    @SuppressWarnings("unchecked")
    @Override
    public boolean contains(Object o) {
        checkArgument((E) o);
        return Collections.binarySearch(elements, (E) o, comparator) >= 0;
    }

    /* private constructors */
    private ArraySet(List<E> elements, List<E> reversedElements, Comparator<? super E> comparator, int start, int end, boolean reversed) {
        this.elements = elements;
        this.reversedElements = reversedElements;
        this.comparator = comparator;
        this.start = start;
        this.end = end;
        this.reversed = reversed;
    }

    /* private useful methods */
    private void checkEmpty() {
        if (isEmpty()) throw new NoSuchElementException();
    }

    private void checkArgument(E e) {
        if (e == null) throw new NullPointerException();
    }

    private Comparator<? super E> reversedComparator() {
        return Collections.reverseOrder(comparator);
    }

    /* private methods for navigation */
    private int find(List<E> list, E e, Comparator<? super E> comparator) {
        checkArgument(e);
        int x = Collections.binarySearch(list, e, comparator);
        if (x < 0) x = -x - 1;
        return x;
    }

    private int greater(List<E> list, E e, Comparator<? super E> comparator) {
        checkArgument(e);
        int x = Collections.binarySearch(list, e, comparator);
        if (x < 0) {
            x = -x - 1;
        } else {
            x++;
        }
        return x;
    }

    private int lowerOrHigher(E e, boolean mode) {
        // mode: false for finding lower, true for finding lower
        return (mode) ? greater(elements, e, comparator) : size() - 1 - greater(reversedElements, e, reversedComparator());
    }

    private int floorOrCeiling(E e, boolean mode) {
        // mode: false for finding ceiling, true for finding floor
        return (mode) ? find(elements, e, comparator) : size() - 1 - find(reversedElements, e, reversedComparator());
    }

    private E getElementFromNaturalIndex(int index) {
        // index should be an index of original array
        if (start <= index && index <= end) {
            return elements.get(index);
        } else {
            return null;
        }
    }

    /* private methods for subsets */
    private int getFromIndex(E fromElement, boolean fromInclusive) {
        return (fromInclusive) ? floorOrCeiling(fromElement, !reversed) : lowerOrHigher(fromElement, !reversed);
    }

    private int getToIndex(E toElement, boolean toInclusive) {
        return (toInclusive) ? floorOrCeiling(toElement, reversed) : lowerOrHigher(toElement, reversed);
    }

    private NavigableSet<E> getSubSet(int fromIndex, int toIndex) {
        if (fromIndex < start || toIndex > end) {
            throw new IllegalArgumentException();
        } else if (fromIndex > toIndex) {
            return new ArraySet<>(elements, reversedElements, comparator, fromIndex, fromIndex - 1, reversed);
        } else {
            return new ArraySet<>(elements, reversedElements, comparator, fromIndex, toIndex, reversed);
        }
    }
}
