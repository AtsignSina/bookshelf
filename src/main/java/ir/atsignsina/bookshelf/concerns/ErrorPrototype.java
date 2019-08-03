package ir.atsignsina.bookshelf.concerns;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
class ErrorPrototype {
  private String code;
  private String message;
  private Date time;
}
