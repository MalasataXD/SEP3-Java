package Database.Dto;

public class WorkerDTO
{
    public int workerId;
    public String firstName;
    public String lastName;
    public int phoneNumber;
    public String mail;
    public String address;

    // < Constructors
    public WorkerDTO(int workerId, String firstName, String lastName, int phoneNumber, String mail, String address)
    {
        this.workerId = workerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.mail = mail;
        this.address = address;
    }

    public WorkerDTO(String firstName, String lastName, int phoneNumber, String mail, String address)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.mail = mail;
        this.address = address;
    }

    public WorkerDTO()
    {
        // ! EMPTY
    }



    // < ToString
    @Override
    public String toString()
    {
        return "WorkerDTO{" +
                "workerId=" + workerId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber=" + phoneNumber +
                ", mail='" + mail + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
