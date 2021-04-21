package Observer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public interface ObservableInterface {
    List<ObserverInterface> observerList = new ArrayList<>();

    default List<ObserverInterface> getObserverList() {
        return observerList;
    }

    default void addObserver(ObserverInterface o) {
        synchronized (this.observerList) {
            observerList.add(o);
            System.out.println(observerList);
        }
    }

    default void removeObserver(ObserverInterface o) {
        synchronized (this.observerList) {
            observerList.remove(o);
        }
    }

    default void myNotifyAll() {
        observerList.forEach(e-> {
            try {
                e.notified();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
    }

    default void myNotifyAllExcept(ObserverInterface obs) {
        observerList.forEach(e-> {
            if (e != obs) {
                try {
                    e.notified();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });
    }
}
