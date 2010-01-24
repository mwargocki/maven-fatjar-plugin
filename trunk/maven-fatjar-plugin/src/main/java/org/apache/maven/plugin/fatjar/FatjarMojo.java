package org.apache.maven.plugin.fatjar;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.apache.maven.archiver.ManifestConfiguration;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.MojoExecutionException;
import org.codehaus.plexus.util.FileUtils;

/**
 * Goal which fatjar file.
 * 
 * @author Ñî³¬ <ychao@bankcomm.com> 2010-1-23
 * 
 * @goal fatjar
 * @phase package
 * @requiresDependencyResolution runtime
 */
public class FatjarMojo extends AbstractJarMojo {
	
	/**
	 * @parameter expression="${project.build.outputDirectory}"
	 */
	private File classesDirectory;

	/**
	 * Classifier to add to the artifact generated. If given, the artifact will
	 * be an attachment instead.
	 * 
	 * @parameter
	 */
	private String classifier = "fatjar";

	/**
	 * @parameter expression="${fatjar.classpathPrefix}"
	 */
	private String classpathPrefix = "";

	/**
	 * @parameter expression="${fatjar.deployDirectory}"
	 */
	private File deployDirectory;

	public void execute() throws MojoExecutionException {
		copyDependencies();

		extraManifest();

		super.execute();

		cleanup();

		deploy();
	}	

	/**
	 * copy transive dependencies to ${project.build.outputDirectory}/${fatjar.classpathPrefix}
	 */
	private void copyDependencies() {
		
		File f = classesDirectory;

		if (!f.exists()) {
			f.mkdirs();
		}

		super.getLog().info("Copy dependencies: ");
		Set<Artifact> artifacts = project.getArtifacts();

		for (Artifact artifact : artifacts) {
			String scope = artifact.getScope();
			if (!"compile".equals(scope) && !"runtime".equals(scope)
					&& !"".equals(scope) && scope != null) {
				continue;
			}
			super.getLog().info(getId(artifact));
			File jarFile = artifact.getFile();

			try {
				FileUtils.copyFile(jarFile, new File(classesDirectory,
						classpathPrefix + File.separator + jarFile.getName()));
			} catch (IOException e) {
				super.getLog().error(e.getMessage());
			}
		}
	}
	
	/**
	 * add "Class-Path: xx.jar" to MANIFEST.MF
	 */
	private void extraManifest() {
		ManifestConfiguration manifest = super.archive.getManifest();
		PropertyUtil.setProperty(manifest, "addClasspath", "true");
		PropertyUtil.setProperty(manifest, "classpathPrefix", classpathPrefix);
	}
	
	/**
	 * clean up ${project.build.outputDirectory}/${fatjar.classpathPrefix} directory and files
	 */
	private void cleanup() {
		File f = new File(classesDirectory, classpathPrefix);
		try {
			FileUtils.forceDelete(f);
		} catch (IOException e) {
			super.getLog().error(e.getMessage());
			try {
				FileUtils.forceDeleteOnExit(f);
			} catch (IOException ex) {
				super.getLog().error(ex.getMessage());
			}

		}
	}
	
	/**
	 * copy jar to deploy directory, specially for webapp deploy case, if any
	 */
	private void deploy() {
		if (deployDirectory == null)
			return;

		if (!deployDirectory.exists()) {
			deployDirectory.mkdir();
		}
		
		super.getLog().info("Copy fatjar to deploy directory: " + deployDirectory.getAbsolutePath());

		File fatjar = getJarFile(outputDirectory, finalName, classifier);
		try {
			FileUtils.copyFileToDirectory(fatjar, deployDirectory);
		} catch (IOException e) {
			super.getLog().error(e.getMessage());
		}
	}

	protected String getClassifier() {
		return classifier;
	}

	/**
	 * @return type of the generated artifact
	 */
	protected String getType() {
		return "jar";
	}

	/**
	 * Return the main classes directory, so it's used as the root of the jar.
	 */
	protected File getClassesDirectory() {
		return classesDirectory;
	}

	@SuppressWarnings("unused")
	private String getId(Dependency dependency) {
		return getId(dependency.getGroupId(), dependency.getArtifactId(),
				dependency.getType(), dependency.getClassifier());
	}

	private String getId(Artifact artifact) {
		return getId(artifact.getGroupId(), artifact.getArtifactId(), artifact
				.getType(), artifact.getClassifier());
	}

	private String getId(String groupId, String artifactId, String type,
			String classifier) {
		return groupId + ":" + artifactId + ":" + type
				+ ((classifier != null) ? ":" + classifier : "");
	}

}
