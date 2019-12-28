package top.ccheng.springbootlearn;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author ccheng
 * @date 2018/9/13
 */
public interface UserRepository extends MongoRepository<User, Long> {

    User findByUsername(String username);

}
