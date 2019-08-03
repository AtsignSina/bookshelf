package ir.atsignsina.bookshelf.app.author;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("author")
public class AuthorController {
  private final AuthorService authorService;

  @Autowired
  public AuthorController(AuthorService authorService) {
    this.authorService = authorService;
  }

  @PostMapping("")
  public ResponseEntity createAuthor(@RequestBody Author author) {
    return ResponseEntity.status(HttpStatus.CREATED).body(authorService.createAuthor(author));
  }

  @GetMapping("")
  public ResponseEntity searchAuthors(String name, Pageable pageable) {
    return ResponseEntity.status(HttpStatus.OK).body(authorService.searchAuthors(name, pageable));
  }

  @DeleteMapping("")
  public ResponseEntity flushAuthors() {
    authorService.deleteAuthors();
    return ResponseEntity.status(HttpStatus.ACCEPTED).build();
  }

  @GetMapping("{id}")
  public ResponseEntity getAuthor(@PathVariable(value = "id") Long id) {
    return ResponseEntity.status(HttpStatus.OK).body(authorService.getAuthor(id));
  }

  @DeleteMapping("{id}")
  public ResponseEntity deleteAuthor(@PathVariable(value = "id") Long id) {
    authorService.deleteAuthor(id);
    return ResponseEntity.status(HttpStatus.ACCEPTED).build();
  }

  @PutMapping("{id}")
  public ResponseEntity editAuthor(
      @PathVariable(value = "id") Long id, @RequestBody Author authorForEdit) {
    return ResponseEntity.status(HttpStatus.OK).body(authorService.editAuthor(id, authorForEdit));
  }
}
