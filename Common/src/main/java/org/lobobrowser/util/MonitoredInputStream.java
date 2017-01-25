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
 * Created on Apr 15, 2005
 */
package org.lobobrowser.util;

import java.io.IOException;
import java.io.InputStream;

/**
 * The Class MonitoredInputStream.
 *
 * @author J. H. S.
 */
public class MonitoredInputStream extends InputStream {
    /** The delegate. */
    private final InputStream delegate;
    /** The progress. */
    private int progress = 0;
    /** The min progress event gap. */
    private final long minProgressEventGap;
    /** The evt progress. */
    public final EventDispatch evtProgress = new EventDispatch();
    
    /**
     * Instantiates a new monitored input stream.
     *
     * @param delegate
     *            the delegate
     * @param minProgressEventGap
     *            the min progress event gap
     */
    public MonitoredInputStream(InputStream delegate, int minProgressEventGap) {
        this.delegate = delegate;
        this.minProgressEventGap = minProgressEventGap;
    }
    
    /**
     * Instantiates a new monitored input stream.
     *
     * @param delegate
     *            the delegate
     */
    public MonitoredInputStream(InputStream delegate) {
        this(delegate, 200);
    }
    
    /*
     * (non-Javadoc)
     * @see java.io.InputStream#available()
     */
    @Override
    public int available() throws IOException {
        return this.delegate.available();
    }
    
    /*
     * (non-Javadoc)
     * @see java.io.InputStream#close()
     */
    @Override
    public void close() throws IOException {
        this.delegate.close();
    }
    
    /*
     * (non-Javadoc)
     * @see java.io.InputStream#markSupported()
     */
    @Override
    public boolean markSupported() {
        return false;
    }
    
    /*
     * (non-Javadoc)
     * @see java.io.InputStream#read()
     */
    @Override
    public int read() throws IOException {
        int b = this.delegate.read();
        if (b != -1) {
            this.progress++;
        }
        return b;
    }
    
    /** The last even posted. */
    private long lastEvenPosted = 0;
    
    /*
     * (non-Javadoc)
     * @see java.io.InputStream#read(byte[], int, int)
     */
    @Override
    public int read(byte[] arg0, int arg1, int arg2) throws IOException {
        int numRead = this.delegate.read(arg0, arg1, arg2);
        if (numRead != -1) {
            this.progress += numRead;
            long currentTime = System.currentTimeMillis();
            if ((currentTime
                    - this.lastEvenPosted) > this.minProgressEventGap) {
                this.evtProgress
                        .fireEvent(new InputProgressEvent(this, this.progress));
                this.lastEvenPosted = currentTime;
            }
        }
        return numRead;
    }
}
