package tk.tuxclient.util.http.gson;

public class ObjIsWhitelisted {

    private String hwid;
    private int isWhitelisted;

    public String getHwid() {
        return hwid;
    }

    public boolean isWhitelisted() {
        return isWhitelisted == 1;
    }
}
