/*
    GNU GENERAL LICENSE
    Copyright (C) 2006 The Lobo Project. Copyright (C) 2014 - 2017 Lobo Evolution

    This program is free software; you can redistribute it and/or
    modify it under the terms of the GNU General Public
    License as published by the Free Software Foundation; either
    verion 3 of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    General License for more details.

    You should have received a copy of the GNU General Public
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
    

    Contact info: lobochief@users.sourceforge.net; ivan.difrancesco@yahoo.it
 */
/*
 * Created on Jun 19, 2005
 */
package org.lobobrowser.util;

import java.util.Collection;

/**
 * The Class MultiplexClassLoader.
 *
 * @author J. H. S.
 */
public abstract class MultiplexClassLoader extends BaseClassLoader {
    /** The Constant EMPTY_CLASS_LOADERS. */
    private static final BaseClassLoader[] EMPTY_CLASS_LOADERS = new BaseClassLoader[0];
    /** The parent loaders. */
    private final BaseClassLoader[] parentLoaders;
    
    /**
     * Instantiates a new multiplex class loader.
     *
     * @param classLoaders
     *            the class loaders
     */
    public MultiplexClassLoader(Collection<BaseClassLoader> classLoaders) {
        super(null);
        this.parentLoaders = (BaseClassLoader[]) classLoaders.toArray(EMPTY_CLASS_LOADERS);
    }
    
    /*
     * (non-Javadoc)
     * @see java.lang.ClassLoader#loadClass(String, boolean)
     */
    @Override
    public synchronized Class loadClass(String name, boolean resolve)
            throws ClassNotFoundException {
        // First, check if the class has already been loaded
        Class c = findLoadedClass(name);
        if (c == null) {
            try {
                int len = this.parentLoaders.length;
                if (len == 0) {
                    c = findSystemClass(name);
                } else {
                    for (int i = 0; i < len; i++) {
                        BaseClassLoader parent = this.parentLoaders[i];
                        try {
                            c = parent.loadClass(name, false);
                            if (c != null) {
                                return c;
                            }
                        } catch (ClassNotFoundException cnfe) {
                            // ignore
                        }
                    }
                }
            } catch (ClassNotFoundException e) {
                // If still not found, then invoke findClass in order
                // to find the class.
                c = findClass(name);
            }
            if (c == null) {
                c = findClass(name);
            }
        }
        if (resolve) {
            resolveClass(c);
        }
        return c;
    }
}
