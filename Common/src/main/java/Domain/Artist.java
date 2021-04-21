package Domain;

public class Artist extends Entity<Long>{
    private String name;
    private String genre;

    public Artist(Long id, String name, String genre) {
        super.setId(id);
        this.name = name;
        this.genre = genre;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Artist{ id=" + this.getId() +
                ", name='" + name + '\'' +
                ", genre='" + genre + '\'' +
                '}';
    }
}
