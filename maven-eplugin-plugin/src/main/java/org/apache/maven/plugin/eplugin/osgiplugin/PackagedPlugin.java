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
package org.apache.maven.plugin.eplugin.osgiplugin;

import java.io.File;
import java.io.IOException;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 * Represents an Eclipse plugin that it's packaged as a jar
 * 
 * @author <a href="mailto:carlos@apache.org">Carlos Sanchez</a>
 * @version $Id: PackagedPlugin.java 728546 2008-12-21 22:56:51Z bentmann $
 */
public class PackagedPlugin
    extends AbstractEclipseOsgiPlugin
{
    public PackagedPlugin( File jar )
        throws IOException
    {
        super( jar );
    }

    public boolean hasManifest()
        throws IOException
    {
        return getJar().getManifest() != null;
    }

    public JarFile getJar()
        throws IOException
    {
        return new JarFile( getFile(), false );
    }

    public Manifest getManifest()
        throws IOException
    {
        return getJar().getManifest();
    }

    public File getJarFile()
        throws IOException
    {
        return getFile();
    }

}
