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
package org.lobobrowser.util;

import java.lang.ref.WeakReference;

/**
 * The Class LocalFilter.
 */
public class LocalFilter implements ObjectFilter {
    /*
     * (non-Javadoc)
     * @see org.xamjwg.util.ObjectFilter#decode(Object)
     */
    @Override
    public Object decode(Object source) {
        WeakReference<?> wf = (WeakReference<?>) source;
        return wf == null ? null : wf.get();
    }
    
    /*
     * (non-Javadoc)
     * @see org.xamjwg.util.ObjectFilter#encode(Object)
     */
    @Override
    public Object encode(Object source) {
        throw new UnsupportedOperationException("Read-only collection.");
    }
}
