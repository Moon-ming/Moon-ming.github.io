import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.moomin.MySpringBootApplication;
import io.moomin.domain.User;
import io.moomin.respository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MySpringBootApplication.class)
public class RedisTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Test
    public void test() throws JsonProcessingException {
        String userListJson = redisTemplate.boundValueOps("user.findAll").get();
        if (null == userListJson) {
            List<User> all = userRepository.findAll();
            ObjectMapper objectMapper = new ObjectMapper();
            userListJson = objectMapper.writeValueAsString(all);
            redisTemplate.boundValueOps("user.findAll").set(userListJson);
            System.out.println("sql...");
        } else {
            System.out.println("redis");

        }
        System.out.println(userListJson);
    }
}
