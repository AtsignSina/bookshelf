package ir.atsignsina.bookshelf.app.category;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
}
