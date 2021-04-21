package Domain;

import java.time.LocalDate;

public class ShowAux extends Entity<Long>{
    private Show show;
    private String artist;
    private Integer ticketNumber;
    private LocalDate date;

    public ShowAux(Show show) {
        this.show=show;
        this.setId(show.getId());
        this.artist = show.getArtist().getName();
        this.ticketNumber = show.getTicketNumber();
        this.date = show.getDate();
    }

    public Show getShow() {
        return show;
    }

    public void setShow(Show show) {
        this.show = show;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public Integer getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(Integer ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
