package com.android.parnyukt.googlequeryapp.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by tparnyuk on 09.02.15.
 */
public class Streams {

    public static <T extends OutputStream> T copy(InputStream in, T out) throws IOException {
        byte[] buf = new byte[8192];

        for(int read = in.read(buf); read != -1; read = in.read(buf)) {
            out.write(buf, 0, read);
        }

        return out;
    }

    public static <T extends OutputStream> T copyAndClose(InputStream in, T out) throws IOException {
        try {
            return copy(in, out);
        }
        finally {
            in.close();
            out.flush();
            out.close();
        }
    }
}
