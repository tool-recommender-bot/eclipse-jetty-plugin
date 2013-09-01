// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package net.sourceforge.eclipsejetty.util;

import net.sourceforge.eclipsejetty.JettyPluginM2EUtils;

import org.eclipse.jdt.core.IClasspathAttribute;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.launching.IRuntimeClasspathEntry;
import org.eclipse.m2e.core.embedder.ArtifactRef;
import org.eclipse.m2e.core.project.IMavenProjectFacade;

/**
 * Holds the artifact name and scope of a dependency.
 * 
 * @author thred
 */
public class MavenDependencyInfo
{

    public static MavenDependencyInfo create(ArtifactRef artifactRef)
    {
        return new MavenDependencyInfo(artifactRef.getGroupId(), artifactRef.getArtifactId(), artifactRef.getVersion(),
            artifactRef.getClassifier(), null, MavenScope.to(artifactRef.getScope()));
    }

    public static MavenDependencyInfo create(IMavenProjectFacade facade, String variant, MavenScope scope)
    {
        return new MavenDependencyInfo(facade.getArtifactKey().getGroupId(), facade.getArtifactKey().getArtifactId(),
            facade.getArtifactKey().getVersion(), facade.getArtifactKey().getClassifier(), variant, scope);
    }

    public static MavenDependencyInfo create(IRuntimeClasspathEntry runtimeClasspathEntry)
    {
        IClasspathEntry classpathEntry = runtimeClasspathEntry.getClasspathEntry();

        if (classpathEntry == null)
        {
            return null;
        }

        IClasspathAttribute[] extraAttributes = classpathEntry.getExtraAttributes();
        String groupId = null;
        String artifactId = null;
        String version = null;
        String classifier = null;
        MavenScope scope = null;

        for (IClasspathAttribute extraAttribute : extraAttributes)
        {
            if ("maven.groupId".equals(extraAttribute.getName()))
            {
                groupId = extraAttribute.getValue();
            }
            else if ("maven.artifactId".equals(extraAttribute.getName()))
            {
                artifactId = extraAttribute.getValue();
            }
            else if ("maven.version".equals(extraAttribute.getName()))
            {
                version = extraAttribute.getValue();
            }
            else if ("maven.classifier".equals(extraAttribute.getName()))
            {
                classifier = extraAttribute.getValue();
            }
            else if ("maven.scope".equals(extraAttribute.getName()))
            {
                scope = MavenScope.to(extraAttribute.getValue());
            }
        }

        if (artifactId == null)
        {
            return null;
        }

        return new MavenDependencyInfo(groupId, artifactId, version, classifier, null, scope);
    }

    private final String groupId;
    private final String artifactId;
    private final String version;
    private final String classifier;
    private final String variant;

    private MavenScope scope;

    public MavenDependencyInfo(String groupId, String artifactId, String version, String classifier)
    {
        this(groupId, artifactId, version, classifier, null);
    }

    public MavenDependencyInfo(String groupId, String artifactId, String version, String classifier, String variant)
    {
        super();

        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.classifier = classifier;
        this.variant = variant;
    }

    public MavenDependencyInfo(String groupId, String artifactId, String version, String classifier, String variant,
        MavenScope scope)
    {
        this(groupId, artifactId, version, classifier, variant);

        setScope(scope);
    }

    public String getGroupId()
    {
        return groupId;
    }

    public String getArtifactId()
    {
        return artifactId;
    }

    public String getVersion()
    {
        return version;
    }

    public String getClassifier()
    {
        return classifier;
    }

    public String getVariant()
    {
        return variant;
    }

    public MavenScope getScope()
    {
        return scope;
    }

    public void setScope(MavenScope scope)
    {
        this.scope = scope;
    }

    public String toPortableString()
    {
        return JettyPluginM2EUtils.toPortableString(groupId, artifactId, version, classifier, variant);
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;

        result = (prime * result) + ((artifactId == null) ? 0 : artifactId.hashCode());
        result = (prime * result) + ((classifier == null) ? 0 : classifier.hashCode());
        result = (prime * result) + ((groupId == null) ? 0 : groupId.hashCode());
        result = (prime * result) + ((version == null) ? 0 : version.hashCode());
        result = (prime * result) + ((variant == null) ? 0 : variant.hashCode());

        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }

        if (obj == null)
        {
            return false;
        }

        if (getClass() != obj.getClass())
        {
            return false;
        }

        MavenDependencyInfo other = (MavenDependencyInfo) obj;

        if (artifactId == null)
        {
            if (other.artifactId != null)
            {
                return false;
            }
        }
        else if (!artifactId.equals(other.artifactId))
        {
            return false;
        }

        if (classifier == null)
        {
            if (other.classifier != null)
            {
                return false;
            }
        }
        else if (!classifier.equals(other.classifier))
        {
            return false;
        }

        if (groupId == null)
        {
            if (other.groupId != null)
            {
                return false;
            }
        }
        else if (!groupId.equals(other.groupId))
        {
            return false;
        }

        if (version == null)
        {
            if (other.version != null)
            {
                return false;
            }
        }
        else if (!version.equals(other.version))
        {
            return false;
        }

        if (variant == null)
        {
            if (other.variant != null)
            {
                return false;
            }
        }
        else if (!variant.equals(other.variant))
        {
            return false;
        }

        return true;
    }

    @Override
    public String toString()
    {
        return toPortableString();
    }

}