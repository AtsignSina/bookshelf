package ir.atsignsina.bookshelf.app.book;

import ir.atsignsina.bookshelf.app.book.proto.BookCreationProto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("book")
public class BookController {
  private final BookService bookService;

  @Autowired
  public BookController(BookService bookService) {
    this.bookService = bookService;
  }

  @PostMapping("")
  public ResponseEntity createBook(@RequestBody BookCreationProto bookCreationProto) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(bookService.createBook(bookCreationProto));
  }

  @GetMapping("")
  public ResponseEntity searchBooks(String name, Pageable pageable) {
    return ResponseEntity.status(HttpStatus.OK).body(bookService.searchBooks(name, pageable));
  }

  @DeleteMapping("")
  public ResponseEntity flushBooks() {
    bookService.deleteBooks();
    return ResponseEntity.status(HttpStatus.ACCEPTED).build();
  }

  @GetMapping("{id}")
  public ResponseEntity getBook(@PathVariable(value = "id") Long id) {
    return ResponseEntity.status(HttpStatus.OK).body(bookService.getBook(id));
  }

  @DeleteMapping("{id}")
  public ResponseEntity deleteBook(@PathVariable(value = "id") Long id) {
    bookService.deleteBook(id);
    return ResponseEntity.status(HttpStatus.ACCEPTED).build();
  }

  @PutMapping("{id}")
  public ResponseEntity editBook(
      @PathVariable(value = "id") Long id, @RequestBody Book bookForEdit) {
    return ResponseEntity.status(HttpStatus.OK).body(bookService.editBook(id, bookForEdit));
  }
}
