package Database.Interfaces;

import Database.Dto.ShiftDTO;

import java.util.ArrayList;

public interface IShiftDao
{
    void CreateShift(ShiftDTO toCreate);
    ShiftDTO GetShift(int id);
    ArrayList<ShiftDTO> GetAllShifts();
    ArrayList<ShiftDTO> getBySearchParameters(String date, String workerName);
    void UpdateShift(ShiftDTO toUpdate);
    void DeleteShift(int id);


}
