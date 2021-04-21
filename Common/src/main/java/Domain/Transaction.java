package Domain;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class Transaction extends Entity<Integer>{
    public static Set<Integer> idList = new HashSet<>();
    private String client;
    private Show show;
    private LocalDate date;
    private Integer ticketNumber;

    public static void initIds(Iterable<Transaction> list) {
        list.forEach(e-> {
            idList.add(e.getId());
        });
    }

    private static Integer generateUniqueId() {
        UUID idOne = UUID.randomUUID();
        String str=""+idOne;
        int uid=str.hashCode();
        String filterStr=""+uid;
        str=filterStr.replaceAll("-", "");
        return Integer.parseInt(str);
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public Show getShow() {
        return show;
    }

    public void setShow(Show show) {
        this.show = show;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(Integer ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public Transaction(String client, Show show, LocalDate date, Integer ticketNumber) {
        Integer id = generateUniqueId();
        while (idList.contains(id)) {
            id = generateUniqueId();
        }
        this.setId(id);
        this.client = client;
        this.show = show;
        this.date = date;
        this.ticketNumber = ticketNumber;
    }

    public Transaction(Integer id, String client, Show show, LocalDate date, Integer ticketNumber) {
        this.setId(id);
        this.client = client;
        this.show = show;
        this.date = date;
        this.ticketNumber = ticketNumber;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id='" + this.getId() + '\'' +
                ", client='" + client + '\'' +
                ", show=" + show.toString() +
                ", date=" + date +
                ", ticketNumber=" + ticketNumber +
                '}';
    }
}
