package networking;

import Domain.ShowDTO;

public class GetAllShowsRespons implements Response{
    private ShowDTO[] shows;

    public GetAllShowsRespons(ShowDTO[] shows) {
        this.shows = shows;
    }

    public ShowDTO[] getShows() { return shows; }
}
