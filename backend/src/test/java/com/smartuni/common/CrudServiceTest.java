package com.smartuni.common;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

class CrudServiceTest {

    private final ItemRepository repository = mock(ItemRepository.class);
    private final ItemCrudService service = new ItemCrudService(repository);

    @Test
    void delegatesReadOperationsWithCorrectTypes() {
        Item item = new Item(1L);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Item> page = new PageImpl<>(List.of(item));

        when(repository.findAll()).thenReturn(List.of(item));
        when(repository.findAll(pageable)).thenReturn(page);
        when(repository.findById(1L)).thenReturn(Optional.of(item));

        assertThat(service.findAll()).containsExactly(item);
        assertThat(service.findAll(pageable)).isSameAs(page);
        assertThat(service.findById(1L)).contains(item);
    }

    @Test
    void delegatesSpecificationQueriesWithoutCasts() {
        Specification<Item> specification = (root, query, builder) -> builder.conjunction();
        Pageable pageable = PageRequest.of(0, 5);
        Item item = new Item(2L);
        Page<Item> page = new PageImpl<>(List.of(item));

        when(repository.findAll(specification)).thenReturn(List.of(item));
        when(repository.findAll(specification, pageable)).thenReturn(page);

        assertThat(service.findAll(specification)).containsExactly(item);
        assertThat(service.findAll(specification, pageable)).isSameAs(page);
    }

    @Test
    void delegatesWritesAndHardDeletion() {
        Item item = new Item(3L);
        List<Item> items = List.of(item);

        when(repository.save(item)).thenReturn(item);
        when(repository.saveAll(items)).thenReturn(items);

        assertThat(service.save(item)).isSameAs(item);
        assertThat(service.saveAll(items)).containsExactly(item);

        service.delete(item);
        service.deleteById(3L);

        verify(repository).delete(item);
        verify(repository).deleteById(3L);
    }

    record Item(Long id) {
    }

    interface ItemRepository extends JpaRepository<Item, Long>, JpaSpecificationExecutor<Item> {
    }

    static class ItemCrudService extends CrudService<Item, Long, ItemRepository> {
        ItemCrudService(ItemRepository repository) {
            super(repository);
        }
    }
}
