package th.co.aerothai.callservice.servlet;

import java.io.IOException;

import javax.servlet.ServletOutputStream;

import org.apache.commons.io.output.CountingOutputStream;

public class CountingServletOutputStream extends ServletOutputStream {

    private final CountingOutputStream output;

    public CountingServletOutputStream(ServletOutputStream output) {
        this.output = new CountingOutputStream(output);
    }

    @Override
    public void write(int b) throws IOException {
        output.write(b);
    }

    @Override
    public void flush() throws IOException {
        output.flush();
    }

    public long getByteCount() {
        return output.getByteCount();
    }

}