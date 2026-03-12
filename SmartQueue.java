import java.util.*;

class Patient {
    String name;
    int priority;
    int waitingTime;

    Patient(String name, int priority) {
        this.name = name;
        this.priority = priority;
        this.waitingTime = 0;
    }

    public String toString() {
        return name + " | Priority: " + priority + " | Waiting: " + waitingTime;
    }
}

public class SmartQueue {

    // HASHING (Store patient records)
    static HashMap<String, Patient> patientMap = new HashMap<>();

    // QUEUE (Normal waiting queue)
    static Queue<Patient> normalQueue = new LinkedList<>();

    // HEAP / PRIORITY QUEUE (Emergency patients)
    static PriorityQueue<Patient> emergencyQueue =
            new PriorityQueue<>((a,b)->b.priority - a.priority);

    // STACK (History of served patients)
    static Stack<Patient> servedHistory = new Stack<>();

    // LINKED LIST (All patients list)
    static LinkedList<Patient> patientList = new LinkedList<>();

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        while(true){

            System.out.println("\n--- SMARTQUEUE MENU ---");
            System.out.println("1 Add Patient");
            System.out.println("2 Call Next Patient");
            System.out.println("3 Sort Patients (Bubble Sort)");
            System.out.println("4 Display All Patients");
            System.out.println("5 Search Patient (Hashing)");
            System.out.println("6 Exit");

            int choice = sc.nextInt();
            sc.nextLine();

            switch(choice){

                case 1 -> addPatient();
                case 2 -> callNext();
                case 3 -> bubbleSort();
                case 4 -> display();
                case 5 -> searchPatient();
                case 6 -> System.exit(0);

                default -> System.out.println("Invalid choice");
            }
        }
    }

    // ADD PATIENT
    static void addPatient(){

        System.out.print("Enter Name: ");
        String name = sc.nextLine();

        System.out.println("Category");
        System.out.println("1 Normal");
        System.out.println("2 Emergency");

        int cat = sc.nextInt();

        int priority = (cat==2)?50:10;

        Patient p = new Patient(name,priority);

        // HASHING
        patientMap.put(name,p);

        // LINKED LIST
        patientList.add(p);

        // QUEUE
        if(cat==1)
            normalQueue.add(p);
        else
            emergencyQueue.add(p);

        System.out.println("Patient Added");
    }

    // CALL NEXT PATIENT
    static void callNext(){

        Patient p;

        // HEAP first
        if(!emergencyQueue.isEmpty())
            p = emergencyQueue.poll();

        // QUEUE next
        else if(!normalQueue.isEmpty())
            p = normalQueue.poll();

        else{
            System.out.println("No patients");
            return;
        }

        System.out.println("Serving: "+p.name);

        // STACK history
        servedHistory.push(p);
    }

    // SORTING (Bubble Sort)
    static void bubbleSort(){

        for(int i=0;i<patientList.size();i++){
            for(int j=0;j<patientList.size()-i-1;j++){

                if(patientList.get(j).priority <
                        patientList.get(j+1).priority){

                    Patient temp = patientList.get(j);
                    patientList.set(j,patientList.get(j+1));
                    patientList.set(j+1,temp);
                }
            }
        }

        System.out.println("Patients sorted by priority");
    }

    // DISPLAY
    static void display(){

        System.out.println("\nAll Patients");

        for(Patient p : patientList)
            System.out.println(p);
    }

    // HASHING SEARCH
    static void searchPatient(){

        System.out.print("Enter patient name: ");
        String name = sc.nextLine();

        if(patientMap.containsKey(name))
            System.out.println(patientMap.get(name));
        else
            System.out.println("Patient not found");
    }
}