package ir.atsignsina.bookshelf.app.contribute;

import ir.atsignsina.bookshelf.app.author.Author;
import ir.atsignsina.bookshelf.app.book.Book;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Contribute {
  @Id @GeneratedValue private Long id;
  @ManyToOne private Author author;
  private ContributeType contribute;
  @ManyToOne private Book book;
}
