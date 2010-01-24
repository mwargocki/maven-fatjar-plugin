package com.bankcomm.laptev.project.classpath;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.ClasspathContainerInitializer;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

public class JavaEEContainerInitializer extends ClasspathContainerInitializer {

	@Override
	public void initialize(IPath containerPath, IJavaProject project) throws CoreException {

		IClasspathContainer container = JavaEEContainer.createInstance("5.0");
		JavaCore.setClasspathContainer(containerPath, 
									   new IJavaProject[] {project}, 
									   new IClasspathContainer[] { container}, 
									   null);
	}

}
