package ir.atsignsina.bookshelf.app.publisher;

import ir.atsignsina.bookshelf.app.book.Book;
import ir.atsignsina.bookshelf.concerns.exception.data.DataNotFoundException;
import ir.atsignsina.bookshelf.concerns.exception.request.RequestBodyException;
import ir.atsignsina.bookshelf.concerns.utils.NilUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class PublisherService {
  private PublisherRepository publisherRepository;

  public PublisherService(PublisherRepository publisherRepository) {
    this.publisherRepository = publisherRepository;
  }

  /**
   * check request body and create publisher.
   *
   * @param publisher request body
   * @return saved publisher
   */
  Publisher createPublisher(Publisher publisher) {
    checkCreationProto(publisher);
    return publisherRepository.save(publisher);
  }

  /**
   * Check book creation prototype for ensure request body met all requirements
   *
   * @param publisher request body
   */
  private void checkCreationProto(Publisher publisher) {
    if (NilUtils.checkNullOrEmpty(publisher.getName())) {
      throw new RequestBodyException("NAME_IS_EMPTY");
    }
  }

  /**
   * find publisher from database
   *
   * @param id publisher id
   * @return optional which is filled with found publisher or is empty if query will not return any
   *     value
   */
  public Optional<Publisher> findById(Long id) {
    return publisherRepository.findById(id);
  }

  /**
   * Find publisher from database based on input id
   *
   * @param id publisher id
   * @return found publisher
   */
  Publisher getPublisher(Long id) {
    Optional<Publisher> publisher = findById(id);
    if (publisher.isPresent()) {
      return publisher.get();
    } else {
      throw new DataNotFoundException("AUTHOR_NOT_FOUND");
    }
  }

  /**
   * Search in publishers with name containing in specified page
   *
   * @param name string will be query in name attribute of all publishers. If name is null or empty
   *     this method returns all publishers without specified where clause.
   * @param pageable returning page
   * @return page of found publishers
   */
  Page<Publisher> searchPublishers(String name, Pageable pageable) {
    return (name == null || name.isEmpty())
        ? publisherRepository.findAll(pageable)
        : publisherRepository.findByNameContaining(name, pageable);
  }

  /** Delete all publishers from database */
  void deletePublishers() {
    publisherRepository.deleteAll();
  }

  /**
   * Delete specified publisher from database
   *
   * @param id publisher's id
   */
  void deletePublisher(Long id) {
    publisherRepository.delete(getPublisher(id));
  }

  /**
   * Edit publisher with request body
   *
   * @param id publisher's id
   * @param publisherForEdit request body of newly object
   * @return edited publisher
   */
  Publisher editPublisher(Long id, Publisher publisherForEdit) {
    Publisher publisher = getPublisher(id);
    if (!NilUtils.checkNullOrEmpty(publisherForEdit.getName())) {
      publisher.setName(publisherForEdit.getName());
    }
    if (!NilUtils.checkNull(publisherForEdit.getDescription())) {
      publisher.setDescription(publisherForEdit.getDescription());
    }
    return publisherRepository.save(publisher);
  }

  /**
   * Get list of specified publisher book
   *
   * @param id publisher id
   * @return list of books
   */
  Set<Book> getPublisherBooks(Long id) {
    Publisher publisher = getPublisher(id);
    return publisher.getBooks();
  }
}
