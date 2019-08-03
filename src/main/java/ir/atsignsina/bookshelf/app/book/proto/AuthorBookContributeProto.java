package ir.atsignsina.bookshelf.app.book.proto;

import ir.atsignsina.bookshelf.app.contribute.ContributeType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AuthorBookContributeProto {
  private Long author;
  private ContributeType contribute;
}
