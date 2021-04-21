import Domain.Transaction;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class MainClient {

    public static void main(String[] args) {
        System.out.println(Transaction.idList);

        GuiInit.main(args);
    }
}
