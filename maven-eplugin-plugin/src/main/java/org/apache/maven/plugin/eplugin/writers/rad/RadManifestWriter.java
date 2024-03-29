/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.maven.plugin.eplugin.writers.rad;

import java.io.File;
import java.util.Iterator;

import org.apache.maven.model.Resource;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.eplugin.Constants;
import org.apache.maven.plugin.eplugin.EclipseSourceDir;
import org.apache.maven.plugin.eplugin.writers.AbstractEclipseManifestWriter;
import org.apache.maven.plugin.ide.IdeUtils;
import org.apache.maven.plugin.ide.JeeUtils;
import org.apache.maven.project.MavenProject;

/**
 * Create or adapt the manifest files for the RAD6 runtime dependencys. attention these will not be used for the real
 * ear these are just to get the runtime enviorment using the maven dependencies. WARNING: The manifest resources added
 * here will not have the benefit of the dependencies of the project, since that's not provided in the setup() apis, one
 * of the locations from which this writer is used in the RadPlugin.
 * 
 * @author <a href="mailto:nir@cfc.at">Richard van Nieuwenhoven </a>
 */
public class RadManifestWriter
    extends AbstractEclipseManifestWriter
{

    private static final String DEFAULT_WEBAPP_RESOURCE_DIR =
        "src" + File.separatorChar + "main" + File.separatorChar + "webapp";

    /**
     * Search the project for the existing META-INF directory where the manifest should be located.
     * 
     * @return the apsolute path to the META-INF directory
     * @throws MojoExecutionException
     */
    protected String getMetaInfBaseDirectory( MavenProject project )
        throws MojoExecutionException
    {
        String metaInfBaseDirectory = null;

        if ( config.getProject().getPackaging().equals( Constants.PROJECT_PACKAGING_WAR ) )
        {
            // Generating web content settings based on war plug-in warSourceDirectory property
            File warSourceDirectory =
                new File( IdeUtils.getPluginSetting( config.getProject(), JeeUtils.ARTIFACT_MAVEN_WAR_PLUGIN,
                                                     "warSourceDirectory", //$NON-NLS-1$
                                                     DEFAULT_WEBAPP_RESOURCE_DIR ) );

            String webContentDir =
                IdeUtils.toRelativeAndFixSeparator( config.getEclipseProjectDirectory(), warSourceDirectory, false );

            metaInfBaseDirectory =
                config.getProject().getBasedir().getAbsolutePath() + File.separatorChar + webContentDir;

            log.debug( "Attempting to use: " + metaInfBaseDirectory + " for location of META-INF in war project." );

            File metaInfDirectoryFile = new File( metaInfBaseDirectory + File.separatorChar + META_INF_DIRECTORY );

            if ( metaInfDirectoryFile.exists() && !metaInfDirectoryFile.isDirectory() )
            {
                metaInfBaseDirectory = null;
            }
        }

        if ( metaInfBaseDirectory == null )
        {
            for ( Iterator iterator = project.getResources().iterator(); iterator.hasNext(); )
            {
                metaInfBaseDirectory = ( (Resource) iterator.next() ).getDirectory();

                File metaInfDirectoryFile = new File( metaInfBaseDirectory + File.separatorChar + META_INF_DIRECTORY );

                log.debug( "Checking for existence of META-INF directory: " + metaInfDirectoryFile );

                if ( metaInfDirectoryFile.exists() && !metaInfDirectoryFile.isDirectory() )
                {
                    metaInfBaseDirectory = null;
                }
            }
        }

        return metaInfBaseDirectory;
    }

    /**
     * {@inheritDoc}
     */
    public void write()
        throws MojoExecutionException
    {
        super.write();
        verifyManifestBasedirInSourceDirs( getMetaInfBaseDirectory( config.getProject() ) );
    }



    // NOTE: This could change the config!
    private void verifyManifestBasedirInSourceDirs( String metaInfBaseDirectory )
    {
        EclipseSourceDir[] sourceDirs = config.getSourceDirs();

        if ( sourceDirs != null )
        {
            boolean foundMetaInfBaseDirectory = false;

            for ( int i = 0; i < sourceDirs.length; i++ )
            {
                EclipseSourceDir esd = sourceDirs[i];

                if ( esd.getPath().equals( metaInfBaseDirectory ) )
                {
                    foundMetaInfBaseDirectory = true;
                    break;
                }
            }

            if ( !foundMetaInfBaseDirectory )
            {
                EclipseSourceDir dir =
                    new EclipseSourceDir( metaInfBaseDirectory, null, true, false, null, null, false );

                EclipseSourceDir[] newSourceDirs = new EclipseSourceDir[sourceDirs.length + 1];
                newSourceDirs[sourceDirs.length] = dir;

                System.arraycopy( sourceDirs, 0, newSourceDirs, 0, sourceDirs.length );

                config.setSourceDirs( newSourceDirs );
            }
        }
    }
}
