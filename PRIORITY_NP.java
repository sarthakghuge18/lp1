import java.util.Scanner;

// Class to represent a process with priority
class PriorityProcess {
    int pid; // Process ID
    int burstTime; // Burst time of the process
    int priority; // Priority of the process (lower value = higher priority)
    int waitingTime; // Waiting time (to be calculated)
    int turnaroundTime; // Turnaround time (to be calculated)

    // Constructor to initialize process attributes
    public PriorityProcess(int pid, int burstTime, int priority) {
        this.pid = pid;
        this.burstTime = burstTime;
        this.priority = priority;
    }
}

public class PRIORITY_NP {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Input: Number of processes
        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();
        PriorityProcess[] processes = new PriorityProcess[n]; // Array of processes

        // Input: Burst time and priority for each process
        for (int i = 0; i < n; i++) {
            System.out.print("Enter burst time and priority for process " + (i + 1) + ": ");
            int bt = sc.nextInt(); // Burst time
            int priority = sc.nextInt(); // Priority (lower value = higher priority)
            processes[i] = new PriorityProcess(i + 1, bt, priority); // Create a new process
        }

        // Sort processes by priority (Bubble Sort: lower priority number = higher priority)
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (processes[j].priority > processes[j + 1].priority) {
                    // Swap processes if the current process has a lower priority
                    PriorityProcess temp = processes[j];
                    processes[j] = processes[j + 1];
                    processes[j + 1] = temp;
                }
            }
        }

        // Variables to calculate total waiting time and total turnaround time
        int totalTime = 0; // Keeps track of the current time in the schedule
        int totalWT = 0; // Total waiting time
        int totalTAT = 0; // Total turnaround time

        // Calculate waiting time and turnaround time for each process
        for (PriorityProcess p : processes) {
            p.waitingTime = totalTime; // Waiting time = total time before process starts
            totalTime += p.burstTime; // Increment total time by the burst time of the process
            p.turnaroundTime = p.waitingTime + p.burstTime; // Turnaround time = waiting time + burst time
            totalWT += p.waitingTime; // Add to total waiting time
            totalTAT += p.turnaroundTime; // Add to total turnaround time
        }

        // Print Gantt Chart (execution order of processes)
        System.out.println("Gantt Chart: ");
        for (PriorityProcess p : processes) {
            System.out.print("P" + p.pid + " ");
        }
        System.out.println("\n");

        // Print process details (PID, Burst Time, Priority, Waiting Time, Turnaround Time)
        System.out.println("Process\tBurst\tPriority\tWaiting\tTurnaround");
        for (PriorityProcess p : processes) {
            System.out.println("P" + p.pid + "\t" + p.burstTime + "\t" + p.priority + "\t" + p.waitingTime + "\t" + p.turnaroundTime);
        }

        // Print average waiting time and average turnaround time
        System.out.println("Average Waiting Time: " + (totalWT / (float) n));
        System.out.println("Average Turnaround Time: " + (totalTAT / (float) n));

        sc.close(); // Close the scanner
    }
}

