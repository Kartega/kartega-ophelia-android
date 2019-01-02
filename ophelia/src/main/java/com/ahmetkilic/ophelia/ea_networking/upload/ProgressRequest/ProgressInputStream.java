package com.ahmetkilic.ophelia.ea_networking.upload.ProgressRequest;


import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Ahmet Kılıç on 18.12.2018.
 * Copyright © 2018, Kartega. All rights reserved.
 *
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 */
class ProgressInputStream extends InputStream {
    private final InputStream stream;
    private final ProgressCallback listener;
    private long total;
    private long totalRead;

    ProgressInputStream(InputStream stream, ProgressCallback listener, long total) {
        this.stream = stream;
        this.listener = listener;
        this.total = total;
    }

    @Override
    public int read() throws IOException {
        int read = this.stream.read();
        if (this.total < 0) {
            this.listener.onProgressChanged(-1, -1, -1);
            return read;
        }
        if (read >= 0) {
            this.totalRead++;
            this.listener.onProgressChanged(this.totalRead, this.total, (this.totalRead * 1.0F) / this.total);
        }
        return read;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int read = this.stream.read(b, off, len);
        if (this.total < 0) {
            this.listener.onProgressChanged(-1, -1, -1);
            return read;
        }
        if (read >= 0) {
            this.totalRead += read;
            this.listener.onProgressChanged(this.totalRead, this.total, (this.totalRead * 1.0F) / this.total);
        }
        return read;
    }

    @Override
    public void close() throws IOException {
        if (this.stream != null) {
            this.stream.close();
        }
    }

}