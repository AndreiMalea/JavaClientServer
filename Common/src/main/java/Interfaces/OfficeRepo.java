package Interfaces;

import Domain.Office;

import java.util.List;

public interface OfficeRepo extends Repository<Long, Office> {
    List<Office> filterByLocation(String location);
}
