package ir.atsignsina.bookshelf.app.category;

import ir.atsignsina.bookshelf.app.category.proto.CategoryCreationProto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("category")
public class CategoryController {
  private final CategoryService categoryService;

  @Autowired
  public CategoryController(CategoryService categoryService) {
    this.categoryService = categoryService;
  }

  @PostMapping("")
  public ResponseEntity createCategory(@RequestBody CategoryCreationProto category) {
    return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.createCategory(category));
  }

  @GetMapping("")
  public ResponseEntity searchCategories(String name, Pageable pageable) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(categoryService.searchCategories(name, pageable));
  }

  @DeleteMapping("")
  public ResponseEntity flushCategories() {
    categoryService.deleteCategories();
    return ResponseEntity.status(HttpStatus.ACCEPTED).build();
  }

  @GetMapping("{id}")
  public ResponseEntity getCategory(@PathVariable(value = "id") Long id) {
    return ResponseEntity.status(HttpStatus.OK).body(categoryService.getCategory(id));
  }

  @DeleteMapping("{id}")
  public ResponseEntity deleteCategory(@PathVariable(value = "id") Long id) {
    categoryService.deleteCategory(id);
    return ResponseEntity.status(HttpStatus.ACCEPTED).build();
  }

  @PutMapping("{id}")
  public ResponseEntity editCategory(
      @PathVariable(value = "id") Long id, @RequestBody Category categoryForEdit) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(categoryService.editCategory(id, categoryForEdit));
  }

  @GetMapping("{id}/book")
  public ResponseEntity getPublisherBooks(@PathVariable(value = "id") Long id) {
    return ResponseEntity.status(HttpStatus.OK).body(categoryService.getCategoryBooks(id));
  }
}
