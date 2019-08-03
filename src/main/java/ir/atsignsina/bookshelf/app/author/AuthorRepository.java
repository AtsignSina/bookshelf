package ir.atsignsina.bookshelf.app.author;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface AuthorRepository extends PagingAndSortingRepository<Author, Long> {
  Page<Author> findByNameContaining(String name, Pageable pageable);

  List<Author> findByIdIn(List<Long> ids);
}
