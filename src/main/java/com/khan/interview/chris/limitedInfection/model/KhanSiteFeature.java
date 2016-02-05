package com.khan.interview.chris.limitedInfection.model;

import java.util.ArrayList;
import java.util.List;

/**
 * KhanSiteFeature is the class that represents a typical Khan Academy feature.
 * This class contains:
 * <ul>
 * <li>The feature's name (name) e.g. "Cool Blue NavBar"</li>
 * <li>Supported versions (supportedVersions) e.g. "1.0, 1.1"</li>
 * <li>The current version of the feature (currentVersion) e.g. "Version 1.2"
 * </li>
 * </ul>
 * 
 * @author Christopher Chen
 *
 */
public class KhanSiteFeature {

	// Feature name
	private String name;
	// supported versions
	private List<String> supportedVersions = new ArrayList<String>();
	// current version
	private String currentVersion;

	/**
	 * Default empty constructor
	 */
	public KhanSiteFeature() {
		// empty
	}

	/**
	 * @param name
	 *            : name of feature
	 * @param supportVersions
	 *            : list of versions supported
	 * @param current
	 *            : current version
	 */
	public KhanSiteFeature(String name, List<String> supportVersions, String current) {
		this.name = name;
		this.supportedVersions = supportVersions;
		this.currentVersion = current;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getSupportedVersions() {
		return supportedVersions;
	}

	public void setSupportedVersions(List<String> supportedVersions) {
		this.supportedVersions = supportedVersions;
	}

	public String getCurrentVersion() {
		return currentVersion;
	}

	public void setCurrentVersion(String currentVersion) {
		this.currentVersion = currentVersion;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		KhanSiteFeature other = (KhanSiteFeature) obj;
		if (currentVersion == null) {
			if (other.currentVersion != null)
				return false;
		} else if (!currentVersion.equals(other.currentVersion))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (supportedVersions == null) {
			if (other.supportedVersions != null)
				return false;
		} else if (!supportedVersions.equals(other.supportedVersions))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "KhanSiteFeature [name=" + name + ", supportedVersions=" + supportedVersions + ", currentVersion="
				+ currentVersion + "]";
	}

}
