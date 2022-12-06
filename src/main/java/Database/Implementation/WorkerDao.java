package Database.Implementation;

import Database.Dto.WorkerDTO;
import Database.entity.WorkerEntity;
import Database.Interfaces.IWorkerDao;
import Database.util.EntityConverter;
import jakarta.persistence.*;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.ArrayList;

public class WorkerDao implements IWorkerDao
{
    // < Fields
    private static WorkerDao instance = new WorkerDao();

    // < Constructor
    private WorkerDao() {}

    // < Get instance
    public static WorkerDao getInstance()
    {
        if(instance == null)
            synchronized (WorkerDao.class)
            {
                if(instance == null)
                {
                    instance = new WorkerDao();

                }
            }
        return instance;
    }

    // < Create a new worker
    @Override public void CreateWorker(WorkerDTO toCreate)
    {
        // # Setup for Hibernate
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("default");
        EntityManager manager = factory.createEntityManager();
        EntityTransaction transaction = manager.getTransaction();

        try
        {
            // * Start the transaction
            transaction.begin();

            // # Create Worker (Entity)
            WorkerEntity entity = EntityConverter.toEntity(toCreate);

            // * Add the entity to the transaction (INSERT)
            manager.persist(entity);

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

    // < Get an existing worker using Id
    @Override public WorkerDTO GetWorker(int id)
    {
        // # Setup for Hibernate
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("default");
        EntityManager manager = factory.createEntityManager();
        EntityTransaction transaction = manager.getTransaction();

        WorkerDTO foundWorker;
        try
        {
            // * Start the transaction
            transaction.begin();

            // * Search for worker in database
            TypedQuery<WorkerEntity> WorkerById = manager.createNamedQuery("Worker.ById", WorkerEntity.class);
            WorkerById.setParameter(1, id);

            // # Convert from Worker (Entity) to WorkerDTO
            WorkerEntity foundInDatabase = WorkerById.getSingleResult(); // Extract the worker from the result set
            foundWorker = EntityConverter.toDTO(foundInDatabase);

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

        // # Return the Worker with the given id
        return foundWorker;
    }
    // < Get an existing worker using full name
    @Override public WorkerDTO GetWorkerByFullName(String fullName)
    {
        // < Split full name into first and last name
        String[] split = fullName.split(" ",2);
        String firstName = split[0];
        String lastName = split[1];

        // # Setup for Hibernate
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("default");
        EntityManager manager = factory.createEntityManager();
        EntityTransaction transaction = manager.getTransaction();

        WorkerDTO foundWorker;
        try
        {
            // * Start the transaction
            transaction.begin();

            // * Search for worker in database
            TypedQuery<WorkerEntity> WorkerByFullName = manager.createNamedQuery("Worker.ByFullName", WorkerEntity.class);
            WorkerByFullName.setParameter(1, firstName);
            WorkerByFullName.setParameter(2,lastName);

            // # Convert from Worker (Entity) to WorkerDTO
            WorkerEntity foundInDatabase = WorkerByFullName.getSingleResult(); // Extract the worker from the result set
            foundWorker = EntityConverter.toDTO(foundInDatabase);

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

        // # Return the Worker with the given full name
        return foundWorker;
    }
    // < Get a list of all workers
    @Override public ArrayList<WorkerDTO> getAllWorkers()
    {
        // # Setup for Hibernate
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("default");
        EntityManager manager = factory.createEntityManager();
        EntityTransaction transaction = manager.getTransaction();

        ArrayList<WorkerDTO> dtos = new ArrayList<>();
        try
        {
            // * Start the transaction
            transaction.begin();

            // * Get All workers in the database.
            TypedQuery<WorkerEntity> GetAllWorkers = manager.createNamedQuery("Worker.GetAll", WorkerEntity.class);

            // # Convert from Worker (Entity) to WorkerDTO
            ArrayList<WorkerEntity> workersEntity = (ArrayList<WorkerEntity>) GetAllWorkers.getResultList(); // Extract the worker from the result set

            // < Convert to DTOs
            for (WorkerEntity worker: workersEntity)
            {
                WorkerDTO dto = EntityConverter.toDTO(worker);
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
    // < Search parameters.
    @Override
    public ArrayList<WorkerDTO> getBySearchParameters(String fullName)
    {
        // # Get all workers in database
        ArrayList<WorkerDTO> allWorkers = getAllWorkers();

        ArrayList<WorkerDTO> result = new ArrayList<>();

        if(fullName == null)
        {
            return allWorkers;
        }


        // # Check if the string contains space or not
        if(!fullName.matches(".*\\s.*"))
        {
            for (WorkerDTO worker: allWorkers)
            {
                if(worker.firstName.toLowerCase().contains(fullName.toLowerCase()) | worker.lastName.toLowerCase().contains(fullName.toLowerCase()))
                {
                    result.add(worker);
                }
            }
        }
        else
        {
            for (WorkerDTO worker:allWorkers)
            {
                if(worker.getFullname().toLowerCase().contains(fullName.toLowerCase()))
                {
                    result.add(worker);
                }
            }
        }
        return result;
    }

    // < Update an existing worker
    @Override public void UpdateWorker(WorkerDTO toUpdate)
    {
        // # Setup for Hibernate
        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");
        SessionFactory factory = configuration.buildSessionFactory();
        Session session = factory.openSession();
        Transaction transaction = null;

        WorkerDTO workerToUpdate = GetWorker(toUpdate.workerId);
        try
        {
            // * Start the transaction
            transaction = session.beginTransaction();

            // # Update Worker (Entity)
            WorkerEntity worker = EntityConverter.toEntity(toUpdate);



            // * Update the entity in database
            session.saveOrUpdate(worker);

            // * End the transaction
            transaction.commit();
        }
        catch (HibernateException e)
        {
            // ! If something went wrong, close connection.
            if(transaction != null)
            {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        finally
        {
            // ¤ Close connection..
            session.close();
        }
    }

    // < Delete an existing worker
    @Override public void DeleteWorker(int id)
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
            WorkerEntity entity = new WorkerEntity();
            entity.setWorkerId(id);

            // * Update the entity in database
            session.delete(entity);

            // * End the transaction
            transaction.commit();
        }
        catch (HibernateException e)
        {
            // ! If something went wrong, close connection.
            if(transaction != null)
            {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        finally
        {
            // ! Close connection..
            session.close();
        }
    }

    // ? Private methods
    private static WorkerEntity Update(WorkerDTO old, WorkerDTO changes)
    {
        // ¤ Updated shift
        WorkerEntity updated = new WorkerEntity();

        updated.setWorkerId(old.workerId);

        // # First name
        if (!(old.firstName.equals(changes.firstName)) && changes.firstName != null) {
            updated.setFirstName(changes.firstName);
        } else {
            updated.setFirstName(old.firstName);
        }
        // # Last name
        if (!(old.lastName.equals(changes.lastName)) && changes.lastName != null) {
            updated.setLastName(changes.lastName);
        } else {
            updated.setLastName(old.lastName);
        }
        // # Phone
        if (old.phoneNumber != changes.phoneNumber && changes.phoneNumber != 0) {
            updated.setPhoneNumber(String.valueOf(changes.phoneNumber));
        } else {
            updated.setPhoneNumber(String.valueOf(old.phoneNumber));
        }
        // # Mail
        if (!(old.mail.equals(changes.mail)) && changes.mail != null) {updated.setMail(changes.mail);}
        else {updated.setMail(old.mail);}
        // # Address
        if (!(old.address.equals(changes.address)) && changes.address != null) {updated.setMail(changes.address);}
        else {updated.setMail(old.address);}

        return updated;
    }
}
