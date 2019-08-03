package ir.atsignsina.bookshelf.app.author.proto;

import ir.atsignsina.bookshelf.app.book.Book;
import ir.atsignsina.bookshelf.app.contribute.ContributeType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BookContribute {
  private Book book;
  private ContributeType contribute;
}
