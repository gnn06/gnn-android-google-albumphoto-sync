package gnn.com.googlealbumdownloadappnougat.wizard;

public enum WizardStep {
    S00_NOT_STARTED,
    S01_LOGIN_AND_AUTHORISE,
    S03_CHOOSE_ALBUM,
    S05_CHOOSE_FOLDER_AND_ASK_PERMISSION,
    // éventuellement choisir le nombre de photo à synchroniser)
    // (faire un test de synchro)
    S06_ACTIVATE_LIVEWALLPAPER,
    S07_CHOOSE_WALLPAPER_FREQUENCY,
    S08_CHOOSE_DOWNLOAD_FREQUENCY,
    S09_CHOOSE_UPDATE_FREQUENCY,
    S10_ACTIVATE_SCHEDULER,
    S11_FINISHED
}
