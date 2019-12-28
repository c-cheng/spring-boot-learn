package top.ccheng.springbootlearn.second.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import top.ccheng.springbootlearn.second.entity.Message;

/**
 * @author ccheng
 * @date 2018/9/10
 */
public interface MessageRepository extends JpaRepository<Message, Long> {
}
