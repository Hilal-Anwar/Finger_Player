package sample;

public class Clock {
    int h,m,s;
    public  String CalculateTime(int time) {
        if (time >= 60) {
            h = time / 3600;
            m = time % 3600;
        }
        if (time < 60)
            s = time;
        if (m >= 60) {
            s = m % 60;
            m = m / 60;
        }
        String hours, minutes, seconds;
        if (h <= 9)
            hours = "0" + h;
        else
            hours = "" + h;
        if (m <= 9)
            minutes = "0" + m;
        else
            minutes = "" + m;
        if (s <= 9)
            seconds = "0" + s;
        else
            seconds = "" + s;
        return hours + ":" + minutes + ":" + seconds;
    }
}
