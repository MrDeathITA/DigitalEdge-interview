import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;

public class issaclass {


    private Socket socket  = null;
    private DataInputStream input = null;
    private DataOutputStream out = null;


    public issaclass(String address, int port)  {


        try {
            socket = new Socket(address,port );
            System.out.println("connected");

            input = new DataInputStream(System.in);

            out = new DataOutputStream(socket.getOutputStream());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String line = "";

        while (!line.equals("end")) {
            try
            {
                line = input.readLine();
                out.writeUTF(line);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            input.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public static void main(String[] args) {
    issaclass client = new issaclass("127.0.0.1", 5000);
    }
}
