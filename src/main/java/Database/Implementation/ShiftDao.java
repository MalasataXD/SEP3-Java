package Database.Implementation;

import Database.Dto.ShiftDTO;
import Database.Dto.WorkerDTO;
import Database.Interfaces.IShiftDao;
import Database.entity.ShiftsEntity;

import Database.util.EntityConverter;
import jakarta.persistence.*;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.ArrayList;

public class ShiftDao implements IShiftDao
{
    // < Fields
    private static ShiftDao instance = new ShiftDao();

    // < Constructor
    private ShiftDao() {}

    // < Get instance
    public static ShiftDao getInstance()
    {
        if(instance == null)
            synchronized (WorkerDao.class)
            {
                if(instance == null)
                {
                    instance = new ShiftDao();

                }
            }
        return instance;
    }


    // < Create a new Shift
    @Override public void CreateShift(ShiftDTO toCreate)
    {
        // # Setup for Hibernate
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("default");
        EntityManager manager = factory.createEntityManager();
        EntityTransaction transaction = manager.getTransaction();

        try
        {
            // * Start the transaction
            transaction.begin();

            // # Create Shift (Entity)
            ShiftsEntity shift = EntityConverter.toEntity(toCreate);

            // * Add the entity to the transaction (INSERT)
            manager.persist(shift);

            // * End the transaction
            transaction.commit();
        }
        finally
        {
            // ! If something went wrong, close connection.
            if(transaction.isActive())
            {
                transaction.rollback();
            }
            manager.close();
            factory.close();
        }
    }

    // < Get an existing Shift from the database, by using id as the parameter
    @Override public ShiftDTO GetShift(int id)
    {
        // # Setup for Hibernate
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("default");
        EntityManager manager = factory.createEntityManager();
        EntityTransaction transaction = manager.getTransaction();

        ShiftDTO foundShift;
        try
        {
            // * Start the transaction
            transaction.begin();

            TypedQuery<ShiftsEntity> ShiftById = manager.createNamedQuery("Shift.ById", ShiftsEntity.class);
            ShiftById.setParameter(1,id);

            // # Convert from Shift (Entity) to ShiftDTO
            ShiftsEntity foundInDatabase = ShiftById.getSingleResult(); // Extract the shift from the result set.
            foundShift = EntityConverter.toDTO(foundInDatabase);

            // * End the transaction
            transaction.commit();
        }
        finally
        {
            // ! If something went wrong, close connection.
            if(transaction.isActive())
            {
                transaction.rollback();
            }
            manager.close();
            factory.close();
        }

        // # Return the Shift with the given id
        return foundShift;
    }

    @Override
    public ArrayList<ShiftDTO> GetAllShifts()
    {
        // # Setup for Hibernate
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("default");
        EntityManager manager = factory.createEntityManager();
        EntityTransaction transaction = manager.getTransaction();

        ArrayList<ShiftDTO> dtos = new ArrayList<>();
        try
        {
            // * Start the transaction
            transaction.begin();

            // * Get All workers in the database.
            TypedQuery<ShiftsEntity> GetAllShifts = manager.createNamedQuery("Shift.GetAll", ShiftsEntity.class);

            // # Convert from Worker (Entity) to WorkerDTO
            ArrayList<ShiftsEntity> shiftsEntity = (ArrayList<ShiftsEntity>) GetAllShifts.getResultList(); // Extract the worker from the result set

            System.out.println(shiftsEntity.get(0));
            // < Convert to DTOs
            for (ShiftsEntity shift: shiftsEntity)
            {
                ShiftDTO dto = EntityConverter.toDTO(shift);
                dtos.add(dto);
            }

            // * End the transaction
            transaction.commit();
        }
        finally
        {
            // ! If something went wrong, close connection.
            if (transaction.isActive())
            {
                transaction.rollback();
            }
            manager.close();
            factory.close();
        }

        // # Return a list of all workers.
        return dtos;
    }
    @Override
    public ArrayList<ShiftDTO> getBySearchParameters(String date, String workerName)
    {
        ArrayList<ShiftDTO> allShifts = GetAllShifts();
        ArrayList<ShiftDTO> result = new ArrayList<>();

        // # Date is set and workername is not.
        if(date != null && workerName == null)
        {
            for (ShiftDTO shift :allShifts)
            {
                if(shift.date.equals(date))
                {
                    result.add(shift);
                }
            }
        }

        // # Workername is set and date is not.
        if(workerName != null && date == null)
        {
            for (ShiftDTO shift : allShifts)
            {
                WorkerDTO worker = WorkerDao.getInstance().GetWorker(shift.workerId);
                if(worker.firstName.equalsIgnoreCase(workerName) | worker.lastName.equalsIgnoreCase(workerName) | worker.getFullname().equalsIgnoreCase(workerName))
                {
                    result.add(shift);
                }
            }
        }

        // # Both are set
        if(date != null && workerName != null)
        {
            for (ShiftDTO shift :allShifts)
            {
                if(shift.date.equals(date))
                {
                    WorkerDTO worker = WorkerDao.getInstance().GetWorker(shift.workerId);
                    if(worker.firstName.equalsIgnoreCase(workerName) | worker.lastName.equalsIgnoreCase(workerName) | worker.getFullname().equalsIgnoreCase(workerName))
                    {
                        result.add(shift);
                    }
                }
            }
        }


        return result;
    }

    // < Update an existing Shift in the database, by using id as the parameter
    @Override public void UpdateShift(ShiftDTO toUpdate)
    {
        // # Setup for Hibernate
        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");
        SessionFactory factory = configuration.buildSessionFactory();
        Session session = factory.openSession();
        Transaction transaction = null;

        // # Get the existing shift with the given id
        ShiftDTO shiftToUpdate = GetShift(toUpdate.shiftId);
        try
        {
            // * Start the transaction
            transaction =  session.beginTransaction();

            // ¤ Update the information in the shift.
            ShiftsEntity updated = Update(shiftToUpdate,toUpdate);

            // * Update the shift in the database with the given id
            session.saveOrUpdate(updated);

            // * End the transaction
            transaction.commit();
        }
        catch (HibernateException e)
        {
            // ! If something went wrong, close connection
            if(transaction != null)
            {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        finally
        {
            // ¤ Close connection...
            session.close();
        }
    }

    // < Delete an existing Shift from the database, by using id as the parameter
    @Override public void DeleteShift(int id)
    {
        // # Setup for Hibernate
        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");
        SessionFactory factory = configuration.buildSessionFactory();
        Session session = factory.openSession();
        Transaction transaction = null;

        try
        {
            // * Start the transaction
            transaction = session.beginTransaction();

            // # Delete Worker (Entity)
            ShiftsEntity entity = new ShiftsEntity();
            entity.setVagtId(id);

            // * Update the entity in database
            session.delete(entity);

            // * End the transaction
            transaction.commit();
        }
        catch (HibernateException e)
        {
            // ! If something went wrong, close connection
            if(transaction != null)
            {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        finally
        {
            // ¤ Close connection...
            session.close();
        }
    }

    // ? Private methods
    private static ShiftsEntity Update(ShiftDTO old, ShiftDTO changes)
    {
        // ¤ Updated shift
        ShiftsEntity updated = new ShiftsEntity();

        updated.setVagtId(old.shiftId);

        // # Compare Date
        if(!(old.date.equals(changes.date)) && changes.date != null) {updated.setDate(changes.date);}
        else {updated.setDate(old.date);}

        // # Compare From hour + minute
        if(old.fromHour != changes.fromHour && changes.fromHour != 0) {updated.setFromHour(changes.fromHour);}
        else { updated.setFromHour(old.fromHour);}

        if(old.fromMinute != changes.fromMinute && changes.fromMinute != 0) {updated.setFromMinute(changes.fromMinute);}
        else { updated.setFromMinute(old.fromMinute);}

        // # Compare to Hour + minute
        if(old.toHour != changes.toHour && changes.toHour != 0) {updated.setToHour(changes.toHour);}
        else { updated.setToHour(old.toHour);}

        if(old.toMinute != changes.toMinute && changes.toMinute != 0) {updated.setToMinute(changes.toMinute);}
        else { updated.setToMinute(old.toMinute);}

        // # Compare workerId
        if(old.workerId != changes.workerId && changes.workerId != 0) {updated.setWorkerId(changes.workerId);}
        else { updated.setWorkerId(old.workerId);}

        // # Compare BossId
        if(old.bossId != changes.bossId && changes.bossId != 0) {updated.setBossId(changes.bossId);}
        else { updated.setBossId(old.bossId);}

        return updated;
    }



}