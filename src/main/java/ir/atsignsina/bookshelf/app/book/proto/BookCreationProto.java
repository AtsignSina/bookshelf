package ir.atsignsina.bookshelf.app.book.proto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BookCreationProto {
  private String name;
  private String isbn;
  private Long publisher;
  private List<Long> categories;
  private List<AuthorBookContributeProto> contributes;
  private Integer price;
}
