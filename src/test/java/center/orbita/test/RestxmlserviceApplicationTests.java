package center.orbita.test;

import static org.mockito.Mockito.*;
import javax.naming.Context;
import org.junit.Rule;
import org.junit.Test;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RestxmlserviceApplicationTests {
	private final Context context = mock(Context.class);

    @Rule
    public MockInitialContextRule mockInitialContextRule = new MockInitialContextRule(context);

	
	@Test
	void contextLoads() {
	}

}