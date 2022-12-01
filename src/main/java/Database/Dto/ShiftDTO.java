package Database.Dto;

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

    public ShiftDTO() {}

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

    public int getShiftId() {
        return shiftId;
    }

    public String getDate() {
        return date;
    }

    public int getFromHour() {
        return fromHour;
    }

    public int getFromMinute() {
        return fromMinute;
    }

    public int getToHour() {
        return toHour;
    }

    public int getToMinute() {
        return toMinute;
    }

    public int getWorkerId() {
        return workerId;
    }

    public int getBreakAmount() {
        return breakAmount;
    }

    public int getBossId() {
        return bossId;
    }

    public void setShiftId(int shiftId) {
        this.shiftId = shiftId;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setFromHour(int fromHour) {
        this.fromHour = fromHour;
    }

    public void setFromMinute(int fromMinute) {
        this.fromMinute = fromMinute;
    }

    public void setToHour(int toHour) {
        this.toHour = toHour;
    }

    public void setToMinute(int toMinute) {
        this.toMinute = toMinute;
    }

    public void setWorkerId(int workerId) {
        this.workerId = workerId;
    }

    public void setBreakAmount(int breakAmount) {
        this.breakAmount = breakAmount;
    }

    public void setBossId(int bossId) {
        this.bossId = bossId;
    }
}
