package ro.birsan.budgetone.util;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Irinel on 10/8/2015.
 */
public class CollectionHelper {
    public static <T> Collection<T> filter(Collection<T> target, IPredicate<T> predicate) {
        Collection<T> result = new ArrayList<T>();
        for (T element: target) {
            if (predicate.apply(element)) {
                result.add(element);
            }
        }

        return result;
    }
}
