package ir.atsignsina.bookshelf.app.book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface BookRepository extends PagingAndSortingRepository<Book, Long> {
  Page<Book> findByNameContaining(String name, Pageable pageable);
}
