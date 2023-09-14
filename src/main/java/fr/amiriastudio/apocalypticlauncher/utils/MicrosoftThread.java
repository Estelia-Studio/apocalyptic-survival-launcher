package fr.amiriastudio.apocalypticlauncher.utils;

import fr.amiriastudio.apocalypticlauncher.Launcher;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticationException;

public class MicrosoftThread implements Runnable{
    @Override
    public void run() {
        try {
            Launcher.auth();
        } catch (MicrosoftAuthenticationException e) {
            Launcher.getReporter().catchError(e, "Erreur 001: Impossible de vous connectez au launcher !");
        }
    }
}
