package Database.Interfaces;

import Database.Dto.WorkerDTO;

public interface IWorkerDao
{
    // < Methods
    void CreateWorker(WorkerDTO toCreate);
    WorkerDTO GetWorker(int id);
    void UpdateWorker(WorkerDTO toUpdate);
    void DeleteWorker(int id);
}
