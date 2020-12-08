import io.moomin.MySpringBootApplication;
import io.moomin.domain.User;
import io.moomin.respository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MySpringBootApplication.class)
public class JpaTest {
    @Autowired
    private UserRepository userRepository;
    @Test
    public void test() {
        List<User> all = userRepository.findAll();
        System.out.println(all);
    }
}
