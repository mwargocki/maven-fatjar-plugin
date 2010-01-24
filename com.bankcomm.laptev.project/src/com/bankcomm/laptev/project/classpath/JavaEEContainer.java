package com.bankcomm.laptev.project.classpath;

import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.JavaCore;

public class JavaEEContainer implements IClasspathContainer {
	
	private final static String SDK_BASE_DIR = "D:/JavaEE SDK";
	
	private String version = "5.0";

	private JavaEEContainer(String version) {
		this.version = version;
	}

	public String getDescription() {
		return MessageFormat.format("JavaEE Libraries [J2EE-{0}]", version);
	}

	public int getKind() {
		return IClasspathContainer.K_SYSTEM;
	}

	public IPath getPath() {
		return null;
	}

	public IClasspathEntry[] getClasspathEntries() {
		File versionedSdkDirectory = new File(SDK_BASE_DIR, version);
		
		if(!versionedSdkDirectory.exists() || !versionedSdkDirectory.isDirectory()) {
			return new IClasspathEntry[]{};
		}
		
		List<IClasspathEntry> entries = new ArrayList<IClasspathEntry>();
		for(File jar : versionedSdkDirectory.listFiles()) {
			entries.add(JavaCore.newLibraryEntry(Path.fromOSString(jar.getAbsolutePath()), null, null));
		}
		return entries.toArray(new IClasspathEntry[]{});
	}
	
	public static IClasspathContainer createInstance(String version) {
		return new JavaEEContainer(version);
	}

}
