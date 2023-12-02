package edu.unm.gui;

public class Configuration {
    public static boolean gevEnabled = false;
    public static boolean voterRegEnabled = true;
    public static boolean tabEnabled = false;
    public static boolean staffEnabled = true;

    public static boolean isGevEnabled() {
        return gevEnabled;
    }

    public static void setGevEnabled(boolean gevEnabled) {
        Configuration.gevEnabled = gevEnabled;
    }

    public static boolean isVoterRegEnabled() {
        return voterRegEnabled;
    }

    public static void setVoterRegEnabled(boolean voterRegEnabled) {
        Configuration.voterRegEnabled = voterRegEnabled;
    }

    public static boolean isTabEnabled() {
        return tabEnabled;
    }

    public static void setTabEnabled(boolean tabEnabled) {
        Configuration.tabEnabled = tabEnabled;
    }

    public static boolean isStaffEnabled() {
        return staffEnabled;
    }

    public static void setStaffEnabled(boolean staffEnabled) {
        Configuration.staffEnabled = staffEnabled;
    }
}
