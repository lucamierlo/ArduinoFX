package ArduinoFX;

import com.fazecast.jSerialComm.SerialPort;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.paint.Color;

import java.io.PrintWriter;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ArduinoFXController
{
    public Label LBLstate;
    public Button BTNcnct;
    public Button BTNdiscnt;
    public ColorPicker CLR1;
    public Label LBLtime;
    public ListView LV_comports;
    private int baudRate = 9600;
    private SerialPort comPort;
    static PrintWriter outPut;
    Boolean connection = false;
    String alertitle = "unknown";
    String alertheader = "unknown";
    String alercontext = "unknown";
    ObservableList luca = FXCollections.observableArrayList();

    //test push

    /*
    Connect to the arduino
     */
    public void initialize()
    {
        time();
    }
    public void connect(ActionEvent actionEvent) {


        if (SerialPort.getCommPorts().length <= 0) {
            System.out.println("GEEN VERBINDING");
            connection = false;
        } else {
            System.out.println("COM POORT GEZIEN");
            comPort = SerialPort.getCommPort("COM3");
            SerialPort serials[] = SerialPort.getCommPorts();
            for (SerialPort serial : serials) { luca.add(serial.getPortDescription()+" "+serial.getDescriptivePortName()); }
            LV_comports.setItems(luca);
            //            System.out.println(comPort);
            comPort.setBaudRate(baudRate);
            connection = true;
        }
            //If the port is not closed, open the USB port.4
            if (connection == true) {
                try {
                    //Open the USB port and initialize the PrintWriter.
                    comPort.openPort();
                    if (comPort.isOpen() == true) {
                        System.out.println("Connection to Arduino successful.");
                        LBLstate.setText("Connected");
                    }
                    Thread.sleep(3000);
                    outPut = new PrintWriter(comPort.getOutputStream());
                    BTNcnct.setVisible(false);
                    BTNdiscnt.setVisible(true);
                    LBLstate.setTextFill(Color.LIMEGREEN);
                } catch (Exception c) {
                    System.out.println(c);
                }
            } else if (connection == false) {
                alertitle = "COM ERROR";
                alertheader = "Fatal usb error";
                alercontext = "Arduino niet verbonden met pc. Plug usb opnieuw in pc";
                alert_error(alertitle, alertheader, alercontext);
            } else {

            }
        }


    public void send(int input)
    {
        if(connection == true)
        {
            System.out.println(input);
            outPut.print(input);
            outPut.flush();
    }
        else{
            //Update the status/console if the Arduino hasn't been connected.
            alertitle = "Connection ERROR";
            alertheader = "Fatal Arduino error";
            alercontext = "Arduino niet verbonden met pc. Druk eerst op connect en verbind een Arduino";
            alert_error(alertitle, alertheader, alercontext);
            }
        }

    public void disconnect(ActionEvent actionEvent)
    {
        //Close the USB port if it's open.
        if(comPort.isOpen() == true)
        {
            //Close the port and update the console/status.
            comPort.closePort();
            System.out.println("Disconnected from Arduino.");
            LBLstate.setTextFill(Color.RED);
            LBLstate.setText("Disconnected");
            BTNcnct.setVisible(true);
            BTNdiscnt.setVisible(false);
        }

    }
    public void rainbow(ActionEvent actionEvent) { send(1); }
    public void cloud(ActionEvent actionEvent) { send(2); }
    public void whiteblack(ActionEvent actionEvent) { send(3); }
    public void redwhiteblue(ActionEvent actionEvent) { send(4); }
    public void party(ActionEvent actionEvent) { send(5); }
    public void purplegreen(ActionEvent actionEvent) { send(6); }
    public void randomcolors(ActionEvent actionEvent) { send(7); }
//    public void menuWhite(ActionEvent actionEvent) {send(8); }
//    public void MenuGreen(ActionEvent actionEvent) {send(9); }
//    public void MenuBlue(ActionEvent actionEvent) {send(10); }
//    public void MenuWhite(ActionEvent actionEvent) {send(11);}

    //needs time thread
    public void time()
    {
        LocalTime time = LocalTime.now(); // Gets the current time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String time1 = formatter.format(time);
        LBLtime.setText(time1);
    }

    public void alert_error(String title, String header, String context)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(context);
        alert.showAndWait();
    }
}




