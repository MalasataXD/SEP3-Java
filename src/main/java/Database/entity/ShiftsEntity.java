package Database.entity;

import jakarta.persistence.*;

@Entity
@NamedQuery(name="Shift.ById",query = "SELECT s FROM ShiftsEntity s where s.vagtId  = ?1")
@NamedQuery(name="Shift.GetAll",query = "SELECT s FROM ShiftsEntity s")
@Table(name = "shifts", schema = "vagtplanssystem", catalog = "")
public class ShiftsEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "VagtId")
    private long vagtId;
    @Basic
    @Column(name = "Date")
    private String date;
    @Basic
    @Column(name = "FromHour")
    private int fromHour;
    @Basic
    @Column(name = "FromMinute")
    private int fromMinute;
    @Basic
    @Column(name = "ToHour")
    private int toHour;
    @Basic
    @Column(name = "ToMinute")
    private int toMinute;
    @Basic
    @Column(name = "WorkerId")
    private int workerId;
    @Basic
    @Column(name = "BreakAmount")
    private int breakAmount;
    @Basic
    @Column(name = "BossId")
    private int bossId;

    public long getVagtId() {
        return vagtId;
    }

    public void setVagtId(long vagtId) {
        this.vagtId = vagtId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getFromHour() {
        return fromHour;
    }

    public void setFromHour(int fromHour) {
        this.fromHour = fromHour;
    }

    public int getFromMinute() {
        return fromMinute;
    }

    public void setFromMinute(int fromMinute) {
        this.fromMinute = fromMinute;
    }

    public int getToHour() {
        return toHour;
    }

    public void setToHour(int toHour) {
        this.toHour = toHour;
    }

    public int getToMinute() {
        return toMinute;
    }

    public void setToMinute(int toMinute) {
        this.toMinute = toMinute;
    }

    public int getWorkerId() {
        return workerId;
    }

    public void setWorkerId(int workerId) {
        this.workerId = workerId;
    }

    public int getBreakAmount() {
        return breakAmount;
    }

    public void setBreakAmount(int breakAmount) {
        this.breakAmount = breakAmount;
    }

    public int getBossId() {
        return bossId;
    }

    public void setBossId(int bossId) {
        this.bossId = bossId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ShiftsEntity that = (ShiftsEntity) o;

        if (vagtId != that.vagtId) return false;
        if (fromHour != that.fromHour) return false;
        if (fromMinute != that.fromMinute) return false;
        if (toHour != that.toHour) return false;
        if (toMinute != that.toMinute) return false;
        if (workerId != that.workerId) return false;
        if (breakAmount != that.breakAmount) return false;
        if (bossId != that.bossId) return false;
        if (date != null ? !date.equals(that.date) : that.date != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (vagtId ^ (vagtId >>> 32));
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + fromHour;
        result = 31 * result + fromMinute;
        result = 31 * result + toHour;
        result = 31 * result + toMinute;
        result = 31 * result + workerId;
        result = 31 * result + breakAmount;
        result = 31 * result + bossId;
        return result;
    }

    @Override
    public String toString() {
        return "ShiftsEntity{" +
                "vagtId=" + vagtId +
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
