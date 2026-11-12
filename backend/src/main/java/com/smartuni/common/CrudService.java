package com.smartuni.common;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;

/**
 * Optional, reusable CRUD service for repositories that support JPA Specifications.
 *
 * T  = entity type
 * ID = entity ID type (Long, UUID, etc.)
 * R  = repository type
 *
 * A concrete subclass must be a Spring bean (for example, annotated with @Service)
 * and call super(repository) from its constructor.
 *
 * Requiring both repository interfaces at compile time avoids the unsafe casts
 * in an unbounded generic CRUD service.
 *
 * Deletion here is a HARD delete. Soft deletion is a domain rule and should be
 * implemented in a service whose entity has an explicit deleted flag and
 * whose queries exclude deleted records.
 */
@Transactional(readOnly = true)
public abstract class CrudService<T, ID, R extends JpaRepository<T, ID> & JpaSpecificationExecutor<T>> {

    protected final R repository;

    protected CrudService(R repository) {
        this.repository = Objects.requireNonNull(repository, "repository must not be null");
    }

    public List<T> findAll() {
        return repository.findAll();
    }

    public Page<T> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public List<T> findAll(Specification<T> specification) {
        return repository.findAll(specification);
    }

    public Page<T> findAll(Specification<T> specification, Pageable pageable) {
        return repository.findAll(specification, pageable);
    }

    public Optional<T> findById(ID id) {
        return repository.findById(id);
    }

    @Transactional
    public T save(T entity) {
        return repository.save(entity);
    }

    @Transactional
    public List<T> saveAll(Iterable<T> entities) {
        return repository.saveAll(entities);
    }

    @Transactional
    public void delete(T entity) {
        repository.delete(entity);
    }

    @Transactional
    public void deleteById(ID id) {
        repository.deleteById(id);
    }
}
