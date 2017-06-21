package vn.com.aris.jenkinPlugin;

import hudson.ExtensionList;
import hudson.ExtensionPoint;
import hudson.Functions;
import hudson.model.Action;
import jenkins.model.Jenkins;


public abstract class AbstractAction implements ExtensionPoint, Action {

    @Override
    public String getIconFileName() {
        return getClass().getSimpleName();
    }

    @Override
    public String getDisplayName() {
        return getClass().getSimpleName();
    }

    @Override
    public String getUrlName() {
        return getClass().getSimpleName();
    }
    
//    
    public abstract String getUrl();
    
    /**
     * Returns all the registered {@link UISample}s.
     */
    public static ExtensionList<AbstractAction> all() {
        return Jenkins.getInstance().getExtensionList(AbstractAction.class);
    }

    public String getResourcePath() {
        return Functions.getResourcePath();
    }

}

