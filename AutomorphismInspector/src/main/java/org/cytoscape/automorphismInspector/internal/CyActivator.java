package org.cytoscape.automorphismInspector.internal;

import java.util.Properties;
import org.cytoscape.automorphismInspector.internal.task.CreateUiTaskFactory;
import org.cytoscape.service.util.AbstractCyActivator;
import org.cytoscape.service.util.CyServiceRegistrar;
import static org.cytoscape.work.ServiceProperties.PREFERRED_MENU;
import static org.cytoscape.work.ServiceProperties.TITLE;
import org.cytoscape.work.TaskFactory;
import org.osgi.framework.BundleContext;

/**
 * @author SrikanthB
 *
 */


public class CyActivator extends AbstractCyActivator {
    
    public CyActivator() {
        super();
    }
    
    public void start(BundleContext context) throws Exception {
        final CyServiceRegistrar serviceRegistrar = getService(context, CyServiceRegistrar.class);
        final CreateUiTaskFactory createUiTaskFactory = new CreateUiTaskFactory(serviceRegistrar);
        
        Properties prorankProps = new Properties();
        prorankProps.setProperty(PREFERRED_MENU, "Apps");
        prorankProps.setProperty(TITLE, "CyAutomorphism");
        registerService(context, createUiTaskFactory, TaskFactory.class, prorankProps);
    }

}