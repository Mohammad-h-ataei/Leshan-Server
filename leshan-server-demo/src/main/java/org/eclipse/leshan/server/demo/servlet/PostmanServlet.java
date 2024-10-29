package org.eclipse.leshan.server.demo.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.SocketHandler;

public class PostmanServlet implements Runnable
{
    private static final String IP_ADDRESS = "142.137.242.34";
    private static final int PORT = 9696;

    public LinkedBlockingQueue<String> queue  = null;
    private PrintStream output;
    private static final Logger LOG = LoggerFactory.getLogger(EventServlet.class);

    public PostmanServlet()
    {
       this.queue = new LinkedBlockingQueue<>();

        try
        {
            Socket socket = new Socket(IP_ADDRESS, PORT);
            output = new PrintStream( socket.getOutputStream() );
        }
        catch (IOException e)
        {
            output = null;
            e.printStackTrace();
        }

    }

    @Override
    public void run()
    {
        while(true)
        {
            try
            {
                String message = this.queue.take();
                LOG.debug("Message to send : " + message);

                if(output != null)
                {
                    output.println(message);
                }
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
                LOG.debug("Timeout");
            }
        }
    }

    public boolean add(String message)
    {
        return this.queue.offer(message);
    }
}
