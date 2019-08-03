package ir.atsignsina.bookshelf.app.publisher;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import ir.atsignsina.bookshelf.app.book.Book;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = "name")})
@NoArgsConstructor
@Getter
@Setter
public class Publisher {
  @JsonIgnore @Id @GeneratedValue private Long id;
  private String name;
  private String description;

  @JsonIgnore
  @OneToMany(mappedBy = "publisher")
  private Set<Book> books;

  @JsonProperty
  public Long getId() {
    return this.id;
  }
}
