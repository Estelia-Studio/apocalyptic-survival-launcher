package fr.amiriastudio.apocalypticlauncher;

import fr.litarvan.openauth.microsoft.MicrosoftAuthenticationException;
import fr.theshark34.openlauncherlib.util.ramselector.RamSelector;
import fr.theshark34.swinger.event.SwingerEvent;
import fr.theshark34.swinger.event.SwingerEventListener;
import fr.theshark34.swinger.textured.STexturedButton;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

import static fr.amiriastudio.apocalypticlauncher.Frame.getBufferedImage;
import static fr.amiriastudio.apocalypticlauncher.Frame.getImage;

public class Panel extends JPanel implements SwingerEventListener {

    private Image background = getImage("launcher_apo_home.png");
    private STexturedButton play1 = new STexturedButton(getBufferedImage("launcher_apo_play1.png"), getBufferedImage("launcher_apo_play2.png"));
    private STexturedButton quit = new STexturedButton(getBufferedImage("launcher_apo_quit1.png"), getBufferedImage("launcher_apo_quit2.png"));
    private STexturedButton ram = new STexturedButton(getBufferedImage("launcher_apo_ram1.png"), getBufferedImage("launcher_apo_ram2.png"));
    private RamSelector ramSelector = new RamSelector(Frame.getRamFile());
    public Panel() throws IOException {
        this.setLayout(null);

        play1.setBounds(200, 91);
        play1.setLocation(540, 507);
        play1.addEventListener(this);
        this.add(play1);

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
        if (swingerEvent.getSource() == quit) {
            System.exit(0);
        } else if (swingerEvent.getSource() == play1) {
            ramSelector.save();
            this.play1.setEnabled(false);

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
            this.play1.setEnabled(true);
        } else if (swingerEvent.getSource() == ram) {
            ramSelector.display();
        }
    }

    public RamSelector getRamSelector() {
        return ramSelector;
    }

}
