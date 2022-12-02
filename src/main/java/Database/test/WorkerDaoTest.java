package Database.test;

import Database.Implementation.WorkerDao;
public class WorkerDaoTest
{
    public static void main(String[] args)
    {
        WorkerDao dao = WorkerDao.getInstance();
        System.out.println(dao.getBySearchParameters("Mads"));
    }


}
