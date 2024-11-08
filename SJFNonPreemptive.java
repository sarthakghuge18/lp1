import java.util.Scanner;

// Class to represent a process with its attributes
class SJFNProcess {
    int pid;               // Process ID
    int burstTime;         // Burst Time of the process
    int arrivalTime;       // Arrival Time of the process
    int waitingTime;       // Waiting Time of the process
    int turnaroundTime;    // Turnaround Time of the process

    // Constructor to initialize the process attributes
    public SJFNProcess(int pid, int burstTime, int arrivalTime) {
        this.pid = pid;
        this.burstTime = burstTime;
        this.arrivalTime = arrivalTime;
    }
}

public class SJFNonPreemptive {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Input: Number of processes
        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();
        SJFNProcess[] processes = new SJFNProcess[n];

        // Input: Arrival time and burst time for each process
        for (int i = 0; i < n; i++) {
            System.out.print("Enter arrival time and burst time for process " + (i + 1) + ": ");
            int at = sc.nextInt();  // Arrival time
            int bt = sc.nextInt();  // Burst time
            processes[i] = new SJFNProcess(i + 1, bt, at);
        }

        // Sorting processes by arrival time first and burst time second (SJF rule)
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (processes[j].burstTime > processes[j + 1].burstTime) {
                    SJFNProcess temp = processes[j];
                    processes[j] = processes[j + 1];
                    processes[j + 1] = temp;
                }
            }
        }

        int totalTime = 0;  // Keeps track of the current time
        int totalWT = 0;    // Total Waiting Time
        int totalTAT = 0;   // Total Turnaround Time

        // Calculate waiting time and turnaround time for each process
        for (SJFNProcess p : processes) {
            p.waitingTime = totalTime - p.arrivalTime; // Calculate waiting time
            totalTime += p.burstTime;                 // Add burst time to the total time
            p.turnaroundTime = p.waitingTime + p.burstTime; // Calculate turnaround time
            totalWT += p.waitingTime;                 // Add to total waiting time
            totalTAT += p.turnaroundTime;             // Add to total turnaround time
        }

        // Output: Gantt Chart
        System.out.println("Gantt Chart: ");
        for (SJFNProcess p : processes) {
            System.out.print("P" + p.pid + " ");
        }
        System.out.println("\n");

        // Output: Process details with calculated metrics
        System.out.println("Process\tArrival\tBurst\tWaiting\tTurnaround");
        for (SJFNProcess p : processes) {
            System.out.println("P" + p.pid + "\t" + p.arrivalTime + "\t" + p.burstTime + "\t" + p.waitingTime + "\t" + p.turnaroundTime);
        }

        // Output: Average waiting and turnaround times
        System.out.println("Average Waiting Time: " + (totalWT / (float) n));
        System.out.println("Average Turnaround Time: " + (totalTAT / (float) n));

        sc.close(); // Close the scanner
    }
}

