package ir.atsignsina.bookshelf.app.category;

import ir.atsignsina.bookshelf.app.category.proto.CategoryCreationProto;
import ir.atsignsina.bookshelf.concerns.exception.data.DataNotFoundException;
import ir.atsignsina.bookshelf.concerns.exception.data.RequirementDataNotFoundException;
import ir.atsignsina.bookshelf.concerns.exception.request.RequestBodyException;
import ir.atsignsina.bookshelf.concerns.utils.NilUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
  private CategoryRepository categoryRepository;

  public CategoryService(CategoryRepository categoryRepository) {
    this.categoryRepository = categoryRepository;
  }

  /**
   * check request body and create category.
   *
   * @param categoryCreationProto request body
   * @return saved category
   */
  Category createCategory(CategoryCreationProto categoryCreationProto) {
    checkCreationProto(categoryCreationProto);
    Category category = makeCategoryFromProto(categoryCreationProto);
    return categoryRepository.save(category);
  }

  /**
   * Check category creation prototype for ensure request body met all requirements
   *
   * @param categoryCreationProto request body prototype
   */
  private void checkCreationProto(CategoryCreationProto categoryCreationProto) {
    if (NilUtils.checkNullOrEmpty(categoryCreationProto.getName())) {
      throw new RequestBodyException("NAME_IS_EMPTY");
    }
  }

  /**
   * find parent based on parentId.
   *
   * @param parentId parent's id
   * @return found parent in subcategories or null in root categories
   */
  private Category findParent(Long parentId) {
    if (parentId != null && parentId != 0) { // sub category
      Optional<Category> parentOptional = categoryRepository.findById(parentId);
      if (parentOptional.isPresent()) {
        return parentOptional.get();
      } else {
        throw new RequirementDataNotFoundException("PARENT_NOT_FOUND");
      }
    } else { // root category
      return null;
    }
  }

  /**
   * @param categoryCreationProto request body object which should validate before using this method
   * @return created category
   */
  private Category makeCategoryFromProto(CategoryCreationProto categoryCreationProto) {
    Category category = new Category();
    category.setName(categoryCreationProto.getName());
    category.setParent(findParent(categoryCreationProto.getParent()));
    return category;
  }

  /**
   * Find category from database based on input id
   *
   * @param id category id
   * @return found category
   */
  Category getCategory(Long id) {
    Optional<Category> category = categoryRepository.findById(id);
    if (category.isPresent()) {
      return category.get();
    } else {
      throw new DataNotFoundException("CATEGORY_NOT_FOUND");
    }
  }

  /**
   * Search in categories with name containing in specified page
   *
   * @param name string will be query in name attribute of all categories. If name is null or empty
   *     this method returns all categories without specified where clause.
   * @param pageable returning page
   * @return page of found categories
   */
  Page<Category> searchCategories(String name, Pageable pageable) {
    return (name == null || name.isEmpty())
        ? categoryRepository.findAll(pageable)
        : categoryRepository.findByNameContaining(name, pageable);
  }

  /** Delete all categories from database */
  void deleteCategories() {
    categoryRepository.deleteAll();
  }

  /**
   * Delete specified category from database
   *
   * @param id category's id
   */
  void deleteCategory(Long id) {
    categoryRepository.delete(getCategory(id));
  }

  /**
   * Edit category with request body
   *
   * @param id category's id
   * @param categoryForEdit request body of newly object
   * @return edited category
   */
  Category editCategory(Long id, Category categoryForEdit) {
    Category category = getCategory(id);
    if (!NilUtils.checkNullOrEmpty(categoryForEdit.getName())) {
      category.setName(categoryForEdit.getName());
    }
    return categoryRepository.save(category);
  }

  /**
   * find a list of categories by their id
   *
   * @param ids list of ids
   * @return list of categories. If some id does not exist so there are not presented in returning
   *     list. So returning list size may differ with input list size
   */
  public List<Category> findByIdIn(List<Long> ids) {
    return categoryRepository.findByIdIn(ids);
  }
}
