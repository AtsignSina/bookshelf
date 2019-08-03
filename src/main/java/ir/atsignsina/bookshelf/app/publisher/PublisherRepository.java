package ir.atsignsina.bookshelf.app.publisher;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PublisherRepository extends PagingAndSortingRepository<Publisher, Long> {
  Page<Publisher> findByNameContaining(String name, Pageable pageable);
}
