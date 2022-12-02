package Database.util;

import Database.Dto.ShiftDTO;
import Database.Dto.WorkerDTO;
import Database.entity.ShiftsEntity;
import Database.entity.WorkerEntity;


public class EntityConverter
{

    // < Convert to DTOs from Entity

    // ¤ Shift
    public static ShiftsEntity toEntity(ShiftDTO toConvert)
    {
        ShiftsEntity shift = new ShiftsEntity();
        if(toConvert.shiftId != 0)
        {shift.setVagtId(toConvert.shiftId);}
        shift.setDate((toConvert.date));
        shift.setFromHour(toConvert.fromHour);
        shift.setFromMinute(toConvert.fromMinute);
        shift.setToHour(toConvert.toHour);
        shift.setToMinute(toConvert.toMinute);
        shift.setBreakAmount(toConvert.breakAmount);
        shift.setWorkerId(toConvert.workerId);
        shift.setBossId(toConvert.bossId);
        return shift;
    }

    // ¤ Worker
    public static WorkerEntity toEntity(WorkerDTO toConvert)
    {
        WorkerEntity worker = new WorkerEntity();
        if(toConvert.workerId != 0)
        {worker.setWorkerId(toConvert.workerId);}
        worker.setFirstName(toConvert.firstName);
        worker.setLastName(toConvert.lastName);
        worker.setMail(toConvert.mail);
        worker.setAddress(toConvert.address);
        worker.setPhoneNumber(String.valueOf(toConvert.phoneNumber));
        return worker;
    }

    // < Convert to Entities from DTO

    // ¤ Shift
    public static ShiftDTO toDTO(ShiftsEntity toConvert)
    {
        ShiftDTO shift = new ShiftDTO
                (
                        (int)toConvert.getVagtId(),
                        toConvert.getDate(),
                        toConvert.getFromHour(),
                        toConvert.getFromMinute(),
                        toConvert.getToHour(),
                        toConvert.getToMinute(),
                        toConvert.getWorkerId(),
                        toConvert.getBreakAmount(),
                        toConvert.getBossId()
                );
        return shift;
    }

    // ¤ Worker
    public static WorkerDTO toDTO(WorkerEntity toConvert)
    {
        WorkerDTO worker = new WorkerDTO
                (
                        (int) toConvert.getWorkerId(),
                        toConvert.getFirstName(),
                        toConvert.getLastName(),
                        Integer.parseInt(toConvert.getPhoneNumber()),
                        toConvert.getMail(),
                        toConvert.getAddress()
                );
        return worker;
    }






}
