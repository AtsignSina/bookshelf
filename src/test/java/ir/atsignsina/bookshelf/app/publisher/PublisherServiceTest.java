package ir.atsignsina.bookshelf.app.publisher;

import ir.atsignsina.bookshelf.app.book.Book;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PublisherServiceTest {
  private Publisher publisher = new Publisher();

  @Mock private PublisherRepository publisherRepository;
  @InjectMocks private PublisherService publisherService;
  @Rule public ExpectedException thrown = ExpectedException.none();

  @Before
  public void publisher() {
    publisher = new Publisher();
    publisher.setId(1L);
    publisher.setDescription("new description");
    publisher.setName("Pub1");
  }

  @Test
  public void createPublisher() {
    when(publisherRepository.save(any(Publisher.class))).thenReturn(publisher);
    Publisher publisherBooks = publisherService.createPublisher(publisher);
    Assert.assertNotNull(publisherBooks);
    Assert.assertEquals(publisher.getName(), publisherBooks.getName());
  }

  @Test
  public void createPublisherIfNameIsOkButDescription() {
    publisher.setDescription(null);
    when(publisherRepository.save(any(Publisher.class))).thenReturn(publisher);
    Publisher publisherBooks = publisherService.createPublisher(publisher);
    Assert.assertNotNull(publisherBooks);
    Assert.assertEquals(publisher.getName(), publisherBooks.getName());
  }

  @Test
  public void createPublisherIfNameIsNull() {
    publisher.setName(null);
    thrown.expect(RuntimeException.class);
    thrown.expectMessage("NAME_IS_EMPTY");
    publisherService.createPublisher(publisher);
  }

  @Test
  public void findByIdReturnPresentOptional() {
    when(publisherRepository.findById(1L)).thenReturn(Optional.of(publisher));
    Optional<Publisher> publisherOptional = publisherService.findById(1L);
    Assert.assertTrue(publisherOptional.isPresent());
    Assert.assertEquals(1L, publisherOptional.get().getId().longValue());
  }

  @Test
  public void findByIdReturnEmptyOptional() {
    when(publisherRepository.findById(1L)).thenReturn(Optional.of(publisher));
    Optional<Publisher> publisherOptional = publisherService.findById(0L);
    Assert.assertFalse(publisherOptional.isPresent());
  }

  @Test
  public void getPublisherIfExists() {
    when(publisherRepository.findById(1L)).thenReturn(Optional.of(publisher));
    Publisher pub = publisherService.getPublisher(1L);
    Assert.assertNotNull(pub);
    Assert.assertEquals(1L, pub.getId().longValue());
  }

  @Test
  public void getPublisherIfNotExists() {
    when(publisherRepository.findById(1L)).thenReturn(Optional.of(publisher));
    thrown.expect(RuntimeException.class);
    thrown.expectMessage("PUBLISHER_NOT_FOUND");
    publisherService.getPublisher(0L);
  }

  @Test
  public void searchPublishersWithoutName() {
    Page<Publisher> publishers = new PageImpl<>(new ArrayList<>());
    when(publisherRepository.findAll(any(Pageable.class))).thenReturn(publishers);
    Page<Publisher> pg = publisherService.searchPublishers(null, PageRequest.of(0, 20));
    Assert.assertNotNull(pg);
    Assert.assertEquals(0, pg.getTotalElements());
  }

  @Test
  public void searchPublishers() {
    Page<Publisher> publishers = new PageImpl<>(new ArrayList<>());
    when(publisherRepository.findByNameContaining(any(), any())).thenReturn(publishers);
    Page<Publisher> pg = publisherService.searchPublishers("Pg", PageRequest.of(0, 20));
    Assert.assertNotNull(pg);
    Assert.assertEquals(0, pg.getTotalElements());
  }

  @Test
  public void editPublisherIfExists() {
    when(publisherRepository.findById(1L)).thenReturn(Optional.of(publisher));
    Publisher newPub = new Publisher();
    newPub.setName("EditedPub");
    when(publisherRepository.save(any(Publisher.class))).thenReturn(publisher);
    Publisher publisher = publisherService.editPublisher(1L, newPub);
    Assert.assertNotNull(publisher);
    Assert.assertEquals(this.publisher.getName(), publisher.getName());
  }

  @Test
  public void editPublisherIfNotExists() {
    when(publisherRepository.findById(1L)).thenReturn(Optional.of(publisher));
    thrown.expect(RuntimeException.class);
    thrown.expectMessage("PUBLISHER_NOT_FOUND");
    publisherService.editPublisher(0L, new Publisher());
  }

  @Test
  public void getPublisherBooksIfExists() {
    Set<Book> books = new HashSet<>();
    Book b1 = new Book();
    b1.setId(1L);
    books.add(b1);
    Book b2 = new Book();
    b2.setId(2L);
    books.add(b2);
    publisher.setBooks(books);
    when(publisherRepository.findById(1L)).thenReturn(Optional.of(publisher));
    Set<Book> bookSet = publisherService.getPublisherBooks(1L);
    Assert.assertNotNull(bookSet);
    Assert.assertEquals(2, bookSet.size());
  }

  @Test
  public void getPublisherBooksIfExistsButEmpty() {
    when(publisherRepository.findById(1L)).thenReturn(Optional.of(publisher));
    Set<Book> bookSet = publisherService.getPublisherBooks(1L);
    Assert.assertNull(bookSet);
  }

  @Test
  public void getPublisherBooksIfNotExists() {
    when(publisherRepository.findById(1L)).thenReturn(Optional.of(publisher));
    thrown.expect(RuntimeException.class);
    thrown.expectMessage("PUBLISHER_NOT_FOUND");
    publisherService.getPublisherBooks(0L);
  }
}
