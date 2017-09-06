import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.FileReader;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/*
RAJASOORIYA R.B
E/13/276

*/

public class AuctionServer implements Runnable { 
    
	
	private static String ID="";
    public static final int BASE_PORT = 2000;     
    
    private static ServerSocket serverSocket; 
    private static int socketNumber; 

	private static String[] arr1;
	
	
	public static Timer timer;
    private Socket connectionSocket;
	private static HashMap<String,String> map = new HashMap<String, String>();
	
	private static HashMap<Integer,String[]> userData = new HashMap<Integer, String[]>();		//to store the client inputs
	private static JLabel[] labels;
	
	private static HashMap<String,String[]> map2;		//to store the client inputs for further use
	private static HashMap<String,String[]> map3;
	private static String[] data = new String[3];
	
	private static JButton button;
	private static JFrame frame;
    private static JPanel panel;
   	private static JLabel[] label; 
	private static JLabel label1,label2,label3,label4,label5,label6,label7,label8,lab1,lab2,lab3,lab4; 
    private static JTextField text;
	private static JTextField text2;
	private static JTextField text3;
	
	private static boolean value = false;
	private static String text1 = "";	
	
	private static int i = 0;
	
    public AuctionServer(int socket) throws IOException { 
	serverSocket = new ServerSocket(socket); 
	socketNumber = socket; 
    }

    public AuctionServer(Socket socket) { 
	this.connectionSocket = socket; 
    }
	

    public void server_loop() throws IOException { 
	while(true) { 
	    Socket socket = serverSocket.accept(); 	    
	    Thread worker = new Thread(new AuctionServer(socket)); 
	    worker.start();
		//JOptionPane.showMessageDialog(null, "WINNER !!!"); 	    
	}
    }

    public void run() { 
	try { 
	    BufferedReader in = new 
		BufferedReader(new InputStreamReader(this.connectionSocket.getInputStream()));		//reading the client input line by line
	    PrintWriter out = new 
		PrintWriter(new OutputStreamWriter(this.connectionSocket.getOutputStream()));	 	
	    String line; 
				
		String[] key = new String[2];
	   
	   
	   // ipunts by the client is store in an array ( data[] )
	   for( int n = 0; n < 3 ; n++ ) { 
		line = in.readLine();
		data[n] = line;							// getting the first 3 inputs of the client
		System.out.println(line);
		
		if ( n == 0) {
		out.print("Your name: "+line + "\n");
		
		
			
		}		
		else if ( n == 1 ) {
			
		
			String[] Key = new String[2];
			Key[0] = data[0];
			Key[1] = data[1];
			String L = "";						
			L = L.concat(data[0]);
			L = L.concat(data[1]);
			
			if ( map2.get(data[1]) == null ) {
			JOptionPane.showMessageDialog(null,"Please enter a value more than the previous bid value!");
			out.println("-1");
			n = 3;
			//Thread.stop();
			}
			else if ( map.get(L) == null ) {
			out.println("User has not made a bid earlier on this item.");
			out.print("Entered security key: "+line + "\n"); 
		    System.out.println("The price of the item '" + data[1] + "' currently = " + map2.get(data[1])[2]  );
		    out.println("The price of the item '" + data[1] + "' currently = " + map2.get(data[1])[2]);
			}
			else{ 
			out.println("User's earlier bid value on this item: " +map.get(L));
			out.print("Entered security key: "+line + "\n"); 
		    System.out.println("The price of the item '" + data[1] + "' currently = " + map2.get(data[1])[2]  );
		    out.println("The price of the item '" + data[1] + "' currently = " + map2.get(data[1])[2]);
			}
		
		}
		else if ( n == 2){ 
		out.print("Entered bid value :"+line + "\n"); 
		key[0] = data[0];
		key[1] = data[1];
		String K ="";
		if ( Integer.parseInt(data[2]) < Integer.parseInt( map2.get(data[1])[2] )  ){}
		else{
		K = K.concat(key[0]);			// client name and the security key is concatenated and used as the key to store in the hashmap
		K = K.concat(key[1]);
		map.put(K,data[2]);
		}
		userData.put(i,data);			//client data stored in hashmap , ID is used as the key for the hashmap 
		}
		
		
		
		out.flush();
		
	
	    }
	
			//client name and the bid value is in the hash map " client name is in data[0] " and " bid value is in data[2]"
		updatePrice();				//after client entered the data,update the current values
		
		
		
	}// try 
	catch (IOException e) { 
	    System.out.println(e); 
	} 
	try { 	    
	    this.connectionSocket.close(); 
	} catch(IOException e) {}
	i++;
    }

    public static void main(String [] args) throws IOException {
	getData();

	
	 Timer timer = new Timer();
        TimerTask myTask = new TimerTask() {
            @Override								//updating the GUI repeatdly after 500 mili seconds
            public void run() {
              
				update();
                
            }
        };

        timer.schedule(myTask, 2000, 5);
	
	
	
	setGUI();			// creating the GUI
	
	

	AuctionServer server = new AuctionServer(BASE_PORT);
	server.server_loop();

		
    }
	
	
	
	public static void getData(){

        try{
            BufferedReader br = new BufferedReader(new FileReader("stocks.csv"));		// reading the given file

            map2 = new HashMap<String, String[]>();
			map3 = new HashMap<String, String[]>();				
			
            String line;
			String[] item;
											
			// reading the file line by line
            while (( line=br.readLine()) != null) {

                item = line.split(",");
				map2.put(item[1],item);			// storing the data in the stocks file
				map3.put(item[0],item);
						
            }
			
           

        }catch (IOException e) {
            System.out.println("ERROR.File is not found !");		//if the file is not found,print a error message
        }
	}
	
	public void updatePrice(){
		
		//System.out.println("data of user 1 "+userData.get(0)[0]);
		
		if( Integer.parseInt(data[2]) < Integer.parseInt(map2.get(data[1])[2]) ){
		JOptionPane.showMessageDialog(null,"Please enter a value more than the previous bid value!");	
		
		//return -1;
		
		}
		else {
		String[] arr = new String[3];
		arr = map2.get(data[1]);
		arr[2] = data[2];						//update the new values by replacing old values in the hashmap
		map2.replace(data[1],arr);
		map3.replace(data[0],arr);
		System.out.println("Updated Value = "+map2.get(data[1])[2]);
		
		String var = "Price successfully updated to :";
		var = var.concat(map2.get(data[1])[2]);
		
		
		
		//return 0;
		}
	}
	
	public static void setGUI(){
	
		frame = new JFrame("Auction Server");
		frame.setVisible(true);
        frame.setSize(600, 600);
        panel = new JPanel();
		
		
		String[] arr = {"FB","VRTU","MSFT","GOOGL","YHOO","XLNX","TSLA","TXN"}; 
		String var="";
		
		String[] newData1 = new String[8]; 
		for (  int i = 0; i < 8 ; i++){

		String space = "    ";
		String symbol = "Symbol: ";
		String name = "Name: ";
		String price = "Current Prices: ";
		
		symbol = symbol.concat(map3.get(arr[i])[0]);	//symbol
		symbol = symbol.concat("  ");
		
		name = name.concat(map3.get(arr[i])[1]);	//name
		price = price.concat(map3.get(arr[i])[2]); //price
			
		symbol = symbol.concat(name);
		symbol = symbol.concat("  ");
		
		symbol = symbol.concat(price);
		
		newData1[i] = symbol;
		
		}
		
		
		
		label1 = new JLabel(newData1[0]);
		panel.add(label1);
		label2 = new JLabel(newData1[1]);
		panel.add(label2);
		label3 = new JLabel(newData1[2]);
		panel.add(label3);
		label4 = new JLabel(newData1[3]);
		panel.add(label4);
		label5 = new JLabel(newData1[4]);
		panel.add(label5);
		label6 = new JLabel(newData1[5]);
		panel.add(label6);
		label7 = new JLabel(newData1[6]);
		panel.add(label7);
		label8 = new JLabel(newData1[7]);
		panel.add(label8);
		
		
		lab1 = new JLabel();
		panel.add(lab1);
		lab2 = new JLabel();
		panel.add(lab2);
		
		text = new JTextField();
        text.setSize(200,200);
        panel.add(text);
		
        text.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                text1 = text.getText();
				
                System.out.println("Symbol :"+map2.get(text1)[0]+" Name :"+map2.get(text1)[1]+" Price :"+map2.get(text1)[2] );
				String words = "";
				String space = " ";
				words = words.concat(map2.get(text1)[0]);
				words = words.concat(space);
				words = words.concat(map2.get(text1)[1]);
				words = words.concat(space);
				words = words.concat(map2.get(text1)[2]);
				lab1.setText(words);
            }
        });
		text2 = new JTextField();
        text2.setSize(200,200);
        panel.add(text2);
        text2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
				if( Integer.parseInt( text2.getText() ) > Integer.parseInt ( map2.get(text.getText())[2] )){
				String text3 = text2.getText();
				String[] arrr = new String[3];
				arrr = map2.get(text1);
				arrr[2] = text3;
				map2.replace(text1,arrr);
                System.out.println("Symbol :"+map2.get(text1)[0]+" Name :"+map2.get(text1)[1]+" Price :"+map2.get(text1)[2] );
				
				String word = "";
				String space = " ";
				word = word.concat(map2.get(text1)[0]);
				word = word.concat(space);
				word = word.concat(map2.get(text1)[1]);
				word = word.concat(space);
				word = word.concat(map2.get(text1)[2]);
				lab1.setText(word);
				}
				else {}



		}
		 });
		
		
		
		text3 = new JTextField();
        text3.setSize(200,200);
        panel.add(text3);
        text3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               ID = text3.getText();
            }
        });
		
		
		lab3 = new JLabel();
		panel.add(lab3);
		lab4 = new JLabel();
		panel.add(lab4);
		button = new JButton("Enter the ID and press ENter and click here!");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               
			   String S ="";
			   
					
					S = S.concat(userData.get(Integer.parseInt(ID))[0] );
					S = S.concat(" ");
					S = S.concat(userData.get(Integer.parseInt(ID))[1] );
					S = S.concat(" ");
					S = S.concat(userData.get(Integer.parseInt(ID))[2] );
					
					lab3.setText(S);
					
					
			   
            }
        });
        panel.add(button);
      
		
		panel.setLayout(new GridLayout(10,1,5,10));
        frame.add(panel);
		
		frame.pack();
	}
	


	// this method is used to update the data with the clock
		public static void update(){
		
		String[] arr = {"FB","VRTU","MSFT","GOOGL","YHOO","XLNX","TSLA","TXN"}; 
		String var="";
		
		String[] newData = new String[8]; 
		for (  int i = 0; i < 8 ; i++){
		
		String space = "    ";
		String symbol = "Symbol: ";
		String name = "Name: ";
		String price = "Current Prices: ";
		
		symbol = symbol.concat(map3.get(arr[i])[0]);	//symbol
		symbol = symbol.concat("  ");
		
		name = name.concat(map3.get(arr[i])[1]);	//name
		price = price.concat(map3.get(arr[i])[2]); //price
			
		symbol = symbol.concat(name);
		symbol = symbol.concat("  ");
		
		symbol = symbol.concat(price);
		
		newData[i] = symbol;
		
		}
		
		label1.setText(newData[0]);
		label2.setText(newData[1]);
		label3.setText(newData[2]);
		label4.setText(newData[3]);
		label5.setText(newData[4]);
		label6.setText(newData[5]);
		label7.setText(newData[6]);
		label8.setText(newData[7]);
		
		lab4.setText("Number of bids:  "+ i);
		
		} 



}
	    
	