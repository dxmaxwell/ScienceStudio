/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ClassUtils class.  These utilities have been adapted from JSecurity 0.9.0beta2.
 *     
 */
package ca.sciencestudio.util;

import java.lang.Class;
import java.lang.reflect.Constructor;

import ca.sciencestudio.util.exceptions.InstantiationException;
import ca.sciencestudio.util.exceptions.UnknownClassException;

/**
 * @author maxweld
 * 
 */
public abstract class ClassUtils {

    public static ClassLoader getDefaultClassLoader() {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if ( cl == null ) {
            cl = ClassUtils.class.getClassLoader();
        }
        return cl;
    }

	public static Class<?> forName(String fullyQualified) throws UnknownClassException {
        ClassLoader cl = getDefaultClassLoader();
        try {
            return cl.loadClass(fullyQualified);
        } catch (ClassNotFoundException e) {
            String msg = "Unable to load class name [" + fullyQualified + "] from ClassLoader [" + cl + "]";
            throw new UnknownClassException(msg, e);
        }
    }

    public static boolean isAvailable(String fullyQualifiedClassName) {
        try {
            forName(fullyQualifiedClassName);
            return true;
        } catch (UnknownClassException e) {
            return false;
        }
    }

    public static Object newInstance(String fqcn) {
        return newInstance(forName(fqcn ));
    }
    
	public static Object newInstance(Class<?> clazz) {
        if (clazz == null) {
            String msg = "Class method parameter cannot be null.";
            throw new IllegalArgumentException(msg);
        }
        try {
            return clazz.newInstance();
        } catch ( Exception e ) {
        	String msg = "Unable to instantiate class [" + clazz.getName() + "]";
            throw new InstantiationException(msg, e);
        }
    }
	
	public static Object newInstance(Class<?> clazz, Object... args) {
        Class<?>[] argTypes = new Class[args.length];
        for( int i = 0; i < args.length; i++ ) {
            argTypes[i] = args[i].getClass();
        }
        Constructor<?> ctor = getConstructor(clazz,argTypes);
        return instantiate(ctor, args);
    }
	
	public static Constructor<?> getConstructor(Class<?> clazz, Class<?>... argTypes) {
        try {
            return clazz.getConstructor(argTypes);
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException(e);
        }

    }
	
	public static Object instantiate(Constructor<?> ctor, Object... args) {
        try {
            return ctor.newInstance(args);
        } catch (Exception e) {
            String msg = "Unable to instantiate Permission instance with constructor [" + ctor + "]";
            throw new InstantiationException(msg, e);
        }
    }
}
