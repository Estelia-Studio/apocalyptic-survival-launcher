package fr.amiriastudio.apocalypticlauncher;

import fr.amiriastudio.apocalypticlauncher.utils.MicrosoftThread;
import fr.theshark34.openlauncherlib.util.ramselector.RamSelector;
import fr.theshark34.swinger.event.SwingerEvent;
import fr.theshark34.swinger.event.SwingerEventListener;
import fr.theshark34.swinger.textured.STexturedButton;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

import static fr.amiriastudio.apocalypticlauncher.Frame.*;

public class Panel extends JPanel implements SwingerEventListener {

    private Image background = getImage("launcher_apo_home.png");
    private STexturedButton play1 = new STexturedButton(getBufferedImage("launcher_apo_play1.png"), getBufferedImage("launcher_apo_play2.png"));
    private STexturedButton play2 = new STexturedButton(getBufferedImage("launcher_apo_play3.png"), getBufferedImage("launcher_apo_play3.png"));
    private STexturedButton connect1 = new STexturedButton(getBufferedImage("launcher_apo_connect1.png"), getBufferedImage("launcher_apo_connect2.png"));
    private STexturedButton connect2 = new STexturedButton(getBufferedImage("laucnher_apo_connect3.png"), getBufferedImage("laucnher_apo_connect3.png"));
    private STexturedButton quit = new STexturedButton(getBufferedImage("launcher_apo_quit1.png"), getBufferedImage("launcher_apo_quit2.png"));
    private STexturedButton ram = new STexturedButton(getBufferedImage("launcher_apo_ram1.png"), getBufferedImage("launcher_apo_ram2.png"));
    private RamSelector ramSelector = new RamSelector(Frame.getRamFile());
    public Panel() throws IOException {
        this.setLayout(null);



        final String token = Frame.getSaver().get("refresh_token");
        if (token != null && !token.isEmpty()) {
            connect2.setBounds(233, 90);
            connect2.setLocation(410, 557);
            connect2.addEventListener(this);
            this.add(connect2);
            play1.setBounds(200, 91);
            play1.setLocation(670, 557);
            play1.addEventListener(this);
            this.add(play1);
        } else {
            connect1.setBounds(233, 90);
            connect1.setLocation(410, 557);
            connect1.addEventListener(this);
            this.add(connect1);
            play2.setBounds(200, 91);
            play2.setLocation(670, 557);
            play2.addEventListener(this);
            this.add(play2);
        }

        quit.setBounds(34, 33);
        quit.setLocation(1215, 10);
        quit.addEventListener(this);
        this.add(quit);

        ram.setBounds(100, 100);
        ram.setLocation(1170, 70);
        ram.addEventListener(this);
        this.add(ram);

    }

    @Override
    public void paintComponent (Graphics g) {
        super.paintComponent(g);

        g.drawImage(background, 0, 0, this.getWidth(), this.getHeight(), this);
    }

    @Override
    public void onEvent(SwingerEvent swingerEvent) {
        if (swingerEvent.getSource() == connect1) {
            Thread t = new Thread(new MicrosoftThread());
            t.start();
        } else if (swingerEvent.getSource() == quit) {
            System.exit(0);
        } else if (swingerEvent.getSource() == play1) {
            ramSelector.save();

            try {
                Launcher.update();
            } catch (Exception e) {
                Launcher.getReporter().catchError(e, "Erreur 002: Impossible de mettre à jour le launcher");
            }

            try {
                Launcher.launch();
            } catch (Exception e) {
                Launcher.getReporter().catchError(e, "Erreur 003: Impossible de lancer le jeu");
            }
        } else if (swingerEvent.getSource() == ram) {
            ramSelector.display();
        }
    }

    public RamSelector getRamSelector() {
        return ramSelector;
    }
}
