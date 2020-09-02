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

public class ArduinoFXController {
    public Label LBLstate;
    public Button BTNcnct;
    public Button BTNdiscnt;
    public ColorPicker CLR1;
    public Label LBLtime;
    public ChoiceBox CH_comports;
    private int baudRate = 9600;
    public int choice = 0;
    private SerialPort comPort;
    static PrintWriter outPut;
    int COMchoice = -1;
    int connection = 0;
    String alertitle = "unknown";
    String alertheader = "unknown";
    String alercontext = "unknown";
    ObservableList luca = FXCollections.observableArrayList();


    /*
    Connect to the arduino
     */
    public void initialize() {

            SerialPort serials[] = SerialPort.getCommPorts();
            for (SerialPort serial : serials) {
                System.out.println(serials.length +"1212");
                luca.add(serial.getDescriptivePortName());
            }
            luca.add("test2");
            CH_comports.setItems(luca);
            time();


    }

    public void Clickedchoice() {
        System.out.println("hoi");

        CH_comports.getSelectionModel().selectedIndexProperty().addListener((observableValue, number,number2) -> {
            System.out.println(CH_comports.getSelectionModel().getSelectedIndex());
            COMchoice = CH_comports.getSelectionModel().getSelectedIndex();

        });

    }
    public void connect(ActionEvent actionEvent) {
        String comport1 = "";
        System.out.println(COMchoice + "COMCHOICE");

        if (SerialPort.getCommPorts().length <= 0) {
            System.out.println("GEEN VERBINDING");
            connection = 0;
        }else if (COMchoice == -1)
        {
            System.out.println("not selected one");
            connection = -1;

        } else {
            System.out.println("COM POORT GEZIEN");
            Object serial = CH_comports.getSelectionModel().getSelectedItem();
            for (int i = 0; i <= 256; i++) {
                if (serial.toString().contains("COM" + i)) {
                    System.out.println("het is COM" + i);
                    comport1 = "COM" + i;
                }
            }
            comPort = SerialPort.getCommPort(comport1);
            System.out.println(serial.toString());
            comPort.setBaudRate(baudRate);

            connection = 1;
        }
            //If the port is not closed, open the USB port.4
            if (connection == 1) {
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
            } else if (connection == 0 ) {
                alertitle = "COM ERROR";
                alertheader = "Fatal usb error";
                alercontext = "Arduino niet verbonden met pc. Plug usb opnieuw in pc";
                alert_error(alertitle, alertheader, alercontext);
            } else {
                alertitle = "COM ERROR";
                alertheader = "no com port selected";
                alercontext = "u heeft geen compoort aangeklikt ";
                alert_error(alertitle, alertheader, alercontext);
            }
        }


    public void send(int input)
    {
        if(connection == 1)
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




