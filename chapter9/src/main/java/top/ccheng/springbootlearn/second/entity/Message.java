package top.ccheng.springbootlearn.second.entity;

import javax.persistence.*;

/**
 * @author ccheng
 * @date 2018/9/9
 */
@Table(name = "message_4")
@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", length = 20, nullable = false, unique = true, insertable = true, updatable = true)
    private Long id;

    @Column(name = "name", length = 32, nullable = false, unique = false, insertable = true, updatable = true)
    private String name;

    @Column(name = "content", length = 255, nullable = false, unique = false, insertable = true, updatable = true)
    private String content;

    public Message() {
    }

    public Message(String name, String content) {
        this.name = name;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
