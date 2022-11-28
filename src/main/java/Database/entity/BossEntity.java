package Database.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "boss", schema = "vagtplanssystem", catalog = "")
public class BossEntity {
    @Basic
    @Column(name = "BossId")
    private long bossId;
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "WorkerId")
    private int workerId;

    public long getBossId() {
        return bossId;
    }

    public void setBossId(long bossId) {
        this.bossId = bossId;
    }

    public int getWorkerId() {
        return workerId;
    }

    public void setWorkerId(int workerId) {
        this.workerId = workerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BossEntity that = (BossEntity) o;

        if (bossId != that.bossId) return false;
        if (workerId != that.workerId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (bossId ^ (bossId >>> 32));
        result = 31 * result + workerId;
        return result;
    }
}
