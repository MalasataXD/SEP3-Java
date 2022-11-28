package Database.test;

import Database.Implementation.ShiftDao;

public class ShiftDaoTest
{
    public static void main(String[] args) {
        ShiftDao dao = ShiftDao.getInstance();
        System.out.println(dao.GetShift(1));
    }
}
