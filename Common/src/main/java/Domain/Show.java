package Domain;

import java.time.LocalDate;

public class Show extends Entity<Long>{
    private Artist artist;
    private Integer ticketNumber;
    private LocalDate date;

    public Show(Long id, Artist artist, Integer ticketNumber, LocalDate date) {
        this.setId(id);
        this.artist = artist;
        this.ticketNumber = ticketNumber;
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public Integer getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(Integer ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public void decreaseTicketNumber() {
        ticketNumber--;
    }

    public void decreaseTicketNumber(Integer no) {
        ticketNumber-=no;
    }

    @Override
    public String toString() {
        return "Show{ id=" + this.getId() +
                ", artist=" + artist.toString() +
                ", ticketNumber=" + ticketNumber +
                ", date= " + date +
                '}';
    }
}
