package com.bankcomm.laptev.project;

import java.io.IOException;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
import org.eclipse.ui.internal.console.ConsolePluginImages;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.bankcomm.laptev.project";

	// The shared instance
	private static Activator plugin;
	
	/**
	 * The constructor
	 */
	public Activator() {
	}

	private MessageConsole console;
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;

		console = new MessageConsole("replace for system.out", null);
		IConsoleManager manager = ConsolePlugin.getDefault().getConsoleManager();
		manager.addConsoles(new IConsole[] {console});
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}
	
	/**
     * Returns the <code>Image</code> identified by the given key,
     * or <code>null</code> if it does not exist.
     * 
     * @return the <code>Image</code> identified by the given key,
     * or <code>null</code> if it does not exist
     * @since 3.1
     */
    public static Image getImage(String key) {
        return ConsolePluginImages.getImage(key);
    }
    
    /**
     * Returns the <code>ImageDescriptor</code> identified by the given key,
     * or <code>null</code> if it does not exist.
     * 
     * @return the <code>ImageDescriptor</code> identified by the given key,
     * or <code>null</code> if it does not exist
     * @since 3.1
     */
    public static ImageDescriptor getImageDescriptor(String path) {
    	return imageDescriptorFromPlugin(PLUGIN_ID, path);
    }
	
	public void log(String message) {
		MessageConsoleStream os =  console.newMessageStream();
		os.println(message);
		try {
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
