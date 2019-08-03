package ir.atsignsina.bookshelf.app.book;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import ir.atsignsina.bookshelf.app.book.proto.AuthorContributeProto;
import ir.atsignsina.bookshelf.app.category.Category;
import ir.atsignsina.bookshelf.app.contribute.Contribute;
import ir.atsignsina.bookshelf.app.publisher.Publisher;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Book {
  @Id @GeneratedValue private Long id;
  private String name;
  private String isbn;
  @ManyToOne private Publisher publisher;

  @ManyToMany
  @JoinTable(
      name = "BOOK_CATEGORIES",
      joinColumns = {@JoinColumn(name = "BOOK_ID")},
      inverseJoinColumns = {@JoinColumn(name = "CATEGORIES_ID")})
  private Set<Category> categories;

  @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
  @JsonIgnore
  private Set<Contribute> contributes = new HashSet<>();

  @Transient @JsonIgnore private List<AuthorContributeProto> authors = new ArrayList<>();
  private Integer price;

  @JsonProperty
  public List<AuthorContributeProto> getAuthors() {
    return authors;
  }
}
