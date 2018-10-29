package seoul.democracy.user.service;

import com.mysema.query.types.Predicate;
import com.mysema.query.types.QBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import seoul.democracy.common.exception.AlreadyExistsException;
import seoul.democracy.common.exception.NotFoundException;
import seoul.democracy.user.domain.User;
import seoul.democracy.user.dto.UserCreateDto;
import seoul.democracy.user.dto.UserDto;
import seoul.democracy.user.dto.UserUpdateDto;
import seoul.democracy.user.repository.UserRepository;
import seoul.democracy.user.utils.UserUtils;

import static seoul.democracy.user.predicate.UserPredicate.equalEmail;

@Service
@Transactional(readOnly = true)
public class UserService {

    final private UserRepository userRepository;
    final private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDto getUser(Predicate predicate, QBean<UserDto> projection) {
        return userRepository.findOne(predicate, projection);
    }

    public Page<UserDto> getUsers(Predicate predicate, Pageable pageable, QBean<UserDto> projection) {
        return userRepository.findAll(predicate, pageable, projection);
    }

    private User getUser(String email) {
        User user = userRepository.findOne(equalEmail(email));
        if (user == null)
            throw new NotFoundException("회원 정보를 찾을 수 없습니다.");

        return user;
    }

    public User getMe() {
        return getUser(UserUtils.getEmail());
    }

    @Transactional
    public User update(UserUpdateDto updateDto) {
        User user = getMe();

        return user.update(updateDto);
    }

    @Transactional
    public User join(UserCreateDto createDto) {

        if (existsEmail(createDto.getEmail()))
            throw new AlreadyExistsException("이미 사용중인 이메일입니다.");

        User user = User.create(createDto);

        return userRepository.save(user);
    }

    boolean existsEmail(String email) {
        return userRepository.exists(equalEmail(email.trim()));
    }
}
