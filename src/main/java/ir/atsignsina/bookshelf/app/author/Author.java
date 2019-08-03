package ir.atsignsina.bookshelf.app.author;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import ir.atsignsina.bookshelf.app.contribute.Contribute;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Author {
  @JsonIgnore @Id @GeneratedValue private Long id;
  private String name;

  @JsonIgnore
  @OneToMany(mappedBy = "author")
  private Set<Contribute> contributes;

  @JsonProperty
  public Long getId() {
    return this.id;
  }
}
