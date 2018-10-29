package seoul.democracy.issue.service;


import com.mysema.query.types.Expression;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import seoul.democracy.common.exception.AlreadyExistsException;
import seoul.democracy.common.exception.NotFoundException;
import seoul.democracy.issue.domain.Category;
import seoul.democracy.issue.dto.CategoryCreateDto;
import seoul.democracy.issue.dto.CategoryDto;
import seoul.democracy.issue.dto.CategoryUpdateDto;
import seoul.democracy.issue.repository.CategoryRepository;

import static seoul.democracy.issue.predicate.CategoryPredicate.equalId;
import static seoul.democracy.issue.predicate.CategoryPredicate.equalName;

@Service
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public CategoryDto getCategory(Predicate predicate, Expression<CategoryDto> projection) {
        return categoryRepository.findOne(predicate, projection);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public Category create(CategoryCreateDto createDto) {
        Category category = Category.create(createDto);

        if (categoryRepository.exists(equalName(createDto.getName())))
            throw new AlreadyExistsException("이미 존재하는 카테고리입니다.");

        return categoryRepository.save(category);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public Category update(CategoryUpdateDto updateDto) {
        Category category = categoryRepository.findOne(equalId(updateDto.getId()));
        if (category == null)
            throw new NotFoundException("존재하지 않는 카테고리입니다.");

        if (!category.getName().equals(updateDto.getName()) && categoryRepository.exists(equalName(updateDto.getName())))
            throw new AlreadyExistsException("이미 존재하는 카테고리입니다.");

        return category.update(updateDto);
    }
}
