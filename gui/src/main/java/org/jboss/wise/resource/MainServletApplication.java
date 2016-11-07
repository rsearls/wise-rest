package org.jboss.wise.resource;

import java.util.Collections;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * User: rsearls
 * Date: 11/4/16
 */
@ApplicationPath("")
public class MainServletApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        return Collections.emptySet();
    }
}
