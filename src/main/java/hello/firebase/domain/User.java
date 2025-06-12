package hello.firebase.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="test")
@Getter
@Setter
public class User {

    @Id @Column(columnDefinition = "VARCHAR(10)") //@GeneratedValue(strategy=GenerationType.IDENTITY)
    private String user;

    @Column(name="age")
    private int age;

    @Column
    private int phone;

    @Column(columnDefinition = "VARCHAR(30)")
    private String email;

    @Column(columnDefinition = "VARCHAR(100)")
    private String role;
}
