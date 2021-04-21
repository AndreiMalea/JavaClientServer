package networking;

import Domain.Show;

import java.time.LocalDate;

public class TicketBoughtRequest implements Request{
    private String client;
    private Show show;
    private Integer ticketNumber;

    public TicketBoughtRequest(String client, Show show, Integer ticketNumber) {
        this.client = client;
        this.show = show;
        this.ticketNumber = ticketNumber;
    }

    public String getClient() {
        return client;
    }

    public Show getShow() {
        return show;
    }

    public Integer getTicketNumber() {
        return ticketNumber;
    }
}
