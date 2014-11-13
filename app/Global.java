import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.surevine.gateway.auditing.AuditService;
import com.surevine.gateway.auditing.LogfileAuditServiceImpl;

import play.*;

public class Global extends GlobalSettings {

	private Injector injector;

	@Override
    public void onStart(Application application) {
        injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
            	// TODO load class depending on configuration option (application.classloader().loadClass(String name))
                bind(AuditService.class).to(LogfileAuditServiceImpl.class);
            }
        });
    }

    @Override
    public <T> T getControllerInstance(Class<T> aClass) throws Exception {
        return injector.getInstance(aClass);
    }

}
