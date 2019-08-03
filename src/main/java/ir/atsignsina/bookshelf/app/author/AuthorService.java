package ir.atsignsina.bookshelf.app.author;

import ir.atsignsina.bookshelf.app.author.proto.BookContribute;
import ir.atsignsina.bookshelf.app.contribute.Contribute;
import ir.atsignsina.bookshelf.concerns.exception.data.DataNotFoundException;
import ir.atsignsina.bookshelf.concerns.exception.request.RequestBodyException;
import ir.atsignsina.bookshelf.concerns.utils.NilUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthorService {
  private AuthorRepository authorRepository;

  public AuthorService(AuthorRepository authorRepository) {
    this.authorRepository = authorRepository;
  }

  /**
   * check request body and create author.
   *
   * @param author request body
   * @return saved author
   */
  Author createAuthor(Author author) {
    checkCreationProto(author);
    return authorRepository.save(author);
  }

  /**
   * Check author creation prototype
   *
   * @param author request body prototype
   */
  private void checkCreationProto(Author author) {
    if (NilUtils.checkNullOrEmpty(author.getName())) {
      throw new RequestBodyException("NAME_IS_INVALID");
    }
  }

  /**
   * find author from database
   *
   * @param id author id
   * @return optional which is filled with found author or is empty if query will not return any
   *     value
   */
  private Optional<Author> findById(Long id) {
    return authorRepository.findById(id);
  }

  /**
   * Find author from database based on input id
   *
   * @param id author id
   * @return found author
   */
  Author getAuthor(Long id) {
    Optional<Author> author = findById(id);
    if (author.isPresent()) {
      return author.get();
    } else {
      throw new DataNotFoundException("AUTHOR_NOT_FOUND");
    }
  }

  /**
   * Search in authors with name containing in specified page
   *
   * @param name string will be query in name attribute of all authors. If name is null or empty
   *     this method returns all authors without specified where clause.
   * @param pageable returning page
   * @return page of found authors
   */
  Page<Author> searchAuthors(String name, Pageable pageable) {
    return (name == null || name.isEmpty())
        ? authorRepository.findAll(pageable)
        : authorRepository.findByNameContaining(name, pageable);
  }

  /** Delete all authors from database */
  void deleteAuthors() {
    authorRepository.deleteAll();
  }

  /**
   * Delete specified author from database
   *
   * @param id author's id
   */
  void deleteAuthor(Long id) {
    authorRepository.delete(getAuthor(id));
  }

  /**
   * Edit author with request body
   *
   * @param id author's id
   * @param authorForEdit request body of newly object
   * @return edited author
   */
  Author editAuthor(Long id, Author authorForEdit) {
    Author author = getAuthor(id);
    if (authorForEdit.getName() != null && !author.getName().isEmpty()) {
      author.setName(authorForEdit.getName());
    }
    return authorRepository.save(author);
  }

  /**
   * find a list of authors by their id
   *
   * @param authorsIds list of ids
   * @return list of author. If some id does not exist so there are not presented in returning list.
   *     So returning list size may differ with input list size
   */
  public List<Author> findByIdIn(List<Long> authorsIds) {
    return authorRepository.findByIdIn(authorsIds);
  }

  /**
   * Make book contributes from set of contributes
   *
   * @param contributeSet set of author's contribute
   * @return list of bookContribute which made from set of author's contributes
   */
  private List<BookContribute> makeBookContributes(Set<Contribute> contributeSet) {
    return contributeSet.stream().map(this::makeBookContribute).collect(Collectors.toList());
  }

  /**
   * Make book contribute from contribute
   *
   * @param contribute contribute which returning value made from it
   * @return bookContribute created from author contributes in books
   */
  private BookContribute makeBookContribute(Contribute contribute) {
    BookContribute bookContribute = new BookContribute();
    bookContribute.setBook(contribute.getBook());
    bookContribute.setContribute(contribute.getContribute());
    return bookContribute;
  }
}
