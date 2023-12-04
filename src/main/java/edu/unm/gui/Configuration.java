package edu.unm.gui;

// This class allows the enabling and disabling of certain features in the program
// For example, before the ballot if officially opened, voting should not take place, so
// the "GEV" feature should be disabled. Only features that require enabling/disabling
// are included. The "Staff" feature must always be accessible so, it is not possible
// to disabled it.
public class Configuration {
    public static boolean gevEnabled = false;
    public static boolean voterRegEnabled = true;
    public static boolean tabEnabled = false;

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

}
