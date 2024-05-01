package club.c1sec.c1ctfplatform.services;

import club.c1sec.c1ctfplatform.dao.UserDao;
import club.c1sec.c1ctfplatform.po.User;
import club.c1sec.c1ctfplatform.vo.Ranking.RankingInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    UserDao userDao;

    public List<RankingInfo> findAllRankingInfo() {
        return userDao.findAllRankingInfo();
    }

    public Boolean isUsernameExist(String username) {
        return userDao.existsByUsername(username);
    }

    public Boolean isStudentIdExist(String studentId) {
        return userDao.existsByStudentId(studentId);
    }

    public Boolean isEmailExist(String email) {
        return this.userDao.existsByEmail(email);
    }

    public void addUser(User user) {
        userDao.save(user);
    }

    public User getUserByUserId(Long userId) {
        return this.userDao.findUserByUserId(userId);
    }

    public User getUserByUsername(String username) {
        return this.userDao.findUserByUsername(username);
    }

    public User getUserByEmail(String email) {
        return this.userDao.findUserByEmail(email);
    }

    public void banUser(Long userId) {
        User user = this.getUserByUserId(userId);
        user.setBanned(true);
        this.addUser(user);
    }

    public void unbanUser(Long userId) {
        User user = this.getUserByUserId(userId);
        user.setBanned(false);
        this.addUser(user);
    }

    public List<User> getAllUser() {
        return userDao.findAll();
    }
}
