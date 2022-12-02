package Database.Interfaces;

import Database.Dto.WorkerDTO;

import java.util.ArrayList;

public interface IWorkerDao
{
    // < Methods
    void CreateWorker(WorkerDTO toCreate);
    WorkerDTO GetWorker(int id);
    WorkerDTO GetWorkerByFullName(String fullName);
    ArrayList<WorkerDTO> getAllWorkers();
    ArrayList<WorkerDTO> getBySearchParameters(String fullName);
    void UpdateWorker(WorkerDTO toUpdate);
    void DeleteWorker(int id);

}
