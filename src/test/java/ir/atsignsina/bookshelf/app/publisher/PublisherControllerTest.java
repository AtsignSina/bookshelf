package ir.atsignsina.bookshelf.app.publisher;

import ir.atsignsina.bookshelf.app.book.Book;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.LinkedHashMap;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PublisherControllerTest {

  @LocalServerPort private int port;

  @Autowired private TestRestTemplate restTemplate;
  private Publisher publisher = new Publisher();

  @Before
  public void CreatePublisher() {
    publisher = new Publisher();
    publisher.setName("TestForDelete");
    ResponseEntity<Publisher> create =
        this.restTemplate.exchange(
            "http://localhost:" + port + "/publisher",
            HttpMethod.POST,
            new HttpEntity<>(publisher, new HttpHeaders()),
            Publisher.class);
    publisher = create.getBody();
  }

  @After
  public void deletePublisher() {
    this.restTemplate.exchange(
        "http://localhost:" + port + "/publisher/" + publisher.getId(),
        HttpMethod.DELETE,
        new HttpEntity<>(new HttpHeaders()),
        String.class);
  }

  @Test
  public void createPublisher() {
    Assert.assertNotNull(publisher.getId());
  }

  @Test
  public void searchPublishers() {
    ResponseEntity<LinkedHashMap> search =
        this.restTemplate.exchange(
            "http://localhost:" + port + "/publisher?name=Test",
            HttpMethod.GET,
            new HttpEntity<>(new HttpHeaders()),
            LinkedHashMap.class);
    Assert.assertTrue(
        search.getStatusCodeValue() == 200
            && search.getBody() != null
            && search.getBody().get("totalElements").equals(1));
  }

  @Test
  public void deletePublishers() {
    // Because we have seed data in our database, so cannot delete publishers. In this case database
    // integrity error will raise and we check it for ensure
    ResponseEntity deleteAll =
        this.restTemplate.exchange(
            "http://localhost:" + port + "/publisher",
            HttpMethod.DELETE,
            new HttpEntity<>(new HttpHeaders()),
            Object.class);
    Assert.assertEquals(409, deleteAll.getStatusCodeValue()); // conflict
  }

  @Test
  public void getPublisherIsExist() {
    Publisher p =
        this.restTemplate.getForObject(
            "http://localhost:" + port + "/publisher/" + publisher.getId(), Publisher.class);
    Assert.assertEquals(publisher.getId(), p.getId());
  }

  @Test
  public void getPublisherIsNotExist() {
    Assert.assertEquals(
        this.restTemplate
            .getForEntity("http://localhost:" + port + "/publisher/0", String.class)
            .getStatusCodeValue(),
        204);
  }

  @Test
  public void deletePublisherIsExist() {
    ResponseEntity<String> delete =
        this.restTemplate.exchange(
            "http://localhost:" + port + "/publisher/" + publisher.getId(),
            HttpMethod.DELETE,
            new HttpEntity<>(new HttpHeaders()),
            String.class);
    Assert.assertEquals(202, delete.getStatusCodeValue());
  }

  @Test
  public void deletePublisherIsNotExist() {
    ResponseEntity<String> delete =
        this.restTemplate.exchange(
            "http://localhost:" + port + "/publisher/0",
            HttpMethod.DELETE,
            new HttpEntity<>(new HttpHeaders()),
            String.class);
    Assert.assertEquals(204, delete.getStatusCodeValue());
  }

  @Test
  public void editPublisherIfExists() {
    Publisher p = new Publisher();
    p.setName("TestEdit");
    ResponseEntity<Publisher> edit =
        this.restTemplate.exchange(
            "http://localhost:" + port + "/publisher/" + publisher.getId(),
            HttpMethod.PUT,
            new HttpEntity<>(p, new HttpHeaders()),
            Publisher.class);
    Assert.assertEquals(200, edit.getStatusCodeValue());
    Assert.assertNotNull(edit.getBody());
    Assert.assertEquals("TestEdit", edit.getBody().getName());
  }

  @Test
  public void editPublisherIfNotExists() {
    Publisher p = new Publisher();
    p.setName("TestEdit");
    ResponseEntity<Publisher> edit =
        this.restTemplate.exchange(
            "http://localhost:" + port + "/publisher/0",
            HttpMethod.PUT,
            new HttpEntity<>(p, new HttpHeaders()),
            Publisher.class);
    Assert.assertEquals(204, edit.getStatusCodeValue());
  }

  @Test
  public void getPublisherBooksIfExists() {
    ResponseEntity<List<Book>> get =
        this.restTemplate.exchange(
            "http://localhost:" + port + "/publisher/" + publisher.getId() + "/book",
            HttpMethod.GET,
            new HttpEntity<>(new HttpHeaders()),
            new ParameterizedTypeReference<List<Book>>() {});
    Assert.assertEquals(200, get.getStatusCodeValue());
    Assert.assertNotNull(get.getBody());
    Assert.assertTrue(get.getBody().isEmpty());
  }

  @Test
  public void getPublisherBooksIfNotExists() {
    ResponseEntity<List<Book>> edit =
        this.restTemplate.exchange(
            "http://localhost:" + port + "/publisher/0/book",
            HttpMethod.GET,
            new HttpEntity<>(new HttpHeaders()),
            new ParameterizedTypeReference<List<Book>>() {});
    Assert.assertEquals(204, edit.getStatusCode().value());
  }

  @TestConfiguration
  public static class ContradictionRangeServiceImplTestConfiguration {
    @Bean
    public TestRestTemplate testRestTemplate(RestTemplateBuilder restTemplateBuilder) {
      return new TestRestTemplate(restTemplateBuilder);
    }
  }
}
