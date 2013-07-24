package th.co.aerothai.callservice.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class CountingServletResponse extends HttpServletResponseWrapper {

    private final long startTime;
    private final CountingServletOutputStream output;
    private final PrintWriter writer;

    public CountingServletResponse(HttpServletResponse response) throws IOException {
        super(response);
        startTime = System.nanoTime();
        output = new CountingServletOutputStream(response.getOutputStream());
        writer = new PrintWriter(output, true);
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return output;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return writer;
    }

    @Override
    public void flushBuffer() throws IOException {
        writer.flush();
    }

    public long getElapsedTime() {
        return System.nanoTime() - startTime;
    }

    public long getByteCount() throws IOException {
        flushBuffer(); // Ensure that all bytes are written at this point.
        return output.getByteCount();
    }

}