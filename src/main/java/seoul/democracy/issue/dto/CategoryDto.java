package seoul.democracy.issue.dto;

import com.mysema.query.types.Projections;
import com.mysema.query.types.QBean;
import lombok.Data;

import static seoul.democracy.issue.domain.QCategory.category;

@Data
public class CategoryDto {

    public final static QBean<CategoryDto> projection = Projections.fields(CategoryDto.class,
        category.id, category.name, category.enabled, category.sequence);

    public final static QBean<CategoryDto> projectionForFilter = Projections.fields(CategoryDto.class,
        category.name);

    private Long id;
    private String name;
    private Boolean enabled;
    private Integer sequence;
}
