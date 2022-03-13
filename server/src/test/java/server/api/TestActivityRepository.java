package server.api;

import commons.Activity;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import server.database.ActivityRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class TestActivityRepository implements ActivityRepository {
    public final List<Activity> activities = new ArrayList<>();
    public final List<String> calledMethods = new ArrayList<>();

    @Override
    public List<Activity> findAll() {
        calledMethods.add("findAll");
        return this.activities;
    }

    @Override
    public List<Activity> findAll(Sort sort) {
        return null;
    }

    @Override
    public List<Activity> findAllById(Iterable<Long> longs) {
        return null;
    }

    @Override
    public <S extends Activity> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends Activity> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Activity> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<Activity> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Activity getOne(Long aLong) {
        return null;
    }

    @Override
    public Activity getById(Long id) {
        calledMethods.add("getById");
        return find(id).get();
    }

    private Optional<Activity> find(Long id) {
        return activities.stream().filter(q -> q.getRealID() == id).findFirst();
    }

    @Override
    public <S extends Activity> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Activity> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public Page<Activity> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Activity> S save(S entity) {
        calledMethods.add("save");
        activities.add(entity);
        return entity;
    }

    @Override
    public Optional<Activity> findById(Long aLong) {
        calledMethods.add("findById");
        return find(aLong);
    }

    @Override
    public boolean existsById(Long aLong) {
        calledMethods.add("existsById");
        return find(aLong).isPresent();
    }

    @Override
    public long count() {
        return activities.size();
    }

    @Override
    public void deleteById(Long aLong) {
        calledMethods.add("deleteById");
        activities.stream().filter(x -> x.getRealID() != aLong).toList();
    }

    @Override
    public void delete(Activity entity) {
        calledMethods.add("delete");
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends Activity> entities) {

    }

    @Override
    public void deleteAll() {
        calledMethods.add("deleteAll");
        activities.clear();
    }

    @Override
    public <S extends Activity> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Activity> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Activity> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Activity> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Activity, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }
}
