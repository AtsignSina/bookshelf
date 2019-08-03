package ir.atsignsina.bookshelf.app.publisher;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PublisherServiceTest {
  @Autowired PublisherService publisherService;
  private Publisher publisher = new Publisher();

  @Before
  public void _createPublisher() {
    publisher = new Publisher();
    publisher.setName("PublisherServiceTest");
    publisherService.createPublisher(publisher);
  }

  @After
  public void _deletePublisher() {
    publisherService.deletePublisher(publisher.getId());
  }

  @Test
  public void createPublisher() {
    Assert.assertNotNull(publisher.getId());
  }

  @Test
  public void findById() {
    Assert.assertTrue(publisherService.findById(publisher.getId()).isPresent());
  }

  @Test
  public void getPublisher() {
    Assert.assertNotNull(publisherService.getPublisher(publisher.getId()));
  }

  @Test
  public void searchPublishers() {
    Assert.assertEquals(
        1, publisherService.searchPublishers("Test", PageRequest.of(0, 20)).getTotalElements());
  }

  @Test
  public void deletePublisher() {
    publisherService.deletePublisher(publisher.getId());
    Assert.assertNull(publisher.getId());
  }

  @Test
  public void deletePublishers() {
    publisherService.deletePublishers();
    Assert.assertFalse(publisherService.findById(publisher.getId()).isPresent());
  }

  @Test
  public void editPublisher() {
    Publisher p = new Publisher();
    p.setName("TestEdit");
    p = publisherService.editPublisher(publisher.getId(), p);
    Assert.assertEquals("TestEdit", p.getName());
  }

  @Test
  public void getPublisherBooks() {
    // since there are no book for this publisher, so the set contains no book
    Assert.assertTrue(publisherService.getPublisherBooks(publisher.getId()).isEmpty());
  }
}
