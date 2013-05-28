package net.stuxcrystal.minehack.mineping.addons;


import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.zip.ZipEntry;

import net.stuxcrystal.minehack.mineping.MinePing;
import net.stuxcrystal.minehack.mineping.api.Extension;

/**
 * Loads and manages Extensions.
 */
public final class ExtensionManager {

	/**
	 * Represtnes the Extension-Manager.s
	 */
	private static ExtensionManager manager;

	/**
	 * Represents an Extension. The boolean is the state of the Extension.
	 * If the value is true, the Extension is enabled.
	 */
	private List<Extension> extensions = new ArrayList<Extension>();

	/**
	 * The Lock to make the Extension-Manager thread-safe.
	 */
	private final Object lock = new Object();

	/**
	 * Constructs an Extension-manager.
	 */
	public ExtensionManager() {
		if (manager != null) throw new IllegalStateException("The Extension-Manager already exists.");
		manager = this;
	}

	/**
	 * Returns a list of enabled Extensions.
	 */
	public Extension[] getExtensions() {
		return this.extensions.toArray(new Extension[0]);
	}

	/**
	 * Enables an Extension.
	 */
	public void enableExtension(Extension extension) {
		synchronized (lock) {
			this.extensions.add(extension);
		}
	}

	private Extension instantiateExtension(Class<?> cls) throws ExtensionException {
		// Create a new instance.
		Object o;

		try {
			o = cls.newInstance();
		} catch (InstantiationException e) {
			throw new ExtensionException("Failed to instantiate the Extension.");
		} catch (IllegalAccessException e) {
			throw new ExtensionException("Failed to access the constructor.");
		}

		// Fail, if the instance is not an instance of the Extension-class.
		if (!(o instanceof Extension))
			throw new ExtensionException("Mainclass not instance of Extension.");

		// Return the Extension.
		return (Extension) o;
	}

	/**
	 * Loads a Extension from the Extension-configuration file.
	 */
	private Extension getExtension(Properties extensionData, ClassLoader classLoader) throws ExtensionException {

		// Retrieve the main-class.
		String main = extensionData.getProperty("main");

		// If not found, fail.
		if (main == null) {
			throw new ExtensionException("Extension has no main-class.");
		}

		// Retrieve the loader.
		Class<?> cls;

		try {
			cls = classLoader.loadClass(main);
		} catch (ClassNotFoundException e) {
			throw new ExtensionException("Couldn't find main-class.", e);
		}

		// Create a new instance.
		Object o;

		try {
			o = cls.newInstance();
		} catch (InstantiationException e) {
			throw new ExtensionException("Failed to instantiate the Extension.");
		} catch (IllegalAccessException e) {
			throw new ExtensionException("Failed to access the constructor.");
		}

		// Fail, if the instance is not an instance of the Extension-class.
		if (!(o instanceof Extension))
			throw new ExtensionException("Mainclass not instance of Extension.");

		// Return the Extension.
		return (Extension) o;
	}

	/**
	 * Loads the Extension-description-file.
	 */
	private Properties getProperties(JarFile file) throws ExtensionException {

		// Find the entry.
		ZipEntry entry = file.getEntry("extension.properties");

		if (entry == null) {
			throw new ExtensionException("Couldn't find Extension-Description.");
		}

		Properties result = new Properties();
		try {
			result.load(file.getInputStream(entry));
		} catch (IOException e) {
			throw new ExtensionException("Failed to load description.", e);
		}

		return result;
	}

	/**
	 * Returns all JarFiles inside a directory.
	 */
	private List<JarFile> getJarFiles(File directory) throws ExtensionException {

		if (directory == null)
			return Collections.emptyList();

		List<JarFile> result = new ArrayList<JarFile>();

		File[] files = directory.listFiles(new FilenameFilter() {

			public boolean accept(File arg0, String arg1) {

				return arg1.endsWith(".jar");
			}

			// CLASS ENDS HERE;
		});

		if (files == null)
			return Collections.emptyList();

		for (File f : files) {
			try {
				result.add(new JarFile(f));
			} catch (IOException e) {
				throw new ExtensionException("Failed to create JarFile instance.", e);
			}
		}

		return result;

	}

	/**
	 * Loads all Extensions inside a folder.
	 */
	public void loadExtensions(File directory) {

		try {
			for (JarFile jar : getJarFiles(directory)) {
				enableExtension(getExtension(getProperties(jar), getClassLoader(jar)));
			}
		} catch (ExtensionException e) {
			MinePing.staticlogger.log(Level.SEVERE, "Failed to load Extensions: " + e);
		}

	}

	/**
	 * Loads an Extension from a file.
	 */
	public void loadExtension(File file) {
		try {
			JarFile jf = new JarFile(file);
			enableExtension(getExtension(getProperties(jf), getClassLoader(jf)));
		} catch (IOException e) {
			MinePing.staticlogger.log(Level.SEVERE, "Failed to create JarFile instance.", e);
		} catch (ExtensionException e) {
			MinePing.staticlogger.log(Level.SEVERE, "Failed to load Extension...", e);
		}
	}

	/**
	 * Loads a extension from a class object.
	 * @param cls
	 */
	public void loadExtension(Class<?> cls) {
		try {
			enableExtension(this.instantiateExtension(cls));
		} catch (ExtensionException e) {
			MinePing.staticlogger.log(Level.SEVERE, "Failed to load Extension: " + e);
		}
	}

	/**
	 * Loads all extensions by the class object.
	 * @param cls
	 */
	public void loadExtensions(Class<?>[] cls) {
		for (Class<?> c : cls)
			loadExtension(c);
	}

	/**
	 * Creates a class loader.
	 * @param jf
	 * @return
	 */
	private ClassLoader getClassLoader(JarFile jf) {
		try {
			return URLClassLoader.newInstance(
				new URL[] {new File(jf.getName()).toURI().toURL()},
				ClassLoader.getSystemClassLoader()
			);
		} catch (MalformedURLException e) {
			return ClassLoader.getSystemClassLoader();
		}
	}
}
