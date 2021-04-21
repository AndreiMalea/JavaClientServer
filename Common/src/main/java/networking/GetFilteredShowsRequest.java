package networking;

import java.time.LocalDate;

public class GetFilteredShowsRequest implements Request{
    private LocalDate date;

    public GetFilteredShowsRequest(LocalDate date) {
        this.date = date;
    }

    public LocalDate getDate() { return date; }
}
