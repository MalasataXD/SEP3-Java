package Database.Dto;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class ShiftDTO
{
    // < Fields
    public int shiftId; // * Shift id
    public String date; // * When? (10-11-2022)
    // * From when? (16:30)
    public int fromHour;
    public int fromMinute;
    // * To when? (21:30)
    public int toHour;
    public int toMinute;
    public int workerId; // * Who is working the shift?
    public int breakAmount; // * How much break?
    public int bossId; // * Who assigned the shift?


    // < Constructors
    public ShiftDTO(String date, int fromHour, int fromMinute, int toHour, int toMinute, int workerId, int breakAmount, int bossId)
    {
        this.date = date;
        this.fromHour = fromHour;
        this.fromMinute = fromMinute;
        this.toHour = toHour;
        this.toMinute = toMinute;
        this.workerId = workerId;
        this.breakAmount = breakAmount;
        this.bossId = bossId;
    }

    public ShiftDTO(long vagtId, String date, int fromHour, int fromMinute, int toHour, int toMinute, int workerId, int breakAmount, int bossId)
    {
        this.shiftId = (int) vagtId;
        this.date = date;
        this.fromHour = fromHour;
        this.fromMinute = fromMinute;
        this.toHour = toHour;
        this.toMinute = toMinute;
        this.workerId = workerId;
        this.breakAmount = breakAmount;
        this.bossId = bossId;
    }

    // < ToString
    @Override
    public String toString()
    {
        return "ShiftDTO{" +
                "shiftId=" + shiftId +
                ", date='" + date + '\'' +
                ", fromHour=" + fromHour +
                ", fromMinute=" + fromMinute +
                ", toHour=" + toHour +
                ", toMinute=" + toMinute +
                ", workerId=" + workerId +
                ", breakAmount=" + breakAmount +
                ", bossId=" + bossId +
                '}';
    }
}
