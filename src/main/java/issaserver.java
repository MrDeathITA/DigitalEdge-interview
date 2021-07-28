import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLEncoder;
import java.sql.SQLOutput;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.request.GetRequest;
import io.github.mainstringargs.alphavantagescraper.AlphaVantageConnector;
import io.github.mainstringargs.alphavantagescraper.StockQuotes;
import io.github.mainstringargs.alphavantagescraper.output.AlphaVantageException;
import io.github.mainstringargs.alphavantagescraper.output.quote.StockQuotesResponse;
import io.github.mainstringargs.alphavantagescraper.output.quote.data.StockQuote;

public class issaserver {

    private Socket socket = null;
    private ServerSocket server = null;
    private DataInputStream in = null;

    public issaserver(int port) throws IOException {
        String api = "O7VUQGYF49U91NLQ";
        int timeout = 3000;

        AlphaVantageConnector apiConnector = new AlphaVantageConnector(api, timeout);
        StockQuotes stocks = new StockQuotes(apiConnector);


        try {

            server = new ServerSocket(port);

            System.out.println("working");

            socket = server.accept();
            System.out.println("connection w client");

            in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

            String line = "";

            while (!line.equals("end")) {

                try {
                    line = in.readUTF();
                    System.out.println(line);
                    StockQuotesResponse response = stocks.quote(line);
                    StockQuote stock = response.getStockQuote();

                    line = in.readUTF();
                    if (Double.parseDouble(line) < stock.getPrice()) {
                        System.out.println("too low");
                    }
                    else if (Double.parseDouble(line) > stock.getPrice()) {
                        System.out.println("too high");
                    }
                    else {
                        System.out.println("jackpot");
                    }

                    System.out.printf("Date: %s, price: %s%n", stock.getLatestTradingDay(), stock.getPrice());


                } catch (IOException i) {
                    System.out.println(i);
                }
            }

                System.out.println("ending");

                socket.close();
                in.close();
            }
        catch(IOException i)
        {
            System.out.println(i);
        }
    }


    public static void main(String args[]) throws IOException {
        issaserver server = new issaserver(5000);
    }
}
