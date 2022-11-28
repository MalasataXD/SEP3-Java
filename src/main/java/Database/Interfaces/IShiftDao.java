package Database.Interfaces;

import Database.Dto.ShiftDTO;
import Database.entity.ShiftsEntity;

public interface IShiftDao
{
    void CreateShift(ShiftDTO toCreate);
    ShiftDTO GetShift(int id) throws Exception;
    void UpdateShift(ShiftDTO toUpdate) throws Exception;
    void DeleteShift(int id);


}
