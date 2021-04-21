package Observer;

import Domain.Show;

import java.io.IOException;

public interface ObserverInterface {
    void notified() throws IOException;
}
