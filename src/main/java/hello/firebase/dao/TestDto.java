package hello.firebase.dao;

import com.querydsl.jpa.impl.JPAQueryFactory;
import hello.firebase.domain.User;
import hello.firebase.dto.UserDto;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class TestDto {

    EntityManager em;
    //JPAQueryFactory query;

    @Autowired
    public TestDto(EntityManager em) {
        this.em=em;
        //this.query=query;
    }

    public User save(User user){
        em.persist(user);
        return user;
    }

    public Optional<User> find(String user){
        return em.createQuery("select u from User u where u.user=:user",User.class)
                .setParameter("user",user)
                .getResultList()
                .stream().findFirst();
    }

    public Optional<User>findByIdentifier(String identifier){
        return em.createQuery("select u from User u where u.email=:email",User.class)
                .setParameter("email",identifier)
                .getResultList()
                .stream().findFirst();
    }


}
