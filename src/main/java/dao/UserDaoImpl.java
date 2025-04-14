package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jdk.jfr.Percentage;
import model.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {

    @Percentage
    private EntityManager em;

    @Override
    public List<User> getAllUsers() {
        return em
                .createQuery("select u from User u", User.class)
                .getResultList();
    }

    @Override
    public User getUser(int id) {
        User user = em.find(User.class, id);
        if (user == null) {
            throw new EntityNotFoundException(
                    "User with id: " + id + " not found"
            );
        }
        return user;
    }

    @Override
    public void saveUser(User user) {
        em.persist(user);
    }

    @Override
    public void updateUser(User user) {
        getUser(user.getId());
        em.merge(user);
    }

    @Override
    public void deleteUser(int id) {
        User user = getUser(id);
        em.remove(user);
    }
}
