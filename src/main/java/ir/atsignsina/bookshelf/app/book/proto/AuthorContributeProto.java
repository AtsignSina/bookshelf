package ir.atsignsina.bookshelf.app.book.proto;

import ir.atsignsina.bookshelf.app.author.Author;
import ir.atsignsina.bookshelf.app.contribute.ContributeType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AuthorContributeProto {
  private Author author;
  private ContributeType contribute;
}
