package Interfaces;

public interface Repository<ID, E> {

    E findOne(ID id);

    Iterable<E> findAll();

    E save(E entity);

    E delete(ID id);

    E update(E entity);

}
