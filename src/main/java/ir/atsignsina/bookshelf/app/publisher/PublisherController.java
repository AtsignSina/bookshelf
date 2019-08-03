package ir.atsignsina.bookshelf.app.publisher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("publisher")
public class PublisherController {
  private final PublisherService publisherService;

  @Autowired
  public PublisherController(PublisherService publisherService) {
    this.publisherService = publisherService;
  }

  @PostMapping("")
  public ResponseEntity createPublisher(@RequestBody Publisher publisher) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(publisherService.createPublisher(publisher));
  }

  @GetMapping("")
  public ResponseEntity searchPublishers(String name, Pageable pageable) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(publisherService.searchPublishers(name, pageable));
  }

  @DeleteMapping("")
  public ResponseEntity deletePublishers() {
    publisherService.deletePublishers();
    return ResponseEntity.status(HttpStatus.ACCEPTED).build();
  }

  @GetMapping("{id}")
  public ResponseEntity getPublisher(@PathVariable(value = "id") Long id) {
    return ResponseEntity.status(HttpStatus.OK).body(publisherService.getPublisher(id));
  }

  @DeleteMapping("{id}")
  public ResponseEntity deletePublisher(@PathVariable(value = "id") Long id) {
    publisherService.deletePublisher(id);
    return ResponseEntity.status(HttpStatus.ACCEPTED).build();
  }

  @PutMapping("{id}")
  public ResponseEntity editPublisher(
      @PathVariable(value = "id") Long id, @RequestBody Publisher publisherForEdit) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(publisherService.editPublisher(id, publisherForEdit));
  }
}
