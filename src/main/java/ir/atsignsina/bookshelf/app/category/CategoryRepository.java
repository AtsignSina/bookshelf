package ir.atsignsina.bookshelf.app.category;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface CategoryRepository extends PagingAndSortingRepository<Category, Long> {
  Page<Category> findByNameContaining(String name, Pageable pageable);

  List<Category> findByIdIn(List<Long> ids);
}
