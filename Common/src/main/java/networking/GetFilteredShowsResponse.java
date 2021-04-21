package networking;

import Domain.ShowDTO;

public class GetFilteredShowsResponse implements Response{
    private ShowDTO[] shows;

    public GetFilteredShowsResponse(ShowDTO[] shows) {
        this.shows = shows;
    }

    public ShowDTO[] getShows() { return shows; }
}
