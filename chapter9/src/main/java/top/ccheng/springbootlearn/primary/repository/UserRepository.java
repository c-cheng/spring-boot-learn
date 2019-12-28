package top.ccheng.springbootlearn.primary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import top.ccheng.springbootlearn.primary.entity.User;

/**
 * @author ccheng
 * @date 2018/9/10
 */
public interface UserRepository extends JpaRepository<User, Long> {
}
