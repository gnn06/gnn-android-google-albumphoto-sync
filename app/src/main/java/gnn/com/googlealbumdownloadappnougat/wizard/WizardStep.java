package gnn.com.googlealbumdownloadappnougat.wizard;

public enum WizardStep {
    S00_NOT_STARTED,
    S01_LOGIN,
    S02_ASK_GOOGLE_PERMISSION,
    S03_CHOOSE_ALBUM,
    S04_ASK_WRITE_PERMISSION,
    S05_CHOOSE_FOLDER,
    // éventuellement choisir le nombre de photo à synchroniser)
    // (faire un test de synchro)
    S06_ACTIVATE_LIVEWALLPAPER,
    S07_CHOOSE_WALLPAPER_FREQUENCY,
    S08_CHOOSE_DOWNLOAD_FREQUENCY,
    S09_CHOOSE_UPDATE_FREQUENCY,
    S10_ACTIVATE_SCHEDULER,
    S11_FINISHED
}
