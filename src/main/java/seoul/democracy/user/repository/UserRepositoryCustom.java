package seoul.democracy.user.repository;

import com.mysema.query.types.Expression;
import com.mysema.query.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import seoul.democracy.user.dto.UserDto;

public interface UserRepositoryCustom {

    Page<UserDto> findAll(Predicate predicate, Pageable pageable, Expression<UserDto> projection);

    UserDto findOne(Predicate predicate, Expression<UserDto> projection);
}
