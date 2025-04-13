package fr.amiriastudio.apocalypticlauncher;

import fr.theshark34.openlauncherlib.util.ramselector.RamSelector;
import fr.theshark34.swinger.event.SwingerEvent;
import fr.theshark34.swinger.event.SwingerEventListener;
import fr.theshark34.swinger.textured.STexturedButton;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import static fr.amiriastudio.apocalypticlauncher.Frame.getBufferedImage;
import static fr.amiriastudio.apocalypticlauncher.Frame.getImage;

public class Panel extends JPanel implements SwingerEventListener {

    private Image background = getImage("launcher_apo_home.png");
    private STexturedButton play1 = new STexturedButton(getBufferedImage("launcher_apo_play1.png"), getBufferedImage("launcher_apo_play2.png"));
    private STexturedButton incorrect_version = new STexturedButton(getBufferedImage("launcher_apo_incorrect_version.png"), getBufferedImage("launcher_apo_incorrect_version.png"));
    private STexturedButton quit = new STexturedButton(getBufferedImage("launcher_apo_quit1.png"), getBufferedImage("launcher_apo_quit2.png"));
    private STexturedButton ram = new STexturedButton(getBufferedImage("launcher_apo_ram1.png"), getBufferedImage("launcher_apo_ram2.png"));
    private STexturedButton launching = new STexturedButton(getBufferedImage("launcher_apo_launching.png"), getBufferedImage("launcher_apo_launching.png"));
    private RamSelector ramSelector = new RamSelector(Frame.getRamFile());
    public Panel() throws IOException {
        String launcher_version = "1.4.0";
        String debug = "false";

        this.setLayout(null);

        incorrect_version.setBounds(600, 350);
        incorrect_version.setLocation(340, 307);
        incorrect_version.addEventListener(this);
        this.add(incorrect_version);

        launching.setBounds(300, 150);
        launching.setLocation(445, 307);
        launching.addEventListener(this);
        this.add(launching);
        launching.setVisible(false);

        play1.setBounds(200, 91);
        play1.setLocation(500, 507);
        play1.addEventListener(this);
        this.add(play1);

        ram.setBounds(100, 100);
        ram.setLocation(1170, 600);
        ram.addEventListener(this);
        this.add(ram);

        JSONParser parser = new JSONParser();
        try {
            URL url = new URL("https://apocalyptic-survival.wstr.fr/public/launcher-version.json");
            InputStream is = url.openStream();
            JSONObject jsonObject = (JSONObject) parser.parse(new InputStreamReader(is));
            String version = (String) jsonObject.get("version");
            System.out.println("Version Serveur: " + version);
            System.out.println("Version CLient: " + launcher_version);
            if (version.equals(launcher_version)) {
                this.incorrect_version.setVisible(false);
                this.play1.setVisible(true);
                this.ram.setVisible(true);
            } else {
                if (debug.equals("false")) {
                    this.incorrect_version.setVisible(true);
                    this.play1.setVisible(false);
                    this.ram.setVisible(false);
                } else {
                    this.incorrect_version.setVisible(false);
                    this.play1.setVisible(true);
                    this.ram.setVisible(true);
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }


        quit.setBounds(34, 33);
        quit.setLocation(1235, 10);
        quit.addEventListener(this);
        this.add(quit);

    }

    @Override
    public void paintComponent (Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, this.getWidth(), this.getHeight(),this);
    }

    @Override
    public void onEvent(SwingerEvent swingerEvent) {
        if (swingerEvent.getSource() == quit) {
            System.exit(0);
        } else if (swingerEvent.getSource() == play1) {
            ramSelector.save();
            play1.setVisible(false);
            launching.setVisible(true);

            new Thread(() -> {
                try {
                    if(Launcher.getAuthInfos() == null)
                        Launcher.auth();
                    Launcher.update();
                    Launcher.launch();
                } catch (Exception e) {
                    Launcher.getReporter().catchError(e, "Impossible de lancer le launcher");
                }
            }).start();
        } else if (swingerEvent.getSource() == ram) {
            ramSelector.display();
        }
    }

    public RamSelector getRamSelector() {
        return ramSelector;
    }

}
