package server.api;

import commons.Player;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import server.database.PlayerRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class TestPlayerRepository implements PlayerRepository {

    public final List<Player> players = new ArrayList<>();
    public final List<String> calledMethods = new ArrayList<>();

    @Override
    public List<Player> findAll() {
        calledMethods.add("findAll");
        return players;
    }

    @Override
    public List<Player> findAll(Sort sort) {
        return null;
    }

    @Override
    public List<Player> findAllById(Iterable<String> strings) {
        return null;
    }

    @Override
    public <S extends Player> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends Player> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Player> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<Player> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<String> strings) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Player getOne(String s) {
        return null;
    }

    @Override
    public Player getById(String s) {
        calledMethods.add("getById");
        return find(s).get();
    }

    private Optional<Player> find(String id) {
        return players.stream().filter(q -> q.getUsername().equals(id)).findFirst();
    }

    @Override
    public <S extends Player> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Player> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public Page<Player> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Player> S save(S entity) {
        calledMethods.add("save");
        players.add(entity);
        return entity;
    }

    @Override
    public Optional<Player> findById(String s) {
        calledMethods.add("findById");
        return find(s);
    }

    @Override
    public boolean existsById(String s) {
        calledMethods.add("existsById");
        return find(s).isPresent();
    }

    @Override
    public long count() {
        return players.size();
    }

    @Override
    public void deleteById(String s) {
        calledMethods.add("deleteById");
        players.remove(new Player(s));
    }

    @Override
    public void delete(Player entity) {
        calledMethods.add("delete");
    }

    @Override
    public void deleteAllById(Iterable<? extends String> strings) {

    }

    @Override
    public void deleteAll(Iterable<? extends Player> entities) {

    }

    @Override
    public void deleteAll() {
        calledMethods.add("deleteAll");
        players.clear();
    }

    @Override
    public <S extends Player> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Player> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Player> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Player> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Player, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }
}
