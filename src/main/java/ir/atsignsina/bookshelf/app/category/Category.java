package ir.atsignsina.bookshelf.app.category;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ir.atsignsina.bookshelf.app.book.Book;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Category {
  @Id @GeneratedValue private Long id;
  private String name;
  @JsonIgnore @ManyToOne private Category parent;

  @JsonIgnore
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private Set<Category> children;

  @JsonIgnore
  @ManyToMany
  @JoinTable(
      name = "BOOK_CATEGORIES",
      joinColumns = {@JoinColumn(name = "CATEGORIES_ID")},
      inverseJoinColumns = {@JoinColumn(name = "BOOK_ID")})
  Set<Book> books;
}
