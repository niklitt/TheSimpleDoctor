import javax.swing.*;
import java.io.IOException;
import java.sql.SQLException;

public class Driver {
    public static void main(String[] args) throws IOException, SQLException {
        Gui gui = new Gui();
        gui.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
