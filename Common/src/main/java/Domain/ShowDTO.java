package Domain;

import java.io.Serializable;
import java.time.LocalDate;

public class ShowDTO implements Serializable {
    private Long id;
    private Long idArtist;
    private String name;
    private String genre;
    private Integer ticketNumber;
    private String date;

    public static Show parseShow(ShowDTO s) {
        return new Show(s.getId(), new Artist(s.getIdArtist(), s.getName(), s.getGenre()), s.getTicketNumber(), LocalDate.parse(s.getDate()));
    }

    public ShowDTO(Show s) {
        id = s.getId();
        idArtist = s.getArtist().getId();
        name = s.getArtist().getName();
        genre = s.getArtist().getGenre();
        ticketNumber = s.getTicketNumber();

        String month = s.getDate().getMonthValue()+"";
        if (s.getDate().getMonthValue()<=9) {
            month = "0"+s.getDate().getMonthValue();
        }

        String day = s.getDate().getDayOfMonth()+"";
        if (s.getDate().getDayOfMonth()<=9) {
            day = "0"+s.getDate().getDayOfMonth();
        }

        date = s.getDate().getYear()+"-"+month+"-"+day;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdArtist() {
        return idArtist;
    }

    public void setIdArtist(Long idArtist) {
        this.idArtist = idArtist;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Integer getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(Integer ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
