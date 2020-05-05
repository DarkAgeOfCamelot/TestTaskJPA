package center.orbita.test;

import javax.naming.Context;

import org.apache.myfaces.mc.test.core.mock.MockInitialContextFactory;
import org.junit.runners.model.Statement;
import org.mockito.internal.verification.Description;

public class MockInitialContextRule  {

    private final Context context;

    public MockInitialContextRule(Context context) {
        this.context = context;
    }

    public Statement apply(final Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                System.setProperty(Context.INITIAL_CONTEXT_FACTORY, MockInitialContextFactory.class.getName());
                MockInitialContextFactory.setCurrentContext(context);
                try {
                    base.evaluate();
                } finally {
                    System.clearProperty(Context.INITIAL_CONTEXT_FACTORY);
                    MockInitialContextFactory.clearCurrentContext();
                }
            }
        };
    }
}