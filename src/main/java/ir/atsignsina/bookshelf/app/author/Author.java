package ir.atsignsina.bookshelf.app.author;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Author {
  @JsonIgnore @Id @GeneratedValue private Long id;
  private String name;

  @JsonProperty
  public Long getId() {
    return this.id;
  }
}
