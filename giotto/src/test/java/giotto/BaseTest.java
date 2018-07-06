package giotto;

import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * BaseTest
 *
 * @author whb
 * @date 2017/4/28
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Giotto.class})
@WebIntegrationTest
@ActiveProfiles("test")
public class BaseTest {
}
