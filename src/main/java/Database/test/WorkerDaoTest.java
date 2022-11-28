package Database.test;

import Database.Dto.WorkerDTO;
import Database.Implementation.ShiftDao;
import Database.Implementation.WorkerDao;

public class WorkerDaoTest
{
    public static void main(String[] args) throws Exception
    {
        WorkerDao dao = WorkerDao.getInstance();
        dao.DeleteWorker(12);
    }


}
