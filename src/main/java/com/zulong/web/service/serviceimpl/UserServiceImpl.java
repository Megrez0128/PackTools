package com.zulong.web.service.serviceimpl;

import com.zulong.web.dao.UserDao;
import com.zulong.web.entity.User;
import com.zulong.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private UserDao userDao;

    /**
     * TODO：尝试写一个旁路缓存的模型，可能会重构
     * @param id
     * @return
     */
    @Override
    @Cacheable(value = "users", key = "id")
    public User getUserByID(String id){
        // 从缓存中获取数据
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        if(redisTemplate.hasKey(id)){
            return (User)operations.get(id);
        }

        // 从数据库中获取数据
        User user = userDao.findByUserID(id);
        // 将数据存入缓存
        operations.set(id, user);
        return user;

    }
}
