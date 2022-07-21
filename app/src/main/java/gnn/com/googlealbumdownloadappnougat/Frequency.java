package gnn.com.googlealbumdownloadappnougat;

public class Frequency {

    public static final int NEVER = -1;
    public static final int ALWAYS = 0;
    private final int value;

    public Frequency(String s) {
        this.value = Integer.parseInt(s);
    }

    public boolean equals(int obj) {
        return this.value == obj;
    }

    public String toString() {
        return String.valueOf(value);
    }
}
