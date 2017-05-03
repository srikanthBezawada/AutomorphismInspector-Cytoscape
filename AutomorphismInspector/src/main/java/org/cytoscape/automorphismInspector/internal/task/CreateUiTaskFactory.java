package org.cytoscape.automorphismInspector.internal.task;

import org.cytoscape.service.util.CyServiceRegistrar;
import org.cytoscape.work.TaskFactory;
import org.cytoscape.work.TaskIterator;

public class CreateUiTaskFactory implements TaskFactory{
    public final CyServiceRegistrar cyRegistrar;
    
    public CreateUiTaskFactory(CyServiceRegistrar cyRegistrar) {
        this.cyRegistrar = cyRegistrar;
    }
    
    @Override
    public TaskIterator createTaskIterator() {
        return new TaskIterator(new CreateUiTask(cyRegistrar));
    }

    @Override
    public boolean isReady() {
        return true;
    }
}
