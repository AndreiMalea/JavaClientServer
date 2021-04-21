package Domain;

public class Office extends Entity<Long> {
    private String location;

    public Office(Long id, String location) {
        this.setId(id);
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "Office{ id=" + this.getId() +
                ", location='" + location + '\'' +
                '}';
    }
}
