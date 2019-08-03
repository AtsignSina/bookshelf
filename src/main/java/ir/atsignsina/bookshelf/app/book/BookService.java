package ir.atsignsina.bookshelf.app.book;

import ir.atsignsina.bookshelf.app.author.Author;
import ir.atsignsina.bookshelf.app.author.AuthorService;
import ir.atsignsina.bookshelf.app.book.proto.AuthorBookContributeProto;
import ir.atsignsina.bookshelf.app.book.proto.AuthorContributeProto;
import ir.atsignsina.bookshelf.app.book.proto.BookCreationProto;
import ir.atsignsina.bookshelf.app.category.Category;
import ir.atsignsina.bookshelf.app.category.CategoryService;
import ir.atsignsina.bookshelf.app.contribute.Contribute;
import ir.atsignsina.bookshelf.app.contribute.ContributeType;
import ir.atsignsina.bookshelf.app.publisher.Publisher;
import ir.atsignsina.bookshelf.app.publisher.PublisherService;
import ir.atsignsina.bookshelf.concerns.exception.data.DataNotFoundException;
import ir.atsignsina.bookshelf.concerns.exception.data.RequirementDataNotFoundException;
import ir.atsignsina.bookshelf.concerns.exception.request.RequestBodyException;
import ir.atsignsina.bookshelf.concerns.utils.NilUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BookService {
  private BookRepository bookRepository;
  private PublisherService publisherService;
  private AuthorService authorService;
  private CategoryService categoryService;

  public BookService(
      BookRepository bookRepository,
      PublisherService publisherService,
      AuthorService authorService,
      CategoryService categoryService) {
    this.bookRepository = bookRepository;
    this.publisherService = publisherService;
    this.authorService = authorService;
    this.categoryService = categoryService;
  }

  /**
   * check request body and create book.
   *
   * @param bookCreationProto request body
   * @return saved book
   */
  Book createBook(BookCreationProto bookCreationProto) {
    checkBookCreationPrototype(bookCreationProto);
    Book book = makeBookFromPrototype(bookCreationProto);
    bookRepository.save(book);
    fillContributes(book);
    return book;
  }

  /**
   * Check book creation prototype for ensure request body met all requirements
   *
   * @param bookCreationProto request body
   */
  private void checkBookCreationPrototype(BookCreationProto bookCreationProto) {
    if (NilUtils.checkNullOrEmpty(bookCreationProto.getName())) {
      throw new RequestBodyException("NAME_IS_EMPTY");
    } else if (NilUtils.checkNullOrEmpty(bookCreationProto.getIsbn())) {
      throw new RequestBodyException("ISBN_IS_EMPTY");
    } else if (NilUtils.checkNullOrZero(bookCreationProto.getPublisher())) {
      throw new RequestBodyException("PUBLISHER_IS_ZERO");
    } else if (NilUtils.checkNullOrEmpty(bookCreationProto.getContributes())) {
      throw new RequestBodyException("CONTRIBUTES_IS_EMPTY");
    } else if (NilUtils.checkNullOrEmpty(bookCreationProto.getCategories())) {
      throw new RequestBodyException("CATEGORIES_IS_EMPTY");
    } else if (NilUtils.checkNullOrZero(bookCreationProto.getPrice())) {
      throw new RequestBodyException("PRICE_IS_ZERO");
    }
    if (bookCreationProto.getCategories().stream().anyMatch(NilUtils::checkNullOrZero)) {
      throw new RequestBodyException("CATEGORY_IS_EMPTY");
    }
    for (AuthorBookContributeProto proto : bookCreationProto.getContributes()) {
      if (NilUtils.checkNullOrZero(proto.getAuthor())) {
        throw new RequestBodyException("AUTHOR_IS_EMPTY");
      }
      if (NilUtils.checkNull(proto.getContribute())) {
        throw new RequestBodyException("CONTRIBUTE_IS_EMPTY");
      }
      if (bookCreationProto.getContributes().stream()
          .filter(cp -> cp.getAuthor().equals(proto.getAuthor()))
          .filter(cp -> cp.hashCode() != proto.hashCode())
          .anyMatch(cp -> cp.getContribute().equals(proto.getContribute()))) {
        throw new RequestBodyException("DUPLICATE_CONTRIBUTE");
      }
    }
  }

  /**
   * make book object from request body. since user send prototype in request body, We need to map
   * this prototype into entity object. This method created for this purpose. This method find all
   * requirement data and will throw exception if needed.
   *
   * @param bookCreationProto request body
   * @return entity object
   */
  private Book makeBookFromPrototype(BookCreationProto bookCreationProto) {
    Book book = new Book();
    book.setName(bookCreationProto.getName());
    book.setIsbn(bookCreationProto.getIsbn());
    book.setPrice(bookCreationProto.getPrice());
    book.setPublisher(findPublisher(bookCreationProto.getPublisher()));
    book.setCategories(findCategories(bookCreationProto.getCategories()));
    book.getContributes().addAll(contributes(bookCreationProto.getContributes(), book));
    return book;
  }

  /**
   * create contributes set from prototype of book contribute
   *
   * @param contributesPrototypes request body contribute part
   * @param book entity
   * @return set of contributes
   */
  private Set<Contribute> contributes(
      List<AuthorBookContributeProto> contributesPrototypes, Book book) {
    List<Author> authors =
        authorService.findByIdIn(
            contributesPrototypes.stream()
                .map(AuthorBookContributeProto::getAuthor)
                .collect(Collectors.toList()));
    return contributesPrototypes.stream()
        .map(
            bookContributeProto ->
                makeAuthorContributeBook(
                    book,
                    findAuthor(authors, bookContributeProto.getAuthor()),
                    bookContributeProto.getContribute()))
        .collect(Collectors.toSet());
  }

  /**
   * Find specified author from list of authors. We fetch all authors of incoming book in one query
   * for performance. and find each of them in returned list
   *
   * @param authors list of fetched authors
   * @param id specified author id
   * @return founded author. otherwise throw an exception
   */
  private Author findAuthor(List<Author> authors, Long id) {
    Optional<Author> author = authors.stream().filter(auth -> auth.getId().equals(id)).findFirst();
    if (!author.isPresent()) {
      throw new RequirementDataNotFoundException("AUTHOR_DOES_NOT_EXIST");
    }
    return author.get();
  }

  /**
   * make Contribute entity from book and author and contribute type. this method written to use
   * inside of list stream map function
   *
   * @param book book
   * @param author author
   * @param contributeType type of contribute
   * @return contribute entity
   */
  private Contribute makeAuthorContributeBook(
      Book book, Author author, ContributeType contributeType) {
    Contribute contribute = new Contribute();
    contribute.setAuthor(author);
    contribute.setContribute(contributeType);
    contribute.setBook(book);
    return contribute;
  }

  /**
   * find publisher based on id.
   *
   * @param publisher id of publisher
   * @return found publisher. otherwise throw an exception
   */
  private Publisher findPublisher(Long publisher) {
    Optional<Publisher> publisherOptional = publisherService.findById(publisher);
    if (publisherOptional.isPresent()) {
      return publisherOptional.get();
    } else {
      throw new RequirementDataNotFoundException("PUBLISHER_NOT_FOUND");
    }
  }

  /**
   * Find list of categories
   *
   * @param ids wanted categories ids
   * @return list of found categories. If one or more of ids does not found throw an exception
   */
  private Set<Category> findCategories(List<Long> ids) {
    List<Category> categories = categoryService.findByIdIn(ids);
    if (categories.size() == ids.size()) {
      return new HashSet<>(categories);
    } else {
      throw new RequirementDataNotFoundException("CATEGORIES_NOT_FOUND");
    }
  }

  /**
   * Find book from database based on input id
   *
   * @param id book id
   * @return found book
   */
  Book getBook(Long id) {
    Optional<Book> book = bookRepository.findById(id);
    if (book.isPresent()) {
      fillContributes(book.get());
      return book.get();
    } else {
      throw new DataNotFoundException("AUTHOR_NOT_FOUND");
    }
  }

  /**
   * Search in books with name containing in specified page
   *
   * @param name string will be query in name attribute of all books. If name is null or empty this
   *     method returns all books without specified where clause.
   * @param pageable returning page
   * @return page of found books
   */
  Page<Book> searchBooks(String name, Pageable pageable) {
    Page<Book> pb =
        (name == null || name.isEmpty())
            ? bookRepository.findAll(pageable)
            : bookRepository.findByNameContaining(name, pageable);
    pb.forEach(this::fillContributes);
    return pb;
  }

  /** Delete all books from database */
  void deleteBooks() {
    bookRepository.deleteAll();
  }

  /**
   * Delete specified book from database
   *
   * @param id book's id
   */
  void deleteBook(Long id) {
    bookRepository.delete(getBook(id));
  }

  /**
   * Edit book with request body
   *
   * @param id book's id
   * @param bookForEdit request body of newly object
   * @return edited book
   */
  Book editBook(Long id, Book bookForEdit) {
    Book book = getBook(id);
    book.setPrice(bookForEdit.getPrice());
    return bookRepository.save(book);
  }

  /**
   * fill book's transient author field
   *
   * @param book book
   */
  private void fillContributes(Book book) {
    book.setAuthors(
        book.getContributes().stream()
            .map(acp -> new AuthorContributeProto(acp.getAuthor(), acp.getContribute()))
            .collect(Collectors.toList()));
  }
}
